package tk.atna.audiorecorder.data.repository

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import tk.atna.audiorecorder.data.datasource.AudioRecorderDataSource
import tk.atna.audiorecorder.domain.repository.RecordingRepository

class RecordingRepositoryImpl : RecordingRepository {

    override suspend  fun startRecording(link: String) {
        CoroutineScope(Dispatchers.Default).launch {
            AudioRecorderDataSource.create()
            AudioRecorderDataSource.startRecording(link)
        }
    }

    override suspend  fun stopRecording() {
        AudioRecorderDataSource.stopRecording()
        AudioRecorderDataSource.delete()
    }
}