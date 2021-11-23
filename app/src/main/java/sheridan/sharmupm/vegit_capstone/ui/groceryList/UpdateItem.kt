package sheridan.sharmupm.vegit_capstone.ui.groceryList

import android.app.DatePickerDialog
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.viewModels
import sheridan.sharmupm.vegit_capstone.R
import sheridan.sharmupm.vegit_capstone.controllers.groceryList.GroceryListViewModel
import sheridan.sharmupm.vegit_capstone.models.groceryList.Grocery
import java.util.*


class UpdateItem : Fragment(),MygroceryItemRecyclerViewAdapter.OnItemClickListener {
    private val viewModel: GroceryListViewModel by viewModels()

    lateinit var updateDueDate: TextView
    lateinit var mTaskEdit: EditText
    private lateinit var mUpdateBtn: Button
    private var dueDate = ""
    private var pos:Int = 0
    private lateinit var item:Grocery
    private lateinit var adapter:MygroceryItemRecyclerViewAdapter
    companion object{
        val position = "position"

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_update_item, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bundle = this.getArguments()
        updateDueDate = view.findViewById<TextView>(R.id.update_due_tv)
        mTaskEdit = view.findViewById<EditText>(R.id.update_edittext)
        mUpdateBtn = view.findViewById<Button>(R.id.update_btn)
        adapter = MygroceryItemRecyclerViewAdapter(this)

        if (bundle !=null){
            item = bundle.getParcelable<Grocery>("position")!!
            pos = bundle.getInt("Pos")
            //val grocery = viewModel.getGroceryItem(id)
//            viewModel.grocery.observe(viewLifecycleOwner,
//                {results->
//                    mTaskEdit.setText(results?.name)
//                    updateDueDate.setText(results?.due)
//
//            })
            mTaskEdit.setText(item?.name)
            updateDueDate.setText(item?.due)


            Toast.makeText(context,"id $id",Toast.LENGTH_LONG).show()

        }
        else{
            Toast.makeText(context,"none",Toast.LENGTH_LONG).show()
        }



        mTaskEdit.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                mUpdateBtn.setEnabled(false)

                mUpdateBtn.setBackgroundColor(Color.GRAY)
            }
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.toString() == "") {
                    mUpdateBtn.setEnabled(false)

                    mUpdateBtn.setBackgroundColor(Color.GRAY)
                } else {
                    mUpdateBtn.setEnabled(true)
                    mUpdateBtn.setBackgroundColor(resources.getColor(R.color.colorPrimary))

                }
            }

            override fun afterTextChanged(s: Editable) {}
        })

        updateDueDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            val MONTH = calendar[Calendar.MONTH]
            val YEAR = calendar[Calendar.YEAR]
            val DAY = calendar[Calendar.DATE]
            val datePickerDialog = DatePickerDialog(
                requireContext(),
                { view, year, month, dayOfMonth ->
                    var month = month
                    month = month + 1
                    updateDueDate.text = "$dayOfMonth/$month/$year"
                    dueDate = "$dayOfMonth/$month/$year"
                }, YEAR, MONTH, DAY
            )
            datePickerDialog.show()
        }


        mUpdateBtn.setOnClickListener {
            var grocery = mTaskEdit.getText().toString()
            if (grocery.isEmpty()){
                Toast.makeText(context, "Empty input not allowed", Toast.LENGTH_SHORT).show()
            }
            else{
                if (grocery!="" && dueDate!=""&& dueDate!=item.due){
                    viewModel.updateGroceryItem(item?.id!!,grocery,dueDate,item?.status!!)
                    adapter.notifyItemChanged(pos)
                    Toast.makeText(context,"successfully updated", Toast.LENGTH_LONG).show()
                    val groceryListFragment = GroceryListFragment()
                    fragmentManager?.beginTransaction()?.replace(R.id.nav_host_fragment,groceryListFragment)?.commit()


                }
                else if (dueDate==""){
                    viewModel.updateGroceryItem(item?.id!!,grocery,item?.due!!,item?.status!!)
                    adapter.notifyItemChanged(pos)
                    Toast.makeText(context,"successfully updated", Toast.LENGTH_LONG).show()
                    val groceryListFragment = GroceryListFragment()
                    fragmentManager?.beginTransaction()?.replace(R.id.nav_host_fragment,groceryListFragment)?.commit()
                }
                else{
                    Toast.makeText(context,"Please input grocery name and the date", Toast.LENGTH_LONG).show()
                }


            }

        }
    }

    override fun onItemClick(position: Int) {
        println("")
    }


}