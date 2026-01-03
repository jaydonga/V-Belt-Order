package com.nitintraders.v_beltorder.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nitintraders.v_beltorder.R
import com.nitintraders.v_beltorder.data.BeltOrder
import com.nitintraders.v_beltorder.databinding.ItemEachBeltOrderBinding

class EachBeltOrderAdapter : RecyclerView.Adapter<EachBeltOrderAdapter.BeltOrderViewHolder>() {

    private var beltOrders = mutableListOf<BeltOrder>()
    private var itemClickListener: ((ClickEventType, Int) -> Unit)? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BeltOrderViewHolder {
        val binding = ItemEachBeltOrderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BeltOrderViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: BeltOrderViewHolder,
        position: Int
    ) {
        holder.bind(beltOrders[position])
    }

    override fun getItemCount() = beltOrders.size

    fun setBeltOrders(beltOrders: List<BeltOrder>) {
        this.beltOrders.clear()
        this.beltOrders.addAll(beltOrders)
    }

    fun setItemClickListener(itemClickListener: (ClickEventType, Int) -> Unit) {
        this.itemClickListener = itemClickListener
    }

    inner class BeltOrderViewHolder(
        private val binding: ItemEachBeltOrderBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(beltOrder: BeltOrder) {
            binding.textViewCustomerName.text = beltOrder.customerName
            val totalBeltsString = binding.textViewTotalBelts.context.getString(
                R.string.total_belts,
                beltOrder.totalBelts
            )
            binding.textViewTotalBelts.text = totalBeltsString
            val totalAmountString = binding.textViewTotalAmount.context.getString(
                R.string.total_amount,
                beltOrder.grandTotal
            )
            binding.textViewTotalAmount.text = totalAmountString

            binding.imageViewDeleteOrder.setOnClickListener {
                itemClickListener?.invoke(ClickEventType.ItemRemoveEvent, bindingAdapterPosition)
            }

            binding.root.setOnClickListener {
                itemClickListener?.invoke(ClickEventType.ItemClickEvent, bindingAdapterPosition)
            }
        }

    }

    enum class ClickEventType {
        ItemRemoveEvent,
        ItemClickEvent,
    }
}