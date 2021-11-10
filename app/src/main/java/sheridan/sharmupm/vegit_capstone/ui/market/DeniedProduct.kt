package sheridan.sharmupm.vegit_capstone.ui.market

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import sheridan.sharmupm.vegit_capstone.R
import sheridan.sharmupm.vegit_capstone.controllers.market.DeniedProductViewModel
import sheridan.sharmupm.vegit_capstone.ui.home.advertisement.AdvertisementDialogFragment

class DeniedProduct : Fragment() {

    private lateinit var deniedProductViewModel: DeniedProductViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.approved_product_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        deniedProductViewModel = ViewModelProvider(this).get(DeniedProductViewModel::class.java)
        deniedProductViewModel.getDeniedProducts()

        val recyclerView: RecyclerView = view.findViewById(R.id.approvedProducts)

        recyclerView.addItemDecoration(
            DividerItemDecoration(
                context,
                LinearLayoutManager.VERTICAL
            )
        )

        deniedProductViewModel.productList.observe(viewLifecycleOwner,
            { results ->
                if (results != null) {
                    // display results of search in UI as a list
                    println(results)

                    val searchAdapter = DeniedAdapter(results, DeniedAdapter.OnClickListener{
                        deniedProductViewModel.setSelectedProduct(it)
                    })
                    recyclerView.adapter = searchAdapter
                }
                else {
                    recyclerView.adapter = null
                    println("No data found")
                }
            })

        deniedProductViewModel.selectedProduct.observe(viewLifecycleOwner,
            {
                    product->
                // display results of singular product
                var customDialog = AdvertisementDialogFragment(
                    this@DeniedProduct,
                    product,
                    requireContext()
                )
                //if we know that the particular variable not null any time ,we can assign !! (not null operator ), then  it won't check for null, if it becomes null, it willthrow exception
                customDialog.show()
                customDialog.setCanceledOnTouchOutside(false)
            })
    }

}