package com.nitintraders.v_beltorder.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update

@Dao
interface BeltOrderDao {

    @Insert
    fun addNewOrder(beltOrder: BeltOrderEntity)

    @Update
    fun updateOrder(beltOrder: BeltOrderEntity)

    @Delete
    fun deleteOrder(beltOrder: BeltOrderEntity)
}
