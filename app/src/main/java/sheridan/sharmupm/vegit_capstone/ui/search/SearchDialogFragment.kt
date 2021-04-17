

package sheridan.sharmupm.vegit_capstone.ui.search

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import sheridan.sharmupm.vegit_capstone.R
import sheridan.sharmupm.vegit_capstone.helpers.DietSafety
import sheridan.sharmupm.vegit_capstone.helpers.determineSafety
import sheridan.sharmupm.vegit_capstone.models.DietModel
import sheridan.sharmupm.vegit_capstone.models.ingredients.Ingredient

class SearchDialogFragment(
    var fragment: Fragment,
    var ingredientDetail: Ingredient,
    val userDiet: DietModel,
    context: Context
) : Dialog(context),
    View.OnClickListener {
    var dialog: Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.search_dialog)
        val searchDialogTitle = findViewById<TextView>(R.id.search_dialog_title)
        val searchDialogIngredientName = findViewById<TextView>(R.id.search_diet_detail)
        val searchDialogDietType = findViewById<TextView>(R.id.search_diet_type)
        val searchDialogDietDescription = findViewById<TextView>(R.id.search_diet_description)

//        searchDialogTitle.setBackgroundColor(Color.argb(100, 0, 255, 0))
//        searchDialogTitle.setBackgroundColor(Color.parseColor("#B5EB6B"));
        searchDialogIngredientName.text = ingredientDetail.name
        searchDialogDietType.text = ingredientDetail.diet_name
        searchDialogDietDescription.text = ingredientDetail.description

        findViewById<Button>(R.id.search_dialog_done).setOnClickListener(this)

        when (determineSafety(userDiet, ingredientDetail.diet_type!!)) {
            DietSafety.SAFE -> {
                searchDialogTitle.setBackgroundColor(Color.parseColor("#ABEBC6"));
                searchDialogTitle.text = "SAFE"
            }
            DietSafety.CAUTION -> {
                searchDialogTitle.setBackgroundColor(Color.parseColor("#F9E79F"));
                searchDialogTitle.text = "CAUTION"
            }
            DietSafety.AVOID -> {
                searchDialogTitle.setBackgroundColor(Color.parseColor("#F1948A"));
                searchDialogTitle.text = "AVOID"
            }
            else -> {
                searchDialogTitle.setBackgroundColor(Color.parseColor("#F1948A"));
                searchDialogTitle.text = "UNKNOWN"
            }
        }
    }


    override fun onClick(v: View) {
        when (v.id) {
            R.id.search_dialog_done -> dismiss()
            R.id.search_dialog_done -> cancel()
            else -> {
            }
        }//Do Something
        dismiss()
    }
}