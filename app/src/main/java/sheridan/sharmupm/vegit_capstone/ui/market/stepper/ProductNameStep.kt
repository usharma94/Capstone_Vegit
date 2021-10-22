package sheridan.sharmupm.vegit_capstone.ui.market.stepper

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import ernestoyaquello.com.verticalstepperform.Step

class ProductNameStep(title: String) : Step<String>(title) {

    var productName: EditText? = null

    override fun createStepContentLayout(): View {
        productName = EditText(context)
        productName!!.isSingleLine = true
        productName!!.hint = "Product Name"

        val afterTextChangedListener = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // ignore
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                markAsCompletedOrUncompleted(true)
            }

            override fun afterTextChanged(s: Editable?) {
                // ignore
            }
        }

        productName!!.addTextChangedListener(afterTextChangedListener)

        return productName as EditText
    }

    override fun isStepDataValid(stepData: String): IsDataValid {
        val isNameValid: Boolean = stepData.length >= 3
        val errorMessage: String = if (!isNameValid) "Invalid Product Name" else ""
        return IsDataValid(isNameValid, errorMessage)
    }

    override fun getStepData(): String {
        val name: Editable? = productName?.text
        if (name != null) return name.toString()
        return ""
    }

    override fun getStepDataAsHumanReadableString(): String {
        val name: String = stepData
        if (name.isNotEmpty()) return name
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