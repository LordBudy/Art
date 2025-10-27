package com.example.artphotoframe.core.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.artphotoframe.core.data.db.model.PictureEntity


@Database(entities = [PictureEntity::class], version = 1, exportSchema = true)
abstract class AppDatabase: RoomDatabase() {

    abstract fun pictureDao(): PictureDao

    companion object {
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "pictures_database"
                )
                    //пересоздаст таблици с потерей всех данных без миграции
                   // .fallbackToDestructiveMigration(false)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}