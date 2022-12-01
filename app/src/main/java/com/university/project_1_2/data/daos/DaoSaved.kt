package com.university.project_1_2.data.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.university.project_1_2.data.tables.Saved
import kotlinx.coroutines.flow.Flow

@Dao
interface DaoSaved {
    @Insert
    fun insertSaved(saved: Saved)
    @Query("SELECT * FROM saved_quotes")
    fun getAllSavedQs(): Flow<List<Saved>>
    @Query("DELETE FROM saved_quotes WHERE id = :ID")
    fun deleteQsByID(ID:Int)
}