package fastcampus.aop.pjt30_food_delivery.di

import android.content.Context
import androidx.room.Room
import fastcampus.aop.pjt30_food_delivery.data.db.ApplicationDatabase

fun provideDB(context: Context): ApplicationDatabase =
    Room.databaseBuilder(context, ApplicationDatabase::class.java, ApplicationDatabase.DB_NAME).build()

fun provideLocationDao(database: ApplicationDatabase) = database.LocationDao()