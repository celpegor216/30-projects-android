package fastcampus.aop.pjt24_online_store.data.db

import android.content.Context
import androidx.room.Room

internal fun provideDB(context: Context): ProductDatabase =
    Room.databaseBuilder(context, ProductDatabase::class.java, ProductDatabase.DB_NAME).build()

internal fun provideProductDao(database: ProductDatabase) = database.productDao()