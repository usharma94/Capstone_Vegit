package sheridan.sharmupm.vegit_capstone.ui.market.stepper

import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import ernestoyaquello.com.verticalstepperform.Step
import sheridan.sharmupm.vegit_capstone.R

class ProductDietTypeStep(title: String) : Step<Int>(title) {

    var productDiet: Spinner? = null
    private var selectedDiet: Int = 0

    override fun createStepContentLayout(): View {
        productDiet = Spinner(context)
        ArrayAdapter.createFromResource(
            context, R.array.diet_types, android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            productDiet!!.adapter = adapter
        }

        productDiet!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>?, selectedItemView: View,
                position: Int, id: Long
            ) {
                selectedDiet = position
                markAsCompletedOrUncompleted(true)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // ignore
            }
        }

        return productDiet as Spinner
    }

    override fun isStepDataValid(stepData: Int): IsDataValid {
        val isDietValid: Boolean = stepData != 0
        println(stepData)
        val errorMessage: String = if (!isDietValid) "Must Select Diet" else ""
        return IsDataValid(isDietValid, errorMessage)
    }

    override fun getStepData(): Int {
        val diet: Int? = productDiet?.selectedItemPosition
        println(diet)
        if (diet != null) return diet
        return 0
    }

    override fun getStepDataAsHumanReadableString(): String {
        val dietName: String = productDiet!!.selectedItem.toString()
        println(dietName)
        if (dietName.isNotEmpty()) return dietName
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

    override fun restoreStepData(data: Int?) {
        // ignore
    }
}