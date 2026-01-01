package com.nitintraders.v_beltorder.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [BeltOrderEntity::class], version = 1)
abstract class BeltOrderDatabase : RoomDatabase() {

    abstract fun beltOrderDao(): BeltOrderDao
}
