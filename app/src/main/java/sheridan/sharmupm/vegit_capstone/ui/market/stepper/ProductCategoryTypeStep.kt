package sheridan.sharmupm.vegit_capstone.ui.market.stepper

import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import ernestoyaquello.com.verticalstepperform.Step
import sheridan.sharmupm.vegit_capstone.R

class ProductCategoryTypeStep(title: String) : Step<String>(title) {

    var productCategory: Spinner? = null
    private var selectedCategory: Int = 0

    override fun createStepContentLayout(): View {
        productCategory = Spinner(context)
        ArrayAdapter.createFromResource(
            context, R.array.category, android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            productCategory!!.adapter = adapter
        }

        productCategory!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>?, selectedItemView: View,
                position: Int, id: Long
            ) {
                selectedCategory = position
                markAsCompletedOrUncompleted(true)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // ignore
            }
        }

        return productCategory as Spinner
    }

    override fun isStepDataValid(stepData: String): IsDataValid {
        val isCategoryValid: Boolean = selectedCategory != 0
        val errorMessage: String = if (!isCategoryValid) "Must Select Category" else ""
        return IsDataValid(isCategoryValid, errorMessage)
    }

    override fun getStepData(): String {
        val category: String? = productCategory?.selectedItem.toString()
        if (category != null) return category
        return ""
    }

    override fun getStepDataAsHumanReadableString(): String {
        val categoryName: String = productCategory!!.selectedItem.toString()
        if (categoryName.isNotEmpty()) return categoryName
        return "(Empty)"
    }

    override fun onStepOpened(animated: Boolean) {
        // ignore
    }

    override fun onStepClosed(animated: Boolean) {
        // ignore
    }

    override fun onStepMarkedAsCompleted(animated: Boolean) {
        // ignore
    }

    override fun onStepMarkedAsUncompleted(animated: Boolean) {
        // ignore
    }

    override fun restoreStepData(data: String?) {
        // ignore
    }
}