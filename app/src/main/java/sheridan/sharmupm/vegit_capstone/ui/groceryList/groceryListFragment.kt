package sheridan.sharmupm.vegit_capstone.ui.groceryList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import sheridan.sharmupm.vegit_capstone.R
import kotlin.concurrent.fixedRateTimer

/**
 * A fragment representing a list of Items.
 */
class groceryListFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var floatActionButton:FloatingActionButton



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
            AddNewItem.newInstance()?.show(requireFragmentManager(),AddNewItem.TAG)

        }




        return view
    }


}