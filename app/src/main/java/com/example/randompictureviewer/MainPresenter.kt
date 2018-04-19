package com.example.randompictureviewer

import android.graphics.Bitmap
import kotlinx.coroutines.experimental.*
import java.util.*

private val IMAGE_VIEWS = arrayOf(R.id.main_img1, R.id.main_img2, R.id.main_img3, R.id.main_img4,
        R.id.main_img5, R.id.main_img6, R.id.main_img7, R.id.main_img8)

class MainPresenter(val view: MainContract.View) : MainContract.Presenter {
    private var downloadJob: Job? = null
    private val deferredTasks = Stack<Deferred<Bitmap>>()
    private var isDownloading = false

    override fun isDownloading() : Boolean {
        return isDownloading
    }

    override fun startDownload() {
        isDownloading = true

        view.deactivateImages(IMAGE_VIEWS)
        view.showSnackbar("Start Download")

        downloadJob = launch(CommonPool) {
            val downloader = RandomImageDownloader()

            for (i in 0..IMAGE_VIEWS.size) {
                deferredTasks.push(async(CommonPool + coroutineContext) { downloader.downloadImage() })
            }

            try {
                for (resId in IMAGE_VIEWS) {
                    view.setImage(resId, deferredTasks.pop().await())
                }
            } catch (e: EmptyStackException) {
                // NOP
            }

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