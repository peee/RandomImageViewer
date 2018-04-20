package com.example.randompictureviewer

import android.graphics.Bitmap
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView

private val IMAGE_VIEWS = intArrayOf(R.id.main_img1, R.id.main_img2, R.id.main_img3, R.id.main_img4,
        R.id.main_img5, R.id.main_img6, R.id.main_img7, R.id.main_img8)

class MainFragment : Fragment(), MainContract.View {
    private var presenter: MainContract.Presenter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_main, container)

        setPresenter(MainPresenter(this))
        v.findViewById<FloatingActionButton>(R.id.main_fab).setOnClickListener {
            if (presenter!!.isDownloading()) {
                presenter!!.cancelDownload()
            } else {
                presenter!!.startDownload()
            }
        }
        return v
    }

    override fun setPresenter(presenter: MainContract.Presenter) {
        this.presenter = presenter
    }

    override fun showSnackbar(msg: String) {
        val coordinator = view?.findViewById<View>(R.id.main_coordinator) ?: return

        activity?.runOnUiThread {
            Snackbar.make(coordinator, msg, Snackbar.LENGTH_LONG).show()
        }
    }

    override fun getNumberOfImages() : Int {
        return IMAGE_VIEWS.size
    }

    override fun setImages(images: Array<Bitmap>) {
        activity?.runOnUiThread {
            var i = 0
            for (image in images) {
                view?.findViewById<ImageView>(IMAGE_VIEWS[i++])?.apply {
                    setImageBitmap(image)
                    alpha = 1F
                }
            }
        }
    }

    override fun deactivateImages() {
        activity?.runOnUiThread {
            for (resId in IMAGE_VIEWS) {
                view?.findViewById<ImageView>(resId)?.alpha = 0.5F
            }
        }
    }
}