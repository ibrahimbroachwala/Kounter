package kounter.apps.ib.kounter.db

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context

@Database(entities = arrayOf(Count::class), version = 1)
abstract class CountDatabase : RoomDatabase() {

    abstract fun countDao(): CountDao

    companion object {
        private var INSTANCE: CountDatabase? = null

        const val DB_NAME = "kounter-main"

        fun getInstance(context: Context): CountDatabase? {
            if (INSTANCE == null) {
                synchronized(CountDatabase::class) {
                    INSTANCE = Room.databaseBuilder(context,
                            CountDatabase::class.java, DB_NAME)
                            .build()
                }
            }
            return INSTANCE
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }
}