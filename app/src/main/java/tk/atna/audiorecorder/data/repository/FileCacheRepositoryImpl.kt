package tk.atna.audiorecorder.data.repository

import android.content.Context
import android.net.Uri
import tk.atna.audiorecorder.domain.repository.FileCacheRepository
import tk.atna.audiorecorder.stuff.FileProvider
import java.io.File

class FileCacheRepositoryImpl(
    private val context: Context
) : FileCacheRepository {

    override fun getFile(fileName: String): File = File(getCacheDir(), fileName)

    override fun getCacheDir(): File = FileProvider.getCacheDir(context)

    override fun getCacheDirFiles(): List<Uri> =
        listOf(*getCacheDir().listFiles().orEmpty())
            .map { FileProvider.getUriForFile(context, it) }
}
