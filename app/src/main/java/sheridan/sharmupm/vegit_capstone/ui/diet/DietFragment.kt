
package sheridan.sharmupm.vegit_capstone.ui.diet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import sheridan.sharmupm.vegit_capstone.R
import sheridan.sharmupm.vegit_capstone.controllers.diet.DietViewModel
import sheridan.sharmupm.vegit_capstone.models.DietModel

class DietFragment : Fragment() {

    private lateinit var dietViewModel: DietViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.diet_cardview, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dietViewModel = ViewModelProvider(this).get(DietViewModel::class.java)
        val dietList = dietViewModel.populateDietList()

        val recyclerView: RecyclerView = view.findViewById(R.id.diet_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(context)

        recyclerView.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = DietAdapter(dietList) { diet: DietModel ->
                if (!diet.isDisabled) {
                    diet.isSelected = true
                    dietViewModel.setSelectedDiet(diet)

                    // un select the other diets
                    for (_diet in dietList) {
                        if (_diet.id !== diet.id) {
                            _diet.isSelected = false
                        }
                    }
                }
            }
        }

        val continueBtn = view.findViewById<Button>(R.id.btn_continue)
        continueBtn.setOnClickListener {
            this.findNavController().navigate(R.id.action_navigation_diet_to_navigation_classifyproducts)
        }
    }
}
