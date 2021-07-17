package com.MitPatel0801.whatsappstatussaverapp.Data.db

import android.net.Uri

data class StatusValue(
    var fileUri: Uri,
    val fileName:String,
    var isVideo: Boolean = false
)
