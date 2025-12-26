package com.nitintraders.v_beltorder.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nitintraders.v_beltorder.data.BeltItem
import com.nitintraders.v_beltorder.databinding.ItemBeltOrderBinding

class BeltItemAdapter() : RecyclerView.Adapter<BeltItemAdapter.BeltItemViewHolder>() {

    private var itemClickListener: ((item: BeltItem, index: Int) -> Unit)? = null

    private val beltItems = mutableListOf<BeltItem>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): BeltItemViewHolder {
        val binding = ItemBeltOrderBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return BeltItemViewHolder(binding, itemClickListener)
    }

    override fun onBindViewHolder(
        holder: BeltItemViewHolder,
        position: Int,
    ) {
        holder.bind(beltItems[position])
    }

    override fun getItemCount() = beltItems.size

    inner class BeltItemViewHolder(
        private val binding: ItemBeltOrderBinding,
        private val itemClickListener: ((item: BeltItem, index: Int) -> Unit)?,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(beltItem: BeltItem) {
            beltItem.size?.let {
                binding.editTextSizeInInch.setText(it)
            }
            beltItem.quantity?.let {
                binding.editTextQuantity.setText(it)
            }

            binding.imageButtonDelete.setOnClickListener {
                itemClickListener?.invoke(beltItems[bindingAdapterPosition], bindingAdapterPosition)
            }
        }
    }

    fun setItemClickListener(itemClickListener: (item: BeltItem, index: Int) -> Unit) {
        this.itemClickListener = itemClickListener
    }

    fun addBlankItems(numberOfBlankItems: Int) {
        val currentLastIndex = beltItems.lastIndex
        repeat(numberOfBlankItems) {
            beltItems.add(BeltItem())
        }
        notifyItemRangeInserted(currentLastIndex, currentLastIndex + numberOfBlankItems)
    }

    fun itemRemoved(index: Int) {
        beltItems.removeAt(index)
        notifyItemRemoved(index)
    }
}
