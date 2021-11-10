package sheridan.sharmupm.vegit_capstone.ui.home.advertisement

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
import sheridan.sharmupm.vegit_capstone.controllers.home.AdvertisementViewModel

class AdvertisementFragment : Fragment() {

    private lateinit var advertisementViewModel: AdvertisementViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_advertisement, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        advertisementViewModel = ViewModelProvider(this).get(AdvertisementViewModel::class.java)
        advertisementViewModel.getAdvertisementProducts()

        val recyclerView: RecyclerView = view.findViewById(R.id.advertisementProducts)

        recyclerView.addItemDecoration(
            DividerItemDecoration(
                context,
                LinearLayoutManager.VERTICAL
            )
        )

        advertisementViewModel.productList.observe(viewLifecycleOwner,
            { results ->
                if (results != null) {
                    // display results of search in UI as a list
                    println(results)

                    val searchAdapter = AdvertisementAdapter(results, AdvertisementAdapter.OnClickListener{
                        advertisementViewModel.setAdvertisementProduct(it)
                    })
                    recyclerView.adapter = searchAdapter
                }
                else {
                    recyclerView.adapter = null
                    println("No data found")
                }
            })

        advertisementViewModel.selectedProduct.observe(viewLifecycleOwner,
            {
                    product->
                // display results of singular product
                var customDialog = AdvertisementDialogFragment(
                    this@AdvertisementFragment,
                    product,
                    requireContext()
                )
                //if we know that the particular variable not null any time ,we can assign !! (not null operator ), then  it won't check for null, if it becomes null, it willthrow exception
                customDialog.show()
                customDialog.setCanceledOnTouchOutside(false)
            })
    }
}