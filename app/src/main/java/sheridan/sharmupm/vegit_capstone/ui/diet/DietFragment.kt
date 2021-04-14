
package sheridan.sharmupm.vegit_capstone.ui.diet

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import sheridan.sharmupm.vegit_capstone.R
import sheridan.sharmupm.vegit_capstone.databinding.DietCardviewBinding
import sheridan.sharmupm.vegit_capstone.helpers.DietTypes
import sheridan.sharmupm.vegit_capstone.models.DietModel


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

    private lateinit var binding: DietCardviewBinding
    private lateinit var dietList: List<DietModel>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DietCardviewBinding.inflate(inflater, container, false)

        val vegDes: String = getString(R.string.vegetarian_desc)
        val veganDes: String = getString(R.string.vegan_desc)
        val imgVegetarian =  R.drawable.vegetarian
        val imgVegan =  R.drawable.vegan
        val imgCustom =  R.drawable.custom

        dietList = listOf(
            DietModel(0, false, imgVegetarian, "Vegetarian", vegDes, DietTypes.VEGETARIAN.value),
            DietModel(1, false, imgVegan, "Vegan", veganDes, DietTypes.VEGAN.value),
            DietModel(2, false, imgCustom, "Custom", "Custom Diet", -1)

        )
        val recyclerView: RecyclerView = binding.dietRecyclerView

        val adapter = DietAdapter(dietList, DietAdapter.OnClickListener { selectedDiet ->
        })
        recyclerView.adapter = adapter

        val continueBtn = binding.btnContinue

        continueBtn.setOnClickListener {
            this.findNavController().navigate(R.id.action_navigation_diet_to_navigation_classifyproducts)
        }

        return binding.root
    }

    private fun saveRecyclerData(position: Int){
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)
        val editor = sharedPref?.edit()
        editor?.apply{
            putInt("REC_KEY", position)
        }
    }
}


//Testing out original method using view
//class DietFragment : Fragment() {
//
//    private lateinit var dietList: List<DietModel>
//
//    override fun onCreateView(
//            inflater: LayoutInflater, container: ViewGroup?,
//            savedInstanceState: Bundle?
//    ): View? {
//        // Inflate the layout for this fragment
//        val view: View = inflater.inflate(R.layout.diet_cardview, container, false)
//        val vegDes: String = getString(R.string.vegetarian_desc)
//        val veganDes: String = getString(R.string.vegan_desc)
//        val imgVegetarian =  R.drawable.vegetarian
//        val imgVegan =  R.drawable.vegan
//        val imgCustom =  R.drawable.custom
//        dietList = listOf(
//            DietModel(false, imgVegetarian, "Vegetarian", vegDes),
//            DietModel(false, imgVegan, "Vegan", veganDes),
//            DietModel(false, imgCustom, "Custom", "Custom Diet")
//
//        )
//
//        val recyclerView: RecyclerView = view.findViewById(R.id.diet_recycler_view)
//        val adapter = DietAdapter(dietList, DietAdapter.OnClickListener {
//            Toast.makeText(context?.applicationContext, "Display this", Toast.LENGTH_LONG).show()
//
//            this.findNavController().navigate(R.id.action_navigation_diet_to_navigation_classifyproducts)
//        })
//        recyclerView.adapter = adapter
//
//        return view
//    }
//
//}