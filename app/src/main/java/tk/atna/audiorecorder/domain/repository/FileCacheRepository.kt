package tk.atna.audiorecorder.domain.repository

import android.net.Uri
import java.io.File

interface FileCacheRepository {
    fun getFile(fileName: String): File
    fun getCacheDir(): File
    fun getCacheDirFiles(): List<Uri>
}