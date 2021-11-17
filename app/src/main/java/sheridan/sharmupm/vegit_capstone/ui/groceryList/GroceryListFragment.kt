package sheridan.sharmupm.vegit_capstone.ui.groceryList

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import sheridan.sharmupm.vegit_capstone.R
import sheridan.sharmupm.vegit_capstone.models.groceryList.Grocery
import java.util.*

/**
 * A fragment representing a list of Items.
 */
class GroceryListFragment : Fragment(), onDialogCloseListener{
    private lateinit var recyclerView: RecyclerView
    private lateinit var floatActionButton:FloatingActionButton
    private lateinit var adapter:MygroceryItemRecyclerViewAdapter
    private lateinit var mList: MutableList<Grocery>




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_grocery_list, container, false)

        recyclerView = view.findViewById(R.id.recycerlview)
        floatActionButton = view.findViewById(R.id.floatingActionButton)
        recyclerView.setHasFixedSize(true)
        recyclerView.setLayoutManager(LinearLayoutManager(context))
        floatActionButton.setOnClickListener {
            AddNewItem.newInstance()?.show(requireFragmentManager(), AddNewItem.TAG)

        }
        val grocery1:Grocery = Grocery("apple","21/10/2021",0)
        val grocery2:Grocery = Grocery("cake","2021-11-20",1)
        mList = mutableListOf()
        mList.add(grocery1)
        //Toast.makeText(context,mList[0].grocery,Toast.LENGTH_LONG).show()
        showData()
        adapter = MygroceryItemRecyclerViewAdapter(requireContext(), mList)

        Toast.makeText(context, "something", Toast.LENGTH_LONG).show()

        recyclerView.setAdapter(adapter)
        return view
    }

    private fun showData(){
        val bundle: Bundle? = getArguments()
        if (bundle!=null){
            var grocery = bundle?.getParcelable<Grocery>("grocery")
            mList.add(grocery!!)
            adapter.notifyDataSetChanged()
            Toast.makeText(context, "something", Toast.LENGTH_LONG).show()

        }else{
            Toast.makeText(context, "nothing", Toast.LENGTH_LONG).show()
        }
    }


    override fun onDialogClose(dialogInterface: DialogInterface?) {

        showData()

        adapter.notifyDataSetChanged()
    }




}