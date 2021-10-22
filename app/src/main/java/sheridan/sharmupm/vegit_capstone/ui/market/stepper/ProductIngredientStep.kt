package sheridan.sharmupm.vegit_capstone.ui.market.stepper

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.ArrayAdapter
import android.widget.MultiAutoCompleteTextView
import ernestoyaquello.com.verticalstepperform.Step


class ProductIngredientStep(title: String) : Step<String>(title) {

    private var ingredientsText: MultiAutoCompleteTextView? = null

    override fun createStepContentLayout(): View {
        ingredientsText = MultiAutoCompleteTextView(context)
        ingredientsText!!.hint = "Enter Ingredients separated by comma"

        val adapter: ArrayAdapter<String> = ArrayAdapter(context, android.R.layout.simple_dropdown_item_1line)
        ingredientsText!!.setAdapter(adapter)

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

        ingredientsText!!.addTextChangedListener(afterTextChangedListener)

        return ingredientsText as MultiAutoCompleteTextView
    }

    override fun isStepDataValid(stepData: String): IsDataValid {
        val isIngredientsValid: Boolean = stepData.length >= 3
        val errorMessage: String = if (!isIngredientsValid) "Invalid Ingredients" else ""
        return IsDataValid(isIngredientsValid, errorMessage)
    }

    override fun getStepData(): String {
        val ingredients: Editable? = ingredientsText?.text
        if (ingredients != null) return ingredients.toString()
        return ""
    }

    override fun getStepDataAsHumanReadableString(): String {
        val ingredients: String = stepData
        if (ingredients.isNotEmpty()) return ingredients
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