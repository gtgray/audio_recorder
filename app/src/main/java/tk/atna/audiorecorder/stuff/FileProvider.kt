package tk.atna.audiorecorder.stuff

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import tk.atna.audiorecorder.BuildConfig
import java.io.File

class FileProvider : FileProvider() {

    companion object {

        fun getUriForFile(context: Context, file: File): Uri {
            return getUriForFile(context, BuildConfig.APPLICATION_ID, file)
        }

        fun getCacheDir(context: Context): File {
            val cacheDir = File(context.cacheDir, "audio")
            if (!cacheDir.exists()) cacheDir.mkdir()
            return cacheDir
        }
    }
}