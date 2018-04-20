package com.example.randompictureviewer

import android.graphics.Bitmap
import kotlinx.coroutines.experimental.*
import java.util.*


class MainPresenter(val view: MainContract.View) : MainContract.Presenter {
    private var downloadJob: Job? = null
    private val deferredTasks = Stack<Deferred<Bitmap>>()
    private var isDownloading = false

    override fun isDownloading() : Boolean {
        return isDownloading
    }

    override fun startDownload() {
        isDownloading = true

        view.deactivateImages()
        view.showSnackbar("Start Download")
        val numOfImages = view.getNumberOfImages()

        downloadJob = launch(CommonPool) {
            for (i in 0..numOfImages) {
                deferredTasks.push(
                        async(CommonPool + coroutineContext) {
                            RandomImageDownloader().downloadImage()
                        })
            }

            val bitmapArray = try {
                Array(numOfImages, { deferredTasks.pop().await() })
            } catch (e: EmptyStackException) {
                emptyArray<Bitmap>()
            }

            view.setImages(bitmapArray)
            isDownloading = false
            view.showSnackbar("Completed")
        }
    }

    override fun cancelDownload() {
        isDownloading = false
        downloadJob?.cancel()
        deferredTasks.clear()
        view.showSnackbar("Cancelled")
    }
}