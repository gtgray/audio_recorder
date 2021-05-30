package tk.atna.audiorecorder.stuff

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.fragment.app.Fragment

fun Fragment.playExternal(uri: Uri, requestCode: Int) {
    val intent = Intent(Intent.ACTION_VIEW).apply {
        data = uri
        type = "audio/*"
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }
    try {
        startActivityForResult(intent, requestCode)
    } catch (e: ActivityNotFoundException) {
        startActivityForResult(Intent.createChooser(intent, "Open with").addFlags(intent.flags), requestCode)
    }
}

fun Context.openApplicationSettings(): Intent {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
    val uri = Uri.fromParts("package", packageName, null)
    intent.data = uri
    return intent
}