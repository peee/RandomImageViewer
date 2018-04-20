package com.example.randompictureviewer

import android.graphics.Bitmap

interface MainContract {
    interface View {
        fun setPresenter(presenter: MainContract.Presenter)
        fun showSnackbar(msg: String)
        fun getNumberOfImages() : Int
        fun setImages(images: Array<Bitmap>)
        fun deactivateImages()
    }

    interface Presenter {
        fun startDownload()
        fun cancelDownload()
        fun isDownloading() : Boolean
    }
}