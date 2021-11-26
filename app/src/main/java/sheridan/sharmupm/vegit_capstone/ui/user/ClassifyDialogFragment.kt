

package sheridan.sharmupm.vegit_capstone.ui.user

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.*
import androidx.fragment.app.Fragment
import sheridan.sharmupm.vegit_capstone.R
import sheridan.sharmupm.vegit_capstone.models.ingredients.Ingredient

class ClassifyDialogFragment(
    var fragment: Fragment,
    var ingredient: Ingredient,
    context: Context
) : Dialog(context),
    View.OnClickListener {
    var dialog: Dialog? = null
    var selectedDiet: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.classify_dialog)
        val ingredientName = findViewById<TextView>(R.id.ingredient_name)
        val ingredientDietType = findViewById<TextView>(R.id.diet_type)
        val selectDiet = findViewById<Spinner>(R.id.select_diet_type)
        val doneBtn = findViewById<Button>(R.id.classify_dialog_done)

        ingredientName.text = ingredient.name
        ingredientDietType.text = ingredient.diet_name

        ArrayAdapter.createFromResource(
            context,
            R.array.diet_types,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            selectDiet.adapter = adapter
        }

        selectDiet.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>?, selectedItemView: View,
                position: Int, id: Long
            ) {
                doneBtn.isEnabled = position != 0
                selectedDiet = position
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // ignore
            }
        }

        doneBtn.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        dismiss()
    }
}