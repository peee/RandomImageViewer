package com.example.randompictureviewer

import android.graphics.Bitmap
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import kotlinx.coroutines.experimental.*
import java.util.*

private val IMAGE_VIEWS = arrayOf(R.id.main_img1, R.id.main_img2, R.id.main_img3, R.id.main_img4,
        R.id.main_img5, R.id.main_img6, R.id.main_img7, R.id.main_img8)

class MainActivity : AppCompatActivity() {
    private lateinit var downloadJob: Job
    private val deferredTasks = Stack<Deferred<Bitmap>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.main_button_download).setOnClickListener {
            downloadJob = launch {
                deactivateAllImages()

                toast("Start download")
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

                toast("Completed")
            }
        }

        findViewById<Button>(R.id.main_button_cancel).setOnClickListener {
            cancelTasks()
            toast("Cancelled")
        }
    }

    private fun cancelTasks() {
        downloadJob.cancel()
        deferredTasks.clear()
    }

    private fun toast(msg: String) {
        runOnUiThread {
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
        }
    }

    private fun setImage(resId: Int, img: Bitmap?) {
        runOnUiThread {
            findViewById<ImageView>(resId).apply {
                setImageBitmap(img)
                alpha = 1F
            }
        }
    }

    private fun deactivateAllImages() {
        runOnUiThread {
            for (resId in IMAGE_VIEWS) {
                findViewById<ImageView>(resId).alpha = 0.5F
            }
        }
    }
}
