package kounter.apps.ib.kounter.db

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "Count")
data class Count(@PrimaryKey(autoGenerate = true) var id: Long?,
                            @ColumnInfo(name = "count") var count: Int,
                            @ColumnInfo(name = "name") var name: String,
                            @ColumnInfo(name = "timestamp") var timestamp: Long,
                            var selected: Boolean = true) {

    constructor():this(null,0,"",0,true)

}