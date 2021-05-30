package tk.atna.audiorecorder.presentation.main

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.launch
import tk.atna.audiorecorder.domain.interactor.RecordsInteractor
import tk.atna.audiorecorder.domain.model.Record
import tk.atna.audiorecorder.presentation.base.BaseViewModel
import tk.atna.audiorecorder.stuff.SingleLiveEvent

class MainViewModel(
    private val recordsInteractor: RecordsInteractor
) : BaseViewModel() {

    val records = MutableLiveData<List<Record>>()
    val state = SingleLiveEvent(State.DISABLED)
    val playInternal = SingleLiveEvent<Uri>()
    val playExternal = SingleLiveEvent<Uri>()

    init {
        records.value = recordsInteractor.pullRecords(true)
    }

    fun enableRecording(enable: Boolean) {
        state.value = if (enable) State.IDLE else State.DISABLED
    }

    fun handleRecording() {
        when (state.value) {
            State.IDLE -> launch {
                recordsInteractor.startRecording()
                records.value = recordsInteractor.pullRecords(false)
                state.value = State.RECORDING
            }
            State.RECORDING -> launch {
                recordsInteractor.stopRecording()
                records.value = recordsInteractor.pullRecords(true)
                state.value = State.IDLE
            }
            else -> { /* skip */ }
        }
    }

    fun handlePlaying(record: Record? = null) {
        when (state.value) {
            State.IDLE -> {
                if (record == null) return
                playInternal.value = record.link
                records.value = recordsInteractor.pullRecords(false, record.link)
                state.value = State.PLAYING
            }
            State.PLAYING -> {
                records.value = recordsInteractor.pullRecords(true)
                state.value = State.IDLE
            }
            else -> { /* skip */ }
        }
    }

    fun handleStop() {
        when (state.value) {
            State.RECORDING -> launch {
                recordsInteractor.stopRecording()
                records.value = recordsInteractor.pullRecords(true)
                state.value = State.IDLE
            }
            State.PLAYING -> {
                records.value = recordsInteractor.pullRecords(true)
                state.value = State.IDLE
            }
            else -> { /* skip */ }
        }
    }

    fun forward(record: Record) {
        if (state.value == State.IDLE) {
            // do some stuff
            playExternal.value = record.link
        }
    }

    fun forwardFinished() {
        // do some stuff
        state.value = State.IDLE
    }

    enum class State {
        DISABLED,
        IDLE,
        RECORDING,
        PLAYING
    }
}