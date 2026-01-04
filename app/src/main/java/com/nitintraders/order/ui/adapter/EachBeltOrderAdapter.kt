package com.nitintraders.order.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nitintraders.order.R
import com.nitintraders.order.data.BeltOrder
import com.nitintraders.order.databinding.ItemEachBeltOrderBinding
import java.util.Date
import java.util.Locale

class EachBeltOrderAdapter : RecyclerView.Adapter<EachBeltOrderAdapter.BeltOrderViewHolder>() {

    private val simpleDateFormat = java.text.SimpleDateFormat("dd MMMM yyyy", Locale.UK)
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

            val dateString = simpleDateFormat.format(Date(beltOrder.orderDateTime))

            binding.textViewOrderDate.text = binding.textViewOrderDate.context.getString(
                R.string.order_date,
                dateString
            )

            binding.textViewBeltsOfSizeA.text = binding.textViewBeltsOfSizeA.context.getString(
                R.string.belts_of_size_a,
                beltOrder.totalBeltsOfTypeA
            )
            binding.textViewBeltsOfSizeB.text = binding.textViewBeltsOfSizeB.context.getString(
                R.string.belts_of_size_b,
                beltOrder.totalBeltsOfTypeB
            )
            binding.textViewBeltsOfSizeC.text = binding.textViewBeltsOfSizeC.context.getString(
                R.string.belts_of_size_c,
                beltOrder.totalBeltsOfTypeC
            )

            binding.textViewTotalBelts.text = binding.textViewTotalBelts.context.getString(
                R.string.total_belts,
                beltOrder.totalBelts
            )

            binding.textViewTotalAmount.text = binding.textViewTotalAmount.context.getString(
                R.string.total_amount,
                beltOrder.grandTotal
            )

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