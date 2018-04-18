package com.example.randompictureviewer

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.ByteArrayInputStream
import java.util.concurrent.TimeUnit

private const val PLACE_IMG_URL = "http://placeimg.com/640/480/any"

class RandomImageDownloader {
    private val client = OkHttpClient()
    private val request = Request.Builder().url(PLACE_IMG_URL).build()

    fun downloadImage() : Bitmap {
        val response = client.newCall(request).execute()
        val bytes = response?.body()?.byteStream() ?: ByteArrayInputStream(ByteArray(0))

        return BitmapFactory.decodeStream(bytes)
    }
}