package com.rudder.util

import android.R.attr.bitmap
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.graphics.BitmapFactory
import android.media.ExifInterface
import android.net.Uri
import com.bumptech.glide.load.resource.bitmap.TransformationUtils.rotateImage
import java.io.*


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


    //이미지 파일 압축 함수
    fun getDownsizedImageBytes(filePath: String): ByteArray? {
        val ei = ExifInterface(filePath)
        val orientation: Int = ei.getAttributeInt(
            ExifInterface.TAG_ORIENTATION,
            ExifInterface.ORIENTATION_UNDEFINED
        )
        val file = File(filePath)

        val scaleDivider = 4
        var inputStream = FileInputStream(file)
        var fullBitmap =BitmapFactory.decodeStream(inputStream)
        inputStream.close()

        when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> fullBitmap = rotateImage(fullBitmap, 90)
            ExifInterface.ORIENTATION_ROTATE_180 -> fullBitmap = rotateImage(fullBitmap, 180)
            ExifInterface.ORIENTATION_ROTATE_270 -> fullBitmap = rotateImage(fullBitmap, 270)
            ExifInterface.ORIENTATION_NORMAL -> fullBitmap = fullBitmap
            else -> fullBitmap = fullBitmap
        }

        // 2. Get the downsized image content as a byte[]
        val scaleWidth: Int = fullBitmap.width / scaleDivider
        val scaleHeight: Int = fullBitmap.height / scaleDivider
        val scaledBitmap = Bitmap.createScaledBitmap(fullBitmap!!, scaleWidth, scaleHeight, true)

        // 2. Instantiate the downsized image content as a byte[]
        val baos = ByteArrayOutputStream()
        scaledBitmap.compress(CompressFormat.JPEG, 70, baos)
        return baos.toByteArray()
    }



}