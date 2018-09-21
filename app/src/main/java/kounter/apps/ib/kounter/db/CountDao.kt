package kounter.apps.ib.kounter.db

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*

@Dao
interface CountDao {

    @Query("SELECT * from Count")
    fun getAllCounts(): LiveData<List<Count>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(count: Count?)

    @Delete
    fun deleteCount(count: Count?)

    @Update
    fun updateCount(count: Count?)
}