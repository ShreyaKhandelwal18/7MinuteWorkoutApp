package com.example.a7minutesworkoutapp

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface HistoryDao {

    @Insert
    suspend fun insert(historyEntity: HistoryEntity)


   /* @Delete
    suspend fun delete(historyEntity: HistoryEntity)

    @Update
    suspend fun update(historyEntity: HistoryEntity)
*/
    @Query("SELECT * FROM `history-table`")
   fun fetchAllDate(): Flow<List<HistoryEntity>>


}