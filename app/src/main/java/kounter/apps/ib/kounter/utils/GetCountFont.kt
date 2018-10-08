package kounter.apps.ib.kounter.utils

import android.content.Context
import android.graphics.Typeface
import android.util.Log
import android.view.View
import android.widget.TextView
import kotlinx.android.synthetic.main.content_main.*
import java.util.*


class GetCountFont {

    companion object {

        fun setFont(view: TextView,context: Context) {
            Log.d("Locale", Locale.getDefault().isO3Language)

            when (Locale.getDefault().isO3Language) {
                "guj" -> {
                    val face = Typeface.createFromAsset(context.assets, "fonts/guj.ttf")
                    view.typeface = face
                }
                "hin" -> {
                    val face = Typeface.createFromAsset(context.assets, "fonts/hin.ttf")
                    view.typeface = face
                }
                else -> {
                    view.typeface = null
                }
            }
        }
    }

}