

package sheridan.sharmupm.vegit_capstone.ui.search

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import sheridan.sharmupm.vegit_capstone.R

class SearchDialogFragment(
    var fragment: Fragment,
    var ingredientDetail: String,
    context: Context
) : Dialog(context),
    View.OnClickListener {
    var dialog: Dialog? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.search_dialog)
        val titleTxt = findViewById<TextView>(R.id.search_diet_detail)
        titleTxt.text = ingredientDetail
        findViewById<Button>(R.id.search_dialog_done).setOnClickListener(this)

    }


    override fun onClick(v: View) {
        when (v.id) {
            R.id.search_dialog_done -> dismiss()
            R.id.search_dialog_done -> cancel()
            else -> {
            }
        }//Do Something
//        dismiss()
    }
}