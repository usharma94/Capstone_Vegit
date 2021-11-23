package sheridan.sharmupm.vegit_capstone.ui.home.statistics

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import sheridan.sharmupm.vegit_capstone.R
import sheridan.sharmupm.vegit_capstone.controllers.home.StatisticsViewModel
import sheridan.sharmupm.vegit_capstone.ui.home.advertisement.AdvertisementDialogFragment

class StatisticsFragment : Fragment() {

    private lateinit var statisticsViewModel: StatisticsViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.statistics_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        statisticsViewModel = ViewModelProvider(this).get(StatisticsViewModel::class.java)
        statisticsViewModel.getAvoidProducts()
        statisticsViewModel.getIngredientSafe()
        statisticsViewModel.getIngredientNotSafe()

        val recyclerView: RecyclerView = view.findViewById(R.id.avoidProducts)
        val recyclerviewSafe: RecyclerView = view.findViewById(R.id.safeIngredients)
        val recyclerviewNotSafe: RecyclerView = view.findViewById(R.id.notSafeIngredients)

        statisticsViewModel.avoidProductList.observe(viewLifecycleOwner,
            { results ->
                if (results != null) {
                    val searchAdapter = AvoidAdapter(results, AvoidAdapter.OnClickListener{
                        statisticsViewModel.setAdvertisementProduct(it)
                    })
                    recyclerView.adapter = searchAdapter
                }
                else {
                    recyclerView.adapter = null
                    println("No data found")
                }
            })

        statisticsViewModel.selectedProduct.observe(viewLifecycleOwner,
            {
                product->
                // display results of singular product
                var customDialog = AdvertisementDialogFragment(
                    this@StatisticsFragment,
                    product,
                    requireContext()
                )
                //if we know that the particular variable not null any time ,we can assign !! (not null operator ), then  it won't check for null, if it becomes null, it willthrow exception
                customDialog.show()
                customDialog.setCanceledOnTouchOutside(false)
            })

        statisticsViewModel.ingredientSafe.observe(viewLifecycleOwner,
            { results ->
                if (results != null) {
                    val searchAdapter = IngredientSafetyAdapter(results)
                    recyclerviewSafe.adapter = searchAdapter
                }
                else {
                    recyclerviewSafe.adapter = null
                    println("No data found")
                }
            })

        statisticsViewModel.ingredientNotSafe.observe(viewLifecycleOwner,
            { results ->
                if (results != null) {
                    val searchAdapter = IngredientSafetyAdapter(results)
                    recyclerviewNotSafe.adapter = searchAdapter
                }
                else {
                    recyclerviewNotSafe.adapter = null
                    println("No data found")
                }
            })
    }
}