package com.rudder.util

import android.content.Context
import android.net.Uri
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream

object FileUtil {
    // 절대경로 파악할 때 사용된 메소드
    fun createCopyAndReturnRealPath(context: Context, uri: Uri): String? {
        val contentResolver = context.contentResolver ?: return null


        // 파일 경로를 만듬
        val filePath = (context.applicationInfo.dataDir + File.separator
                + System.currentTimeMillis())
        val file = File(filePath)
        try {
            // 매개변수로 받은 uri 를 통해  이미지에 필요한 데이터를 불러 들인다.
            val inputStream = contentResolver.openInputStream(uri) ?: return null
            // 이미지 데이터를 다시 내보내면서 file 객체에  만들었던 경로를 이용한다. 
            val outputStream: OutputStream = FileOutputStream(file)
            val buf = ByteArray(1024)
            var len: Int
            while (inputStream.read(buf).also { len = it } > 0) outputStream.write(buf, 0, len)
            outputStream.close()
            inputStream.close()
        } catch (ignore: IOException) {
            return null
        }
        return file.absolutePath
    }
}