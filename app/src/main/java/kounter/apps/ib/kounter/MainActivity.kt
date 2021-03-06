package kounter.apps.ib.kounter

import android.annotation.SuppressLint
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetBehavior
import androidx.recyclerview.widget.LinearLayoutManager
import android.text.TextUtils
import android.view.View
import kotlinx.android.synthetic.main.bottom_sheet_all_counts.*
import kotlinx.android.synthetic.main.content_main.*
import kounter.apps.ib.kounter.adapter.CountsAdapter
import kounter.apps.ib.kounter.db.Count
import android.os.Vibrator
import androidx.annotation.IntegerRes
import androidx.annotation.RequiresApi
import com.google.android.material.snackbar.Snackbar
import androidx.core.content.ContextCompat
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kounter.apps.ib.kounter.adapter.ItemClick
import kounter.apps.ib.kounter.utils.Theme
import kounter.apps.ib.kounter.utils.Themes
import android.widget.TextView
import kounter.apps.ib.kounter.utils.PrefManager
import android.content.pm.PackageManager
import android.R.attr.versionName
//import com.google.android.gms.common.util.ClientLibraryUtils.getPackageInfo
import android.content.pm.PackageInfo
import android.graphics.Typeface
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_count.*
import kounter.apps.ib.kounter.utils.GetCountFont
import java.util.*


class MainActivity : AppCompatActivity(), ItemClick {


    private val TAG: String = MainActivity::class.java.simpleName

    val viewModel: CountViewModel by lazy {
        ViewModelProviders.of(this).get(CountViewModel::class.java)
    }

    private val countObserver =
            Observer<Int> { value ->
                value?.let { setCount(value) }
            }

    private val allCountsObserver =
            Observer<List<Count>> { value ->
                value?.let { loadCounts(value) }
            }

    var prefs: PrefManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Theme.setTheme(this@MainActivity)
        setContentView(R.layout.activity_main)
        GetCountFont.setFont(count_text,this@MainActivity)


        val vibe = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        val bottomSheetBehavior = BottomSheetBehavior.from(bottom_sheet)


        if (Theme.selectedTheme == Themes.DARK) {
            theme_button_dark.isChecked = true
            themeHandler(Themes.DARK)
        } else {
            theme_button_light.isChecked = true
            themeHandler(Themes.LIGHT)
        }


        prefs = PrefManager(this@MainActivity)

        lifecycle.addObserver(viewModel)

//        viewModel.restoreState(savedInstanceState)

        viewModel.countChange.observe(this, countObserver)

        viewModel.getAllCounts().observe(this, allCountsObserver)

        background.setOnClickListener {
            vibe.vibrate(20)
            viewModel.increment()
        }

        reset_count.setOnClickListener {
            viewModel.resetCount()
            count_name.setText("")
        }

        undo_count.setOnClickListener {
            viewModel.undoCount() }

        save_count.setOnClickListener { saveCount() }


        theme_group.setOnCheckedChangeListener { _, i ->
            when (i) {
                R.id.theme_button_dark -> {
                    prefs!!.setActiveTheme(Themes.DARK)
                    Theme.selectTheme(this, Themes.DARK)
                }

                R.id.theme_button_light -> {
                    prefs!!.setActiveTheme(Themes.LIGHT)
                    Theme.selectTheme(this, Themes.LIGHT)
                }
            }
        }

        bottomSheetBehavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(p0: View, p1: Float) {
            }

            override fun onStateChanged(p0: View, state: Int) {
                when (state) {
                    BottomSheetBehavior.STATE_DRAGGING -> {
                        background.isClickable = false
                    }
                    BottomSheetBehavior.STATE_EXPANDED, BottomSheetBehavior.STATE_HALF_EXPANDED -> {
                        background.isClickable = false
                    }
                    BottomSheetBehavior.STATE_HIDDEN, BottomSheetBehavior.STATE_COLLAPSED -> {
                        background.isClickable = true
                    }
                }
            }
        })


        try {
            val pInfo = this.packageManager.getPackageInfo(packageName, 0)
            val version = pInfo.versionName
            app_version.text = resources.getString(R.string.app_version,version)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

    }




