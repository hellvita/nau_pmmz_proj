package com.university.project_1_2.data.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.university.project_1_2.data.tables.History
import kotlinx.coroutines.flow.Flow

@Dao
interface DaoHistory {
    @Insert (onConflict = OnConflictStrategy.REPLACE)
    fun insertHistory(history: History)
    @Query("SELECT * FROM history_quotes WHERE id > (SELECT MAX(id)-50  FROM history_quotes) ORDER BY time DESC")
    fun getHistory(): Flow<List<History>>
}