package com.nitintraders.v_beltorder.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nitintraders.v_beltorder.data.BeltItem
import com.nitintraders.v_beltorder.databinding.ItemBeltOrderBinding

class BeltItemAdapter(private val beltItems: List<BeltItem>) : RecyclerView.Adapter<BeltItemAdapter.BeltItemViewHolder>() {

    private val mutableBeltItems: MutableList<BeltItem> = beltItems.toMutableList()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): BeltItemViewHolder {
        val binding = ItemBeltOrderBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return BeltItemViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: BeltItemViewHolder,
        position: Int,
    ) {
        holder.bind(mutableBeltItems[position])
    }

    override fun getItemCount() = mutableBeltItems.size

    class BeltItemViewHolder(
        private val binding: ItemBeltOrderBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(beltItem: BeltItem) {
            binding.editTextSizeInInch.setText(beltItem.size.toString())
            binding.editTextQuantity.setText(beltItem.quantity.toString())
        }
    }

    fun updateList(newBeltItems: List<BeltItem>) {
        mutableBeltItems.clear()
        mutableBeltItems.addAll(newBeltItems)
        notifyDataSetChanged()
    }

    fun addBlankItems(blankItems: Int) {
        val newItems = mutableListOf<BeltItem>()
        newItems.addAll(beltItems)
        repeat(blankItems) {
            newItems.add(BeltItem())
        }
        updateList(newItems)
    }
}