//    override fun onSaveInstanceState(outState: Bundle) {
//        super.onSaveInstanceState(outState)
//        viewModel.saveState(outState)
//    }

    private fun setCount(count: Int) {
        count_text.text = count.toString()
    }

    private fun loadCounts(listCounts: List<Count>) {

        //Set bottom sheet header
        if (listCounts.isEmpty())
            bottom_sheet_header.text = resources.getString(R.string.bottom_sheet_header_no_count)
        else
            bottom_sheet_header.text = resources.getString(R.string.bottom_sheet_header)

        //Set recyclerview adapter
        counts_rv.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        counts_rv.adapter = CountsAdapter(this, listCounts, this)


    }

    private fun saveCount() {
        val count = Count()
        count.count = Integer.parseInt(count_text.text.toString())
        if (TextUtils.isEmpty(count_name.text.toString()))
            count.name = "New Count"
        else
            count.name = count_name.text.toString()
        count.timestamp = System.currentTimeMillis()

        viewModel.saveCount(count)
        showSnackbar(resources.getString(R.string.m_saved))

    }

    override fun updateCount(item: Count) {

        viewModel.updateCount(item)
        showSnackbar(resources.getString(R.string.m_update))

    }

    override fun deleteCount(item: Count) {

        viewModel.deleteCount(item)
        showSnackbar(resources.getString(R.string.m_delete))
    }

    @SuppressLint("NewApi")
    private fun themeHandler(theme: Themes) {
        when (theme) {
            Themes.LIGHT -> {
//                bottom_sheet.setBackgroundResource(R.drawable.bottom_sheet_bg_light)
                bottom_sheet_handle.setBackgroundResource(R.drawable.handle_bg_dark)

                undo_count.backgroundTintList = ContextCompat.getColorStateList(this,R.color.colorPrimary)
                undo_count.setTextColor(ContextCompat.getColor(this,R.color.colorPrimaryDark))
                undo_count.iconTint = ContextCompat.getColorStateList(this,R.color.colorPrimaryDark)

                save_count.backgroundTintList = ContextCompat.getColorStateList(this,R.color.colorPrimary)
                save_count.setTextColor(ContextCompat.getColor(this,R.color.colorPrimaryDark))
                save_count.iconTint = ContextCompat.getColorStateList(this,R.color.colorPrimaryDark)

                reset_count.backgroundTintList = ContextCompat.getColorStateList(this,R.color.colorPrimary)
                reset_count.setTextColor(ContextCompat.getColor(this,R.color.colorPrimaryDark))
                reset_count.iconTint = null


            }
            Themes.DARK -> {
//                bottom_sheet.setBackgroundResource(R.drawable.bottom_sheet_bg_dark)
                bottom_sheet_handle.setBackgroundResource(R.drawable.handle_bg_light)

                undo_count.backgroundTintList = ContextCompat.getColorStateList(this, R.color.colorPrimaryDark)
                undo_count.setTextColor(ContextCompat.getColorStateList(this,R.color.colorPrimary))
                undo_count.iconTint = ContextCompat.getColorStateList(this, R.color.colorPrimary)

                save_count.backgroundTintList = ContextCompat.getColorStateList(this, R.color.colorPrimaryDark)
                save_count.setTextColor(ContextCompat.getColorStateList(this,R.color.colorPrimary))
                save_count.iconTint = ContextCompat.getColorStateList(this, R.color.colorPrimary)

                reset_count.backgroundTintList = ContextCompat.getColorStateList(this, R.color.colorPrimaryDark)
                reset_count.setTextColor(ContextCompat.getColorStateList(this,R.color.colorPrimary))
                reset_count.iconTint = null

            }
        }
    }


    @SuppressLint("NewApi")
    fun showSnackbar(message: String) {
        val mySnackbar = Snackbar.make(bottom_sheet,
                message, Snackbar.LENGTH_SHORT)
        val snackbarView = mySnackbar.view
        snackbarView.elevation = 1000f

        val textView = snackbarView.findViewById(R.id.snackbar_text) as TextView

        if(Theme.selectedTheme == Themes.DARK) {
            snackbarView.backgroundTintList = ContextCompat.getColorStateList(this, R.color.colorPrimary)
            textView.setTextColor(ContextCompat.getColor(this,R.color.colorPrimaryDark))

        }
        else {
            snackbarView.backgroundTintList = ContextCompat.getColorStateList(this, R.color.colorPrimaryDark)
            textView.setTextColor(ContextCompat.getColor(this,R.color.colorPrimary))

        }

        mySnackbar.show()
    }


    override fun onPause() {
        super.onPause()
        Log.d("lifecycle","onPause called")
        val count = Count()
        count.name = count_name.text.toString().trim()
        count.count = Integer.valueOf(count_text.text.toString())

        prefs!!.setActiveCount(count)

    }


    override fun onResume() {
        super.onResume()

        if(prefs!=null)
        viewModel.setActiveCount(prefs!!.getActiveCount())
        Log.d("lifecycle","onResume called")
    }


}
