package kounter.apps.ib.kounter

import android.app.Application
import android.arch.lifecycle.*
import android.arch.lifecycle.LiveData
import android.os.AsyncTask
import android.os.Bundle
import android.view.View
import kounter.apps.ib.kounter.db.Count
import kounter.apps.ib.kounter.db.CountDao
import kounter.apps.ib.kounter.db.CountDatabase
import kounter.apps.ib.kounter.utils.PrefManager


class CountViewModel(application: android.app.Application) : AndroidViewModel(application), LifecycleObserver {


    //Declarations
    val countChange = MutableLiveData<Int>()
    private var allCounts: LiveData<List<Count>>

    var db: CountDatabase = CountDatabase.getInstance(application)!!
    var sharedPref: PrefManager

//    private val COUNT_KEY = "count_key"

    var count: Int = 0
    var incrementBy: Int = 1



    //Initializer
    init {
        allCounts = db.countDao().getAllCounts()
        sharedPref = PrefManager(application)
    }



    //Current Count Functions

    fun setActiveCount(count: Int){
        countChange.value = count
    }
    fun increment() {
        count += incrementBy
        countChange.value = count }

    fun incrementBy(value: Int) { incrementBy = value}

    fun resetCount() {
        count = 0
        countChange.value = 0}

    fun undoCount() {
        if (count-incrementBy>-1) {
            count -= incrementBy
            countChange.value = count
        }
    }


    //save and Restore Count
//    fun saveState(outState: Bundle) {
//        outState.putInt(COUNT_KEY, count)
//    }
//
//    fun restoreState(inState: Bundle?) {
//        inState?.let { count = inState.getInt(COUNT_KEY) }
//    }




    //Database Functions
    fun getAllCounts(): LiveData<List<Count>> {
        return allCounts
    }

    fun saveCount(count: Count) {
        insertAsyncTask(db).execute(count)
    }

    fun deleteCount(count: Count){
        deleteAsyncTask(db).execute(count)

    }

    fun updateCount(count: Count){
        updateAsyncTask(db).execute(count)
    }




    //Database AsyncTasks
    private class insertAsyncTask(val db: CountDatabase) : AsyncTask<Count, Void, Void>() {

        override fun doInBackground(vararg params: Count?): Void? {
            db.countDao().insert(params[0])
            return null
        }

    }

    private class deleteAsyncTask(val db: CountDatabase) : AsyncTask<Count, Void, Void>() {

        override fun doInBackground(vararg params: Count?): Void? {
            db.countDao().deleteCount(params[0])
            return null
        }

    }

    private class updateAsyncTask(val db: CountDatabase) : AsyncTask<Count, Void, Void>() {

        override fun doInBackground(vararg params: Count?): Void? {
            db.countDao().updateCount(params[0])
            return null
        }

    }

    //Lifecycle Observer
    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() { countChange.value = sharedPref.getActiveCount()
    count = sharedPref.getActiveCount()}

}