package com.example.randompictureviewer

import android.graphics.Bitmap
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.ImageView
import kotlinx.coroutines.experimental.*
import kotlinx.coroutines.experimental.android.UI
import java.util.*

private val IMAGE_VIEWS = arrayOf(R.id.main_img1, R.id.main_img2, R.id.main_img3, R.id.main_img4,
        R.id.main_img5, R.id.main_img6, R.id.main_img7, R.id.main_img8)

class MainActivity : AppCompatActivity() {
    private var downloadJob: Job? = null
    private val deferredTasks = Stack<Deferred<Bitmap>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.main_button_download).setOnClickListener {
            downloadJob = launch(CommonPool) {
                deactivateAllImages()

                showSnackbar("Start download")
                val downloader = RandomImageDownloader()

                for (i in 0..IMAGE_VIEWS.size) {
                    deferredTasks.push(async(CommonPool + coroutineContext) { downloader.downloadImage() })
                }

                try {
                    for (resId in IMAGE_VIEWS) {
                        setImage(resId, deferredTasks.pop().await())
                    }
                } catch (e: EmptyStackException) {
                    // NOP
                }

                showSnackbar("Completed")
            }
        }

        findViewById<Button>(R.id.main_button_cancel).setOnClickListener {
            cancelTasks()
            showSnackbar("Cancelled")
        }
    }

    private fun cancelTasks() {
        downloadJob?.cancel()
        deferredTasks.clear()
    }

    private fun showSnackbar(msg: String) {
        Snackbar.make(window.decorView, msg, Snackbar.LENGTH_LONG).show()
    }

    private suspend fun setImage(resId: Int, img: Bitmap?) = withContext(UI) {
        findViewById<ImageView>(resId).apply {
            setImageBitmap(img)
            alpha = 1F
        }
    }

    private suspend fun deactivateAllImages() = withContext(UI) {
        for (resId in IMAGE_VIEWS) {
            findViewById<ImageView>(resId).alpha = 0.5F
        }
    }
}
