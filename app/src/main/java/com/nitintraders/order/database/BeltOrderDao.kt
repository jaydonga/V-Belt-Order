package com.nitintraders.order.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface BeltOrderDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addNewOrder(beltOrder: BeltOrderEntity): Long

    @Update
    suspend fun updateOrder(beltOrder: BeltOrderEntity): Int

    @Delete
    suspend fun deleteOrder(beltOrder: BeltOrderEntity): Int

    @Query("SELECT * FROM ${BeltOrderEntity.TABLE_NAME}")
    suspend fun getAllOrders(): List<BeltOrderEntity>

    @Query("SELECT EXISTS(SELECT * FROM ${BeltOrderEntity.TABLE_NAME} WHERE orderId = :orderId)")
    suspend fun orderExists(orderId: Long): Boolean

}
