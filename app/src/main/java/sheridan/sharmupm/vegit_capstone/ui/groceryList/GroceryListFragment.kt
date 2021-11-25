package sheridan.sharmupm.vegit_capstone.ui.groceryList

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

        floatActionButton.setOnClickListener {
            //AddNewItem.newInstance()?.show(requireFragmentManager(), AddNewItem.TAG)
            val addItem = AddItem()
            fragmentManager?.beginTransaction()?.replace(R.id.nav_host_fragment,addItem)?.commit()

        }

       // mList = viewModel.getAllGroceryItems()
//        Toast.makeText(context,mList[0].name,Toast.LENGTH_LONG).show()
        adapter = MygroceryItemRecyclerViewAdapter(this)
        adapter.notifyDataSetChanged()
        recyclerView.adapter = adapter

        viewModel.groceryList.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            if (it!=null ){
                adapter.setList(it)
            }
             })
        viewModel.getAllGroceryItems()

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

//        viewModel.groceryList.observe(viewLifecycleOwner,
//            { results ->
//                if (results != null) {
//                    println(results)
//                    val adapter = MygroceryItemRecyclerViewAdapter(requireContext(),results)
//                    adapter.groceryList=results.toMutableList()
//
//                    //recyclerView.adapter = adapter
//                }
//                else {
//                    println("No data found")
//                }
//
//
//           // mList = viewModel.getAllGroceryItems()
//
//        })
        //recyclerView.setAdapter(adapter)

        //Toast.makeText(context,mList[0].grocery,Toast.LENGTH_LONG).show()
       // showData()
       // adapter = MygroceryItemRecyclerViewAdapter(requireContext(), mList)

       // Toast.makeText(context, "something", Toast.LENGTH_LONG).show()

       // recyclerView.setAdapter(adapter)
        return view
    }

    override fun onItemClick(position: Int) {

        val item = adapter.groceryList[position]
        val updateItem = UpdateItem()
        val bundle = Bundle()
        //bundle.putInt("position",item.id!!)
        bundle.putParcelable("position",item)
        bundle.putInt("Pos",position)
        updateItem.arguments = bundle

        fragmentManager?.beginTransaction()?.replace(R.id.nav_host_fragment,updateItem)?.commit()
    }

    override fun onDialogClose(dialogInterface: DialogInterface?) {

        //showData()
        adapter.clearList()
        //adapter = MygroceryItemRecyclerViewAdapter()
        adapter.notifyDataSetChanged()
        recyclerView.adapter = adapter

        viewModel.groceryList.observe(viewLifecycleOwner, androidx.lifecycle.Observer { adapter.setList(it) })
        viewModel.getAllGroceryItems()
    }
}