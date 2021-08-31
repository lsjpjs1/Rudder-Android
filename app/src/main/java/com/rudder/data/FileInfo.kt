package com.rudder.data

import android.net.Uri

data class FileInfo(
    val uri: Uri,
    val mimeType: String,
    val filePath: String
)
