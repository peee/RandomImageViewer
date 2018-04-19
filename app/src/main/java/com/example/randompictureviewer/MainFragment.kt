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
        val act = activity ?: return
        act.runOnUiThread {
            Snackbar.make(act.window.decorView.rootView, msg, Snackbar.LENGTH_LONG).show()
        }
    }

    override fun setImage(resId: Int, image: Bitmap) {
        val act = activity ?: return
        val v = view ?: return
        act.runOnUiThread {
            v.findViewById<ImageView>(resId).apply {
                setImageBitmap(image)
                alpha = 1F
            }
        }
    }

    override fun deactivateImages(imageResIds: Array<Int>) {
        val act = activity ?: return
        val v = view ?: return
        act.runOnUiThread {
            for (resId in imageResIds) {
                v.findViewById<ImageView>(resId).alpha = 0.5F
            }
        }
    }
}