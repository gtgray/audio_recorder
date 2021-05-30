package tk.atna.audiorecorder.domain.model

import android.net.Uri

data class Record(
    val title: String,
    val link: Uri,
    val playing: Boolean,
    val enabled: Boolean
)
