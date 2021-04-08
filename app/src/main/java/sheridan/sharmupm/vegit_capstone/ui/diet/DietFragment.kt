
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
import sheridan.sharmupm.vegit_capstone.models.DietModel



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
            DietModel(0, false, imgVegetarian, "Vegetarian", vegDes),
            DietModel(1, false, imgVegan, "Vegan", veganDes),
            DietModel(2, false, imgCustom, "Custom", "Custom Diet")

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

}

