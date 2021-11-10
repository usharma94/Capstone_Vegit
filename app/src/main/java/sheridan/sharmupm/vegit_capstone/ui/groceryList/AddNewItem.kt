package sheridan.sharmupm.vegit_capstone.ui.groceryList

import android.app.DatePickerDialog
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import sheridan.sharmupm.vegit_capstone.R
import java.util.*

class AddNewItem: BottomSheetDialogFragment() {


    private lateinit var setDueDate: TextView
    private lateinit var mTaskEdit: EditText
    private lateinit var mSaveBtn: Button
    private var dueDate = ""

    companion object{
        public val TAG = "AddNewTask"
        public fun newInstance(): AddNewItem? {
            return AddNewItem()
        }

    }



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.add_new_item, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setDueDate = view.findViewById(R.id.set_due_tv)
        mTaskEdit = view.findViewById(R.id.task_edittext)
        mSaveBtn = view.findViewById(R.id.save_btn)

        mTaskEdit.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.toString() == "") {
                    mSaveBtn.setEnabled(false)
                    mSaveBtn.setBackgroundColor(Color.GRAY)
                } else {
                    mSaveBtn.setEnabled(true)
                    mSaveBtn.setBackgroundColor(resources.getColor(R.color.green_blue))
                }
            }

            override fun afterTextChanged(s: Editable) {}
        })


        setDueDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            val MONTH = calendar[Calendar.MONTH]
            val YEAR = calendar[Calendar.YEAR]
            val DAY = calendar[Calendar.DATE]
            val datePickerDialog = DatePickerDialog(
                requireContext(),
                { view, year, month, dayOfMonth ->
                    var month = month
                    month = month + 1
                    setDueDate.text = "$dayOfMonth/$month/$year"
                    dueDate = "$dayOfMonth/$month/$year"
                }, YEAR, MONTH, DAY
            )
            datePickerDialog.show()
        }
    }



}