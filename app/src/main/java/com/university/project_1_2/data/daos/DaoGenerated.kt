package com.university.project_1_2.data.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.university.project_1_2.data.tables.Generated
import kotlinx.coroutines.flow.Flow

@Dao
interface DaoGenerated {
    @Insert
    fun insertItem(generated: Generated)
    @Query("SELECT * FROM generated_quotes")
    fun getAllItem(): Flow<List<Generated>>
    @Query("SELECT * FROM generated_quotes WHERE id = (SELECT MAX(id)  FROM generated_quotes)")
    fun getLastItem(): Flow<List<Generated>>
    @Query("SELECT * FROM generated_quotes WHERE id > (SELECT MAX(id)-10  FROM generated_quotes)")
    fun getLastTenItems(): Flow<List<Generated>>
    @Query("SELECT * FROM generated_quotes WHERE id = (SELECT MAX(id)-9  FROM generated_quotes)")
    fun getFirstItemFromList(): Flow<List<Generated>>
    @Query("SELECT * FROM generated_quotes WHERE id = (SELECT MAX(id)-8  FROM generated_quotes)")
    fun getSecondItemFromList(): Flow<List<Generated>>
    @Query("SELECT * FROM generated_quotes WHERE id = (SELECT MAX(id)-7  FROM generated_quotes)")
    fun getThirdItemFromList(): Flow<List<Generated>>
    @Query("SELECT * FROM generated_quotes WHERE id = (SELECT MAX(id)-6  FROM generated_quotes)")
    fun getFourthItemFromList(): Flow<List<Generated>>
    @Query("SELECT * FROM generated_quotes WHERE id = (SELECT MAX(id)-5  FROM generated_quotes)")
    fun getFifthItemFromList(): Flow<List<Generated>>
    @Query("SELECT * FROM generated_quotes WHERE id = (SELECT MAX(id)-4  FROM generated_quotes)")
    fun getSixthItemFromList(): Flow<List<Generated>>
    @Query("SELECT * FROM generated_quotes WHERE id = (SELECT MAX(id)-3  FROM generated_quotes)")
    fun getSeventhItemFromList(): Flow<List<Generated>>
    @Query("SELECT * FROM generated_quotes WHERE id = (SELECT MAX(id)-2  FROM generated_quotes)")
    fun getEighthItemFromList(): Flow<List<Generated>>
    @Query("SELECT * FROM generated_quotes WHERE id = (SELECT MAX(id)-1  FROM generated_quotes)")
    fun getNinthItemFromList(): Flow<List<Generated>>
}