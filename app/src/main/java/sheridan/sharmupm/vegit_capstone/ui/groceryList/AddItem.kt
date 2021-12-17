package sheridan.sharmupm.vegit_capstone.ui.groceryList

import android.app.DatePickerDialog
import android.content.Context
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
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import sheridan.sharmupm.vegit_capstone.R
import sheridan.sharmupm.vegit_capstone.controllers.groceryList.GroceryListViewModel
import java.util.*

class AddItem: Fragment() {

    private val viewModel: GroceryListViewModel by viewModels()

    lateinit var setDueDate: TextView
    private lateinit var mTaskEdit: EditText
    private lateinit var mSaveBtn: Button
    private var dueDate = ""
    private lateinit var myContext: Context

    companion object {
        fun newInstance(): AddNewItem? {
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

        //add Text
        mTaskEdit.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                mSaveBtn.isEnabled = false

                mSaveBtn.setBackgroundColor(Color.GRAY)
            }
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.toString() == "") {
                    mSaveBtn.isEnabled = false

                    mSaveBtn.setBackgroundColor(Color.GRAY)
                } else {
                    mSaveBtn.isEnabled = true
                    mSaveBtn.setBackgroundColor(resources.getColor(R.color.colorPrimary))
                }
            }

            override fun afterTextChanged(s: Editable) {}
        })

        //set the date
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

        //Save the Item
        mSaveBtn.setOnClickListener {
            var grocery = mTaskEdit.text.toString()
            if (grocery.isEmpty()){
                Toast.makeText(context, "Empty input not allowed", Toast.LENGTH_SHORT).show()
            } else{
                if (grocery!="" && dueDate!=""){
                    viewModel.submitGrocery(grocery,dueDate)
                    Toast.makeText(context,"successfully added", Toast.LENGTH_LONG).show()
                    val groceryListFragment = GroceryListFragment()
                    fragmentManager?.beginTransaction()?.replace(R.id.nav_host_fragment,groceryListFragment)?.commit()
                } else{
                    Toast.makeText(context,"Please input grocery name and the date", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        myContext = context
    }
}