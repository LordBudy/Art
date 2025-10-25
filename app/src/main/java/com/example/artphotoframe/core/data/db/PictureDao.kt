package com.example.artphotoframe.core.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.artphotoframe.core.data.db.model.PictureEntity
import kotlinx.coroutines.flow.Flow
@Dao
interface PictureDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(picture: PictureEntity)

    @Query("SELECT DISTINCT * FROM pictures WHERE favorites = 1 ORDER BY picture_id ASC")
    fun getAllPictures(): Flow<List<PictureEntity>>

    @Query("SELECT * FROM pictures WHERE picture_id = :id")
    suspend fun getPictureById(id: Int): PictureEntity?

    @Query("DELETE FROM pictures WHERE picture_id = :id")
    suspend fun deletePictureById(id: Int): Int

    @Query("DELETE FROM pictures")
    suspend fun deleteAllPictures(): Int

    @Update
    suspend fun updatePictures(pictures: List<PictureEntity>)
}
