package com.nitintraders.order.ui.adapter

import android.text.Html
import android.text.Html.FROM_HTML_MODE_COMPACT
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
    private var itemClickListener: ((ClickEventType, BeltOrder) -> Unit)? = null

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
        notifyItemRangeChanged(0, beltOrders.size)
    }

    fun setItemClickListener(itemClickListener: (ClickEventType, BeltOrder) -> Unit) {
        this.itemClickListener = itemClickListener
    }

    fun adjustListAfterOrderDeletion(deletedBeltOrder: BeltOrder) {
        val deletedIndex = beltOrders.indexOf(deletedBeltOrder)
        beltOrders.remove(deletedBeltOrder)
        notifyItemRemoved(deletedIndex)
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

            val totalBeltsOfSizeA = binding.textViewBeltsOfSizeA.context.getString(
                R.string.belts_of_size_a,
                beltOrder.totalBeltsOfTypeA
            )
            binding.textViewBeltsOfSizeA.text = Html.fromHtml(totalBeltsOfSizeA, FROM_HTML_MODE_COMPACT)

            val totalBeltsOfSizeB = binding.textViewBeltsOfSizeB.context.getString(
                R.string.belts_of_size_b,
                beltOrder.totalBeltsOfTypeB
            )
            binding.textViewBeltsOfSizeB.text = Html.fromHtml(totalBeltsOfSizeB, FROM_HTML_MODE_COMPACT)

            val totalBeltsOfSizeC = binding.textViewBeltsOfSizeC.context.getString(
                R.string.belts_of_size_c,
                beltOrder.totalBeltsOfTypeC
            )
            binding.textViewBeltsOfSizeC.text = Html.fromHtml(totalBeltsOfSizeC, FROM_HTML_MODE_COMPACT)

            val totalBelts = binding.textViewTotalBelts.context.getString(
                R.string.total_belts,
                beltOrder.totalBelts
            )
            binding.textViewTotalBelts.text = Html.fromHtml(totalBelts, FROM_HTML_MODE_COMPACT)

            binding.textViewTotalAmount.text = binding.textViewTotalAmount.context.getString(
                R.string.total_amount,
                beltOrder.grandTotal
            )

            binding.imageViewDeleteOrder.setOnClickListener {
                itemClickListener?.invoke(
                    ClickEventType.ItemRemoveEvent,
                    beltOrders[bindingAdapterPosition],
                )
            }

            binding.buttonSendOrder.setOnClickListener {
                itemClickListener?.invoke(
                    ClickEventType.ItemShareEvent,
                    beltOrders[bindingAdapterPosition],
                )
            }

            binding.root.setOnClickListener {
                itemClickListener?.invoke(
                    ClickEventType.ItemClickEvent,
                    beltOrders[bindingAdapterPosition],
                )
            }
        }
    }

    enum class ClickEventType {
        ItemRemoveEvent,
        ItemClickEvent,
        ItemShareEvent,
    }
}
