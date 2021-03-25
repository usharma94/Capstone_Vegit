
package sheridan.sharmupm.vegit_capstone.ui.diet

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import sheridan.sharmupm.vegit_capstone.R
import sheridan.sharmupm.vegit_capstone.databinding.FragmentDietBinding
import sheridan.sharmupm.vegit_capstone.models.DietModel
import sheridan.sharmupm.vegit_capstone.ui.diet.DietViewModel
import sheridan.sharmupm.vegit_capstone.ui.home.HomeViewModel


//class DietFragment : Fragment() {
//
//    companion object {
//        fun newInstance() = DietFragment()
//    }
//
//    private lateinit var viewModel: DietViewModel
//
//    override fun onCreateView(
//            inflater: LayoutInflater, container: ViewGroup?,
//            savedInstanceState: Bundle?
//    ): View? {
//        return inflater.inflate(R.layout.diet_cardview, container, false)
//    }
//
//    override fun onActivityCreated(savedInstanceState: Bundle?) {
//        super.onActivityCreated(savedInstanceState)
//        viewModel = ViewModelProvider(this).get(DietViewModel::class.java)
//        // TODO: Use the ViewModel
//    }
//
//}
//
//class DietFragment : Fragment() {
//
//    private lateinit var dietViewModel: DietViewModel
//    private  lateinit var binding : FragmentDietBinding
//    override fun onCreateView(
//            inflater: LayoutInflater,
//            container: ViewGroup?,
//            savedInstanceState: Bundle?
//    ): View? {
//        dietViewModel =
//                ViewModelProvider(this).get(DietViewModel::class.java)
//        val root = inflater.inflate(R.layout.diet_cardview, container, false)
//        val textView: TextView = root.findViewById(R.id.text_diet)
//        val dietType: TextView = root.findViewById(R.id.vegetarianDiet)
//        dietViewModel.text.observe(viewLifecycleOwner, Observer {
//            textView.text = it
//        })
//        return root
//    }
//}


class DietFragment : Fragment() {

    private lateinit var binding: FragmentDietBinding
    private lateinit var dietList: List<DietModel>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        binding = FragmentDietBinding.inflate(inflater, container, false)
        val view: View = inflater.inflate(R.layout.diet_cardview, container, false)
        dietList = listOf(
            DietModel(false, null, "Vegetarian", "Vegetarian Diet"),
            DietModel(false, null, "Vegan", "Vegan Diet"),
            DietModel(false, null, "Custom", "Custom Diet")

        )
//        binding.vegetarianDiet.setOnClickListener {
//            val vegDiet = "veg diet was clicked"
//            Toast.makeText(context, vegDiet, Toast.LENGTH_LONG).show()
//            binding.vegetarianDiet.isSelected = true
//            binding.vegetarianDiet.setBackgroundColor(Color.GREEN)
//        }
//        return binding.root

        val recyclerView: RecyclerView = view.findViewById(R.id.diet_recycler_view)
        val adapter = DietAdapter(dietList, DietAdapter.OnClickListener { selectedDiet ->
            selectedDiet.isSelected = true
            selectedDiet.dietImage = R.drawable.vegetarian

//            view.findNavController().navigate(
//                            GalleryFragmentDirections.actionNavGalleryToMenuDetailFragment(colorName)
//
//            )
        })
        recyclerView.adapter = adapter

        return view
    }

}