package tk.atna.audiorecorder.domain.repository

interface RecordingRepository {
    suspend fun startRecording(link: String)
    suspend fun stopRecording()
}