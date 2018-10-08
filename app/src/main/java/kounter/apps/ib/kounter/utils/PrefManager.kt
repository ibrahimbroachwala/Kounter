package kounter.apps.ib.kounter.utils

import android.content.Context
import android.content.SharedPreferences
import io.opencensus.stats.Aggregation
import kounter.apps.ib.kounter.db.Count

class PrefManager(context: Context) {


    var prefs: SharedPreferences
    var editor: SharedPreferences.Editor


    // Shared pref mode
    private var PRIVATE_MODE = 0

    // Sharedpref file name
    private val PREF_NAME = "kounter"

    private val ACTIVE_COUNT = "active_count"
    private val ACTIVE_THEME = "active_theme"
    private val ACTIVE_COUNT_NAME = "active_count_name"


    init {
        prefs = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE)
        editor = prefs.edit()
    }


    fun getActiveCount(): Int {
        return prefs.getInt(ACTIVE_COUNT, 0)
    }

    fun getActiveCountName(): String {
        return prefs.getString(ACTIVE_COUNT_NAME, "")!!
    }

    fun setActiveCount(count: Count) {
        editor.putInt(ACTIVE_COUNT, count.count)
        editor.putString(ACTIVE_COUNT_NAME, count.name)
        editor.commit()
    }

    fun setActiveTheme(theme: Themes) {

        editor.putString(ACTIVE_THEME, theme.name)
        editor.commit()
    }

    fun getActiveTheme(): Themes {
        val theme = prefs.getString(ACTIVE_THEME, "")

        if (theme == Themes.DARK.name)
            return Themes.DARK
        else
            return Themes.LIGHT

    }

}