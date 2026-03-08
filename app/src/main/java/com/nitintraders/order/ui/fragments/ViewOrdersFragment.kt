package com.nitintraders.order.ui.fragments

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.print.PrintAttributes
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.nitintraders.order.R
import com.nitintraders.order.data.BeltOrder
import com.nitintraders.order.databinding.FragmentViewOrdersBinding
import com.nitintraders.order.ui.activities.MainActivity
import com.nitintraders.order.ui.adapter.EachBeltOrderAdapter
import com.nitintraders.order.viewmodel.ViewOrdersViewModel
import com.wwdablu.soumya.simplypdf.SimplyPdf
import com.wwdablu.soumya.simplypdf.composers.properties.TableProperties
import com.wwdablu.soumya.simplypdf.composers.properties.TextProperties
import com.wwdablu.soumya.simplypdf.composers.properties.cell.Cell
import com.wwdablu.soumya.simplypdf.composers.properties.cell.TextCell
import com.wwdablu.soumya.simplypdf.document.DocumentInfo
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import java.io.File

@AndroidEntryPoint
class ViewOrdersFragment : Fragment() {

    private lateinit var binding: FragmentViewOrdersBinding
    private val viewModel: ViewOrdersViewModel by viewModels()
    private lateinit var beltOrdersAdapter: EachBeltOrderAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        beltOrdersAdapter = EachBeltOrderAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentViewOrdersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        beltOrdersAdapter.setItemClickListener { clickEventType, beltOrder ->
            when (clickEventType) {
                EachBeltOrderAdapter.ClickEventType.ItemRemoveEvent -> {
                    confirmBeforeDelete(beltOrder)
                }

                EachBeltOrderAdapter.ClickEventType.ItemClickEvent -> {
                    (activity as MainActivity).navigateToCreateNewOrder(beltOrder)
                }

                EachBeltOrderAdapter.ClickEventType.ItemShareEvent -> {
                    viewModel.createOrderInPdf(beltOrder)
                }
            }
        }

        binding.recyclerViewAllOrder.adapter = beltOrdersAdapter
        binding.recyclerViewAllOrder.layoutManager = LinearLayoutManager(requireContext())

        lifecycleScope.launch {
            viewModel.allBeltOrders.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED).collect {
                beltOrdersAdapter.setBeltOrders(it)
            }
        }

        lifecycleScope.launch {
            viewModel.orderDeleted.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED).collect {
                beltOrdersAdapter.adjustListAfterOrderDeletion(it)
            }
        }

        lifecycleScope.launch {
            viewModel.pdfCreationIsComplete
                .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .filterNotNull()
                .collect {
                    sharePdfFile(it)
                }
        }

        viewModel.retrieveAllBeltOrders()
    }

    private fun confirmBeforeDelete(beltOrder: BeltOrder) {
        val builder = AlertDialog.Builder(context)
        builder
            .setTitle(getString(R.string.delete_the_order_from_x, beltOrder.customerName))
            .setPositiveButton(getString(R.string.ok)) { dialog, _ ->
                viewModel.deleteOrder(beltOrder)
                dialog.dismiss()
            }
            .setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
                dialog.dismiss()
            }

        val dialog = builder.create()
        dialog.show()
    }

    private fun sharePdfFile(file: File) {
        val uri = FileProvider.getUriForFile(
            requireContext(),
            "${requireContext().packageName}.fileprovider",
            file
        )

        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "application/pdf"
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        startActivity(Intent.createChooser(intent, getString(R.string.send_order)))
    }

    private companion object {
        private const val ORDER_PDF_WIDTH = 595
        private const val ORDER_PDF_HEIGHT = 842
    }
}