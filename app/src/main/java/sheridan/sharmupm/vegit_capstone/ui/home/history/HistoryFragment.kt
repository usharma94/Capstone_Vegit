package sheridan.sharmupm.vegit_capstone.ui.home.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import sheridan.sharmupm.vegit_capstone.R
import sheridan.sharmupm.vegit_capstone.controllers.home.HistoryViewModel
import sheridan.sharmupm.vegit_capstone.ui.home.advertisement.AdvertisementAdapter
import sheridan.sharmupm.vegit_capstone.ui.home.advertisement.AdvertisementDialogFragment

class HistoryFragment : Fragment() {

    private lateinit var historyViewModel: HistoryViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.history_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        historyViewModel = ViewModelProvider(this).get(HistoryViewModel::class.java)

        historyViewModel.getScanHistory()

        val recyclerviewScanHistory: RecyclerView = view.findViewById(R.id.scanHistory)

        historyViewModel.scanHistoryList.observe(viewLifecycleOwner,
            { results ->
                if (results != null) {
                    val searchAdapter = AdvertisementAdapter(results, AdvertisementAdapter.OnClickListener{
                        historyViewModel.setProduct(it)
                    })
                    recyclerviewScanHistory.adapter = searchAdapter
                }
                else {
                    recyclerviewScanHistory.adapter = null
                    println("No data found")
                }
            })

        historyViewModel.selectedScanHistoryProduct.observe(viewLifecycleOwner,
            {
                    product->
                // display results of singular product
                var customDialog = AdvertisementDialogFragment(
                    this@HistoryFragment,
                    product,
                    requireContext()
                )
                //if we know that the particular variable not null any time ,we can assign !! (not null operator ), then  it won't check for null, if it becomes null, it willthrow exception
                customDialog.show()
                customDialog.setCanceledOnTouchOutside(false)
            })
    }

}