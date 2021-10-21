

package sheridan.sharmupm.vegit_capstone.ui.user

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import sheridan.sharmupm.vegit_capstone.R

class DenyDialogFragment(
    var fragment: Fragment,
    context: Context
) : Dialog(context),
    View.OnClickListener {
    var dialog: Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.deny_dialog)
        val editReason = findViewById<EditText>(R.id.reason)
        val reason = findViewById<Button>(R.id.submit_reason)

        val afterTextChangedListener = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // ignore
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // ignore
            }

            override fun afterTextChanged(s: Editable) {
                reason.isEnabled = s.isNotEmpty()
            }
        }

        editReason.addTextChangedListener(afterTextChangedListener)
        reason.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        dismiss()
    }
}