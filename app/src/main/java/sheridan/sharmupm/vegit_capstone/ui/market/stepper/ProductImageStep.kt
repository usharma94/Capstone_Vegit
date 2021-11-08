package sheridan.sharmupm.vegit_capstone.ui.market.stepper

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.webkit.URLUtil
import android.widget.EditText
import ernestoyaquello.com.verticalstepperform.Step

class ProductImageStep(title: String) : Step<String>(title) {

    var productImage: EditText? = null

    override fun createStepContentLayout(): View {
        productImage = EditText(context)
        productImage!!.isSingleLine = true
        productImage!!.hint = "Image URL"

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

        productImage!!.addTextChangedListener(afterTextChangedListener)

        return productImage as EditText
    }

    override fun isStepDataValid(stepData: String): IsDataValid {
        val isURLValid: Boolean = URLUtil.isValidUrl(stepData)
        val errorMessage: String = if (!isURLValid) "Invalid Image URL" else ""
        return IsDataValid(isURLValid, errorMessage)
    }

    override fun getStepData(): String {
        val url: Editable? = productImage?.text
        if (url != null) return url.toString()
        return ""
    }

    override fun getStepDataAsHumanReadableString(): String {
        val url: String = stepData
        if (url.isNotEmpty()) return url
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