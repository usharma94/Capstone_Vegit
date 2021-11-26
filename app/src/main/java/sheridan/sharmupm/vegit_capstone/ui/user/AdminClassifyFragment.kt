package sheridan.sharmupm.vegit_capstone.ui.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import sheridan.sharmupm.vegit_capstone.R
import sheridan.sharmupm.vegit_capstone.controllers.user.AdminClassifyViewModel
import sheridan.sharmupm.vegit_capstone.models.ingredients.Ingredient

class AdminClassifyFragment : Fragment() {

    private lateinit var adminClassifyViewModel: AdminClassifyViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.admin_classify_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        adminClassifyViewModel = ViewModelProvider(this).get(AdminClassifyViewModel::class.java)
        adminClassifyViewModel.getIngredientsToClassify()

        val recyclerView: RecyclerView = view.findViewById(R.id.classifyIngredients)

        recyclerView.addItemDecoration(
            DividerItemDecoration(
                context,
                LinearLayoutManager.VERTICAL
            )
        )

        adminClassifyViewModel.ingredientList.observe(viewLifecycleOwner,
            { results ->
                if (results != null) {
                    // display results of search in UI as a list
                    println(results)

                    val adapter = IngredientAdapter(results, IngredientAdapter.OnClickListener{
                        adminClassifyViewModel.setIngredient(it)
                    })
                    recyclerView.adapter = adapter
                }
                else {
                    recyclerView.adapter = null
                    println("No data found")
                }
            })

        adminClassifyViewModel.ingredient.observe(viewLifecycleOwner,
            { data ->
                if (data != null) {
                    showIngredientDialog(data)
                } else {
                    println("Error fetching product")
                }
        })
    }

    private fun showIngredientDialog(ingredient: Ingredient) {
        val customDialog = ClassifyDialogFragment(
            this@AdminClassifyFragment,
            ingredient,
            requireContext()
        )

        customDialog.show()
        customDialog.setCanceledOnTouchOutside(false)

        val classifyDoneBtn = customDialog.findViewById<Button>(R.id.classify_dialog_done)
        classifyDoneBtn.setOnClickListener {
            adminClassifyViewModel.setIngredientClassification(ingredient, customDialog.selectedDiet)
            customDialog.dismiss()
        }
    }

}