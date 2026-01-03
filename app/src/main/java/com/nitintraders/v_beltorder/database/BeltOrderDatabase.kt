package com.nitintraders.v_beltorder.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [BeltOrderEntity::class], version = 1)
abstract class BeltOrderDatabase : RoomDatabase() {

    abstract fun beltOrderDao(): BeltOrderDao

    companion object {
        private const val BELT_ORDER_DB_NAME = "BeltOrders"
        fun getInstance(applicationContext: Context): BeltOrderDatabase {
            return Room
                .databaseBuilder(applicationContext, BeltOrderDatabase::class.java, BELT_ORDER_DB_NAME)
                .build()
        }
    }
}
