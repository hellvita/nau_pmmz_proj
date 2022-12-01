package com.university.project_1_2.data.dbs

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.university.project_1_2.data.daos.DaoGenerated
import com.university.project_1_2.data.daos.DaoHistory
import com.university.project_1_2.data.daos.DaoSaved
import com.university.project_1_2.data.tables.Generated
import com.university.project_1_2.data.tables.History
import com.university.project_1_2.data.tables.Saved

@Database (
    entities = [Generated::class, Saved::class, History::class],
    version = 1
)
abstract class MainDB:RoomDatabase() {
    abstract fun getDaoGenerated():DaoGenerated
    abstract fun getDaoSaved():DaoSaved
    abstract fun getDaoHistory():DaoHistory
    companion object{
        fun getDB(context: Context):MainDB{
            return Room.databaseBuilder(
                context.applicationContext,
                MainDB::class.java,
                "nau_proj_db4"
            ).build()
        }
    }
}