package tk.atna.audiorecorder.data.datasource

class AudioRecorderDataSource {

    companion object {

        init {
            System.loadLibrary("audioRecorder")
        }

        // Native methods
        @JvmStatic external fun create(): Boolean
        @JvmStatic external fun delete()
        @JvmStatic external fun startRecording(fullPathToFile: String)
        @JvmStatic external fun stopRecording()
    }
}