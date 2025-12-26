package com.nitintraders.v_beltorder.ui.adapter

import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import com.nitintraders.v_beltorder.R
import com.nitintraders.v_beltorder.data.BeltItem
import com.nitintraders.v_beltorder.databinding.ItemBeltOrderBinding
import com.nitintraders.v_beltorder.utils.orZero

class BeltItemAdapter() : RecyclerView.Adapter<BeltItemAdapter.BeltItemViewHolder>() {

    private var itemClickListener: ((item: BeltItem, index: Int) -> Unit)? = null
    private var itemUpdateListener: (() -> Unit)? = null

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

        private var sizeWatcher: TextWatcher? = null
        private var quantityWatcher: TextWatcher? = null

        fun bind(beltItem: BeltItem) {

            sizeWatcher?.let { watcher ->
                binding.editTextSizeInInch.removeTextChangedListener(
                    watcher
                )
            }
            quantityWatcher?.let { watcher ->
                binding.editTextQuantity.removeTextChangedListener(
                    watcher
                )
            }

            binding.editTextSizeInInch.setText((beltItem.size ?: "").toString())
            binding.editTextQuantity.setText((beltItem.quantity ?: "").toString())
            val totalInchesText =
                binding.root.context.getString(R.string.total_inches, beltItem.totalInches)
            binding.textViewTotalInchesRow.text = totalInchesText

            binding.imageButtonDelete.setOnClickListener {
                itemClickListener?.invoke(beltItems[bindingAdapterPosition], bindingAdapterPosition)
            }

            sizeWatcher = binding.editTextSizeInInch.addTextChangedListener { text ->
                beltItem.size = text?.toString()?.toIntOrNull()
                beltItem.totalInches = beltItem.size.orZero() * beltItem.quantity.orZero()
                binding.textViewTotalInchesRow.text = binding.root.context.getString(
                    R.string.total_inches,
                    beltItem.totalInches
                )
                itemUpdateListener?.invoke()
            }

            quantityWatcher = binding.editTextQuantity.addTextChangedListener { text ->
                beltItem.quantity = text?.toString()?.toIntOrNull()
                beltItem.totalInches = beltItem.size.orZero() * beltItem.quantity.orZero()
                binding.textViewTotalInchesRow.text = binding.root.context.getString(
                    R.string.total_inches,
                    beltItem.totalInches
                )
                itemUpdateListener?.invoke()
            }
        }
    }

    fun setItemClickListener(itemClickListener: (item: BeltItem, index: Int) -> Unit) {
        this.itemClickListener = itemClickListener
    }

    fun setItemUpdateListener(itemUpdateListener: () -> Unit) {
        this.itemUpdateListener = itemUpdateListener
    }


    fun addNewItems(numberOfNewItems: Int) {
        val currentLastIndex = beltItems.lastIndex
        repeat(numberOfNewItems) {
            beltItems.add(BeltItem())
        }
        notifyItemRangeInserted(currentLastIndex + 1, numberOfNewItems)
    }

    fun itemRemoved(index: Int) {
        beltItems.removeAt(index)
        notifyItemRemoved(index)
        itemUpdateListener?.invoke()
    }

    val allBeltItems: List<BeltItem>
        get() = beltItems
}
