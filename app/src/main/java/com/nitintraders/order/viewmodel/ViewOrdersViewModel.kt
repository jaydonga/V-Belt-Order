package com.nitintraders.order.viewmodel

import android.app.Application
import android.graphics.Typeface
import android.print.PrintAttributes
import android.text.Layout
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nitintraders.order.R
import com.nitintraders.order.data.BeltItem
import com.nitintraders.order.data.BeltOrder
import com.nitintraders.order.repository.BeltOrdersRepository
import com.nitintraders.order.ui.adapter.EachBeltItemAdapter.BeltType.A
import com.nitintraders.order.ui.adapter.EachBeltItemAdapter.BeltType.B
import com.nitintraders.order.ui.adapter.EachBeltItemAdapter.BeltType.C
import com.nitintraders.order.utils.orZero
import com.wwdablu.soumya.simplypdf.SimplyPdf
import com.wwdablu.soumya.simplypdf.SimplyPdfDocument
import com.wwdablu.soumya.simplypdf.composers.properties.TableProperties
import com.wwdablu.soumya.simplypdf.composers.properties.TextProperties
import com.wwdablu.soumya.simplypdf.composers.properties.cell.Cell
import com.wwdablu.soumya.simplypdf.composers.properties.cell.TextCell
import com.wwdablu.soumya.simplypdf.document.DocumentInfo
import com.wwdablu.soumya.simplypdf.document.Margin
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import java.io.File
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class ViewOrdersViewModel @Inject constructor(
    private val application: Application,
    private val beltOrdersRepository: BeltOrdersRepository
) : ViewModel() {

    private val _allBeltOrders = MutableStateFlow<List<BeltOrder>>(emptyList())
    val allBeltOrders: Flow<List<BeltOrder>> = _allBeltOrders

    private val _orderDeleted = MutableStateFlow<BeltOrder?>(null)
    val orderDeleted: Flow<BeltOrder> = _orderDeleted.filterNotNull()

    private val _pdfCreationIsComplete = MutableStateFlow<File?>(null)
    val pdfCreationIsComplete: Flow<File?> = _pdfCreationIsComplete

    fun retrieveAllBeltOrders() {
        viewModelScope.launch {
            val listBeltOrders = beltOrdersRepository.getAllOrders()
            _allBeltOrders.value = listBeltOrders
        }
    }

    fun deleteOrder(beltOrder: BeltOrder) {
        viewModelScope.launch {
            val deletionResult = beltOrdersRepository.deleteOrder(beltOrder)
            if (deletionResult) {
                _orderDeleted.value = beltOrder
            }
        }
    }

    fun createOrderInPdf(beltOrder: BeltOrder) {
        viewModelScope.launch {
            val file = File(
                application.applicationContext.cacheDir,
                "${beltOrder.customerName}_${simpleDateFormat.format(beltOrder.orderDateTime)}" + ".pdf",
            )

            val pageMargin = Margin(
                DEFAULT_PAGE_MARGIN,
                DEFAULT_PAGE_MARGIN,
                DEFAULT_PAGE_MARGIN,
                DEFAULT_PAGE_MARGIN,
            )
            val simplyPdfDocument = SimplyPdf.with(application.applicationContext, file)
                .colorMode(DocumentInfo.ColorMode.MONO)
                .paperSize(PrintAttributes.MediaSize.ISO_A5)
                .margin(pageMargin)
                .build()

            simplyPdfDocument.insertEmptyLines(2)

            writeDocumentHeader(simplyPdfDocument)

            writeCustomerName(simplyPdfDocument, beltOrder)

            writeBeltsOfTypeA(beltOrder, simplyPdfDocument)

            writeBeltsOfTypeB(beltOrder, simplyPdfDocument)

            writeBeltsOfTypeC(beltOrder, simplyPdfDocument)

            writeDocumentFooter(simplyPdfDocument, beltOrder)

            simplyPdfDocument.finish()
            _pdfCreationIsComplete.value = file
            _pdfCreationIsComplete.value = null
        }
    }

    private fun writeDocumentHeader(simplyPdfDocument: SimplyPdfDocument) {
        simplyPdfDocument.text.write(
            application.getString(R.string.nitin_traders),
            TextProperties().apply {
                textSize = LARGE_TEXT
                alignment = Layout.Alignment.ALIGN_CENTER
                typeface = Typeface.DEFAULT_BOLD
            },
        )
        simplyPdfDocument.insertEmptyLines(1)
    }

    private fun writeCustomerName(
        simplyPdfDocument: SimplyPdfDocument,
        beltOrder: BeltOrder
    ) {
        simplyPdfDocument.text.write(
            "${beltOrder.customerName}, ${simpleDateFormat.format(beltOrder.orderDateTime)}",
            TextProperties().apply {
                textSize = MEDIUM_TEXT
                alignment = Layout.Alignment.ALIGN_CENTER
                typeface = Typeface.DEFAULT_BOLD
            },
        )
        simplyPdfDocument.insertEmptyLines(2)
    }

    private fun writeBeltsOfTypeA(
        beltOrder: BeltOrder,
        simplyPdfDocument: SimplyPdfDocument
    ) {
        val beltOrdersA = beltOrder.beltItems.filter {
            it.beltType == A
        }.sortedBy { it.size }
        val totalBeltsOfTypeA = beltOrdersA.sumOf { it.quantity.orZero() }
        val totalAmountForTypeABelts = beltOrdersA.sumOf { it.totalInches } * beltOrder.priceOfBeltTypeA

        writeBeltItems(
            simplyPdfDocument,
            application.getText(R.string.label_v_belt_type_a).toString(),
            beltOrdersA,
            totalBeltsOfTypeA,
            totalAmountForTypeABelts,
        )
        simplyPdfDocument.insertEmptyLines(2)
    }

    private fun writeBeltsOfTypeB(
        beltOrder: BeltOrder,
        simplyPdfDocument: SimplyPdfDocument
    ) {
        val beltOrdersB = beltOrder.beltItems.filter {
            it.beltType == B
        }.sortedBy { it.size }
        val totalBeltsOfTypeB = beltOrdersB.sumOf { it.quantity.orZero() }
        val totalAmountForTypeBBelts = beltOrdersB.sumOf { it.totalInches } * beltOrder.priceOfBeltTypeB

        writeBeltItems(
            simplyPdfDocument,
            application.getText(R.string.label_v_belt_type_b).toString(),
            beltOrdersB,
            totalBeltsOfTypeB,
            totalAmountForTypeBBelts,
        )
        simplyPdfDocument.insertEmptyLines(2)
    }

    private fun writeBeltsOfTypeC(
        beltOrder: BeltOrder,
        simplyPdfDocument: SimplyPdfDocument
    ) {
        val beltOrdersC = beltOrder.beltItems.filter {
            it.beltType == C
        }.sortedBy { it.size }
        val totalBeltsOfTypeC = beltOrdersC.sumOf { it.quantity.orZero() }
        val totalAmountForTypeCBelts = beltOrdersC.sumOf { it.totalInches } * beltOrder.priceOfBeltTypeC

        writeBeltItems(
            simplyPdfDocument,
            application.getText(R.string.label_v_belt_type_c).toString(),
            beltOrdersC,
            totalBeltsOfTypeC,
            totalAmountForTypeCBelts,
        )
    }

    private fun writeDocumentFooter(
        simplyPdfDocument: SimplyPdfDocument,
        beltOrder: BeltOrder
    ) {
        simplyPdfDocument.text.write(
            application.getString(
                R.string.total_belts_and_grand_total_english,
                beltOrder.totalBelts,
                beltOrder.grandTotal
            ),
            TextProperties().apply {
                textSize = LARGE_TEXT
                alignment = Layout.Alignment.ALIGN_CENTER
                typeface = Typeface.DEFAULT_BOLD
            },
        )
    }

    private fun writeBeltItems(
        simplyPdfDocument: SimplyPdfDocument,
        cellHeader: String,
        beltItems: List<BeltItem>,
        totalBeltsOfThisType: Int,
        totalAmountPerBeltType: Float,
    ) {
        if (beltItems.isEmpty()) {
            return
        }

        val boldTextProperties = TextProperties().apply {
            textSize = SMALL_TEXT
            alignment = Layout.Alignment.ALIGN_CENTER
            typeface = Typeface.DEFAULT_BOLD
        }

        val defaultTextProperties = TextProperties().apply {
            textSize = SMALL_TEXT
            alignment = Layout.Alignment.ALIGN_CENTER
        }

        val tableProperties = TableProperties().apply {
            borderWidth = 1
            align = TableProperties.ALIGN_CENTER
        }

        val rows = mutableListOf<List<Cell>>()
        val fullCellWidth = simplyPdfDocument.usablePageWidth - 2 * DEFAULT_PAGE_MARGIN.toInt()
        rows.add(
            listOf(
                TextCell(cellHeader, boldTextProperties, fullCellWidth)
            )
        )
        rows.add(
            listOf(
                TextCell(
                    application.getString(R.string.size_in_inch_english),
                    defaultTextProperties,
                    fullCellWidth / 2
                ),
                TextCell(
                    application.getString(R.string.quantity_english),
                    defaultTextProperties,
                    fullCellWidth / 2
                )
            )
        )
        beltItems.forEach {
            val rowCells = listOf(
                TextCell(it.size.toString(), defaultTextProperties, fullCellWidth / 2),
                TextCell(it.quantity.toString(), defaultTextProperties, fullCellWidth / 2)
            )
            rows.add(rowCells)
        }
        val beltsAndAmount = application.getString(
            R.string.belts_amount_english,
            totalBeltsOfThisType,
            totalAmountPerBeltType,
        )
        rows.add(listOf(TextCell(beltsAndAmount, defaultTextProperties, fullCellWidth)))

        simplyPdfDocument.table.draw(rows, tableProperties)
    }

    private companion object {
        private const val DEFAULT_PAGE_MARGIN = 10U
        private const val LARGE_TEXT = 18
        private const val MEDIUM_TEXT = 16
        private const val SMALL_TEXT = 14
        private val simpleDateFormat = java.text.SimpleDateFormat("dd MMMM yyyy", Locale.UK)
    }
}
