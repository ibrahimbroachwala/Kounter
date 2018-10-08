package kounter.apps.ib.kounter.utils

import android.app.Activity
import android.content.Intent
import kounter.apps.ib.kounter.R


enum class Themes {
    LIGHT, DARK
}

class Theme {

    companion object {

        var selectedTheme= Themes.LIGHT

        fun selectTheme(activity: Activity,theme: Themes){

            when(theme){
                Themes.LIGHT -> {
                    selectedTheme = theme
                    activity.startActivity(Intent(activity, activity::class.java))
                    activity.finish()

                }
                Themes.DARK -> {
                    selectedTheme = theme
                    activity.startActivity(Intent(activity, activity::class.java))
                    activity.finish()

                }
            }
        }


        fun setTheme(activity: Activity){

            val prefs = PrefManager(activity)
            selectedTheme = prefs.getActiveTheme()

            when(selectedTheme){
                Themes.LIGHT -> {
                    activity.setTheme(R.style.AppThemeLight)
                }
                Themes.DARK -> {
                    activity.setTheme(R.style.AppThemeDark)
                }
            }
        }


    }

}


