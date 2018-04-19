package com.example.randompictureviewer

import android.graphics.Bitmap

interface MainContract {
    interface View {
        fun setPresenter(presenter: MainContract.Presenter)
        fun showSnackbar(msg: String)
        fun setImage(resId: Int, image: Bitmap)
        fun deactivateImages(imageResIds: Array<Int>)
    }

    interface Presenter {
        fun startDownload()
        fun cancelDownload()
        fun isDownloading() : Boolean
    }
}