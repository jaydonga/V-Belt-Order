package com.nitintraders.order.ui.fragments

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

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
                    createOrderInPdf(beltOrder)
                }
            }
        }

        binding.recyclerViewAllOrder.adapter = beltOrdersAdapter
        binding.recyclerViewAllOrder.layoutManager = LinearLayoutManager(requireContext())

        lifecycleScope.launch {
            viewModel.allBeltOrders.flowWithLifecycle(lifecycle, Lifecycle.State.CREATED).collect {
                beltOrdersAdapter.setBeltOrders(it)
            }
        }

        lifecycleScope.launch {
            viewModel.orderDeleted.flowWithLifecycle(lifecycle, Lifecycle.State.CREATED).collect {
                beltOrdersAdapter.adjustListAfterOrderDeletion(it)
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

    private fun createOrderInPdf(beltOrder: BeltOrder) {
        val pdfDocument = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(ORDER_PDF_WIDTH, ORDER_PDF_HEIGHT, 1).create()
        val page = pdfDocument.startPage(pageInfo)
        val canvas: Canvas = page.canvas

        val paint = Paint()
        paint.color = Color.BLACK
        paint.textSize = 36f

        canvas.drawText("ABC", 80f, 100f, paint)

        pdfDocument.finishPage(page)

        val file = File(requireContext().cacheDir, "${beltOrder.customerName}_order.pdf")
        try {
            pdfDocument.writeTo(FileOutputStream(file))
            sharePdfFile(file)
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(requireContext(), "Failed to create PDF", Toast.LENGTH_SHORT).show()
        } finally {
            pdfDocument.close()
        }
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
        startActivity(Intent.createChooser(intent, "Share Order PDF"))
    }

    private companion object {
        private const val ORDER_PDF_WIDTH = 595
        private const val ORDER_PDF_HEIGHT = 842
    }
}