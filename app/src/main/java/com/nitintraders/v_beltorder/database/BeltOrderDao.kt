package com.nitintraders.v_beltorder.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface BeltOrderDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addNewOrder(beltOrder: BeltOrderEntity): Long

    @Update
    fun updateOrder(beltOrder: BeltOrderEntity): Int

    @Delete
    fun deleteOrder(beltOrder: BeltOrderEntity): Int

    @Query("SELECT * FROM ${BeltOrderEntity.TABLE_NAME}")
    fun getAllOrders(): List<BeltOrderEntity>

    fun orderExists(orderId: Long): Boolean = getAllOrders().any { it.orderId == orderId }

}
