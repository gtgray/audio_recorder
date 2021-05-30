package tk.atna.audiorecorder.domain.interactor

import android.net.Uri
import tk.atna.audiorecorder.domain.model.Record
import tk.atna.audiorecorder.domain.repository.FileCacheRepository
import tk.atna.audiorecorder.domain.repository.RecordingRepository

class RecordsInteractor(
    private val fileCacheRepository: FileCacheRepository,
    private val recordingRepository: RecordingRepository
) {

    fun pullRecords(enabled: Boolean, playing: Uri? = null): List<Record> {
        return fileCacheRepository.getCacheDirFiles().toModel(enabled, playing)
    }

    suspend fun startRecording() {
        val name = "${System.currentTimeMillis()}$DEFAULT_EXTENSION"
        val file = fileCacheRepository.getFile(name)
        recordingRepository.startRecording(file.absolutePath)
    }

    suspend fun stopRecording() {
        recordingRepository.stopRecording()
    }

    private fun List<Uri>.toModel(enabled: Boolean, playing: Uri?) = map {
        Record(it.toString(), it, it == playing, enabled)
    }

    companion object {
        const val DEFAULT_EXTENSION = ".wav"
    }
}