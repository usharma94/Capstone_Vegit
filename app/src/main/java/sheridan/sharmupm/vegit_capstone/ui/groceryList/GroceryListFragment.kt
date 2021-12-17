package sheridan.sharmupm.vegit_capstone.ui.groceryList

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_grocery_list.view.*
import sheridan.sharmupm.vegit_capstone.R
import sheridan.sharmupm.vegit_capstone.controllers.groceryList.GroceryListViewModel
import sheridan.sharmupm.vegit_capstone.models.groceryList.Grocery

/**
 * A fragment representing a list of Items.
 */
class GroceryListFragment : Fragment(), onDialogCloseListener, MygroceryItemRecyclerViewAdapter.OnItemClickListener {

    private val viewModel:GroceryListViewModel by viewModels()
    private lateinit var recyclerView: RecyclerView
    private lateinit var floatActionButton:FloatingActionButton
    private lateinit var adapter:MygroceryItemRecyclerViewAdapter
    private lateinit var mList: List<Grocery>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_grocery_list, container, false)
        viewModel.getAllGroceryItems()

        recyclerView = view.findViewById(R.id.recycerlview)
        floatActionButton = view.findViewById(R.id.floatingActionButton)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(context)

        recyclerView.addItemDecoration(
            DividerItemDecoration(
                context,
                LinearLayoutManager.VERTICAL
            )
        )

        //navigate to add item fragment when click + button
        floatActionButton.setOnClickListener {
            val addItem = AddItem()
            fragmentManager?.beginTransaction()?.replace(R.id.nav_host_fragment,addItem)?.commit()

        }
        adapter = MygroceryItemRecyclerViewAdapter(this)
        adapter.notifyDataSetChanged()
        recyclerView.adapter = adapter

        //retrive data
        viewModel.groceryList.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            if (it!=null ){
                for (item in it){
//                    if (item.name?.contains("\"")==true){
//                        item.name?.replace("\""," ")
//                    }
                    if (item.name?.contains("\"") == true){
                        var name = item.name!!.replace("\"","")
                        item.name = name
                    }
                    if (item.name?.startsWith("[")==true){
                        item.name = item.name!!.substring(1, item.name!!.length-1)
                    }
                }

                adapter.setList(it)
            }
             })


        //delete touch helper
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT or ItemTouchHelper
            .RIGHT){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val item = adapter.groceryList[position]
                //viewModel.deleteGrocery(item.id!!)
                adapter.deleteItem(position)

                Snackbar.make(view,"Deleted",Snackbar.LENGTH_LONG).apply { setAction("Undo"){
                    viewModel.submitGrocery(item.name!!,item.due!!)
                }
                    show() }
            }
        }).attachToRecyclerView(view.recycerlview)
        return view
    }

    override fun onItemClick(position: Int) {

        val item = adapter.groceryList[position]
        val updateItem = UpdateItem()
        val bundle = Bundle()
        bundle.putParcelable("position",item)
        bundle.putInt("Pos",position)
        updateItem.arguments = bundle

        fragmentManager?.beginTransaction()?.replace(R.id.nav_host_fragment,updateItem)?.commit()
    }

    override fun onDialogClose(dialogInterface: DialogInterface?) {
        viewModel.getAllGroceryItems()
        //showData()
        adapter.clearList()
        adapter.notifyDataSetChanged()
        recyclerView.adapter = adapter

        viewModel.groceryList.observe(viewLifecycleOwner, androidx.lifecycle.Observer { adapter.setList(it) })
    }
}