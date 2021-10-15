package sheridan.sharmupm.vegit_capstone.ui.market

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import sheridan.sharmupm.vegit_capstone.R
import sheridan.sharmupm.vegit_capstone.controllers.market.ApprovedProductViewModel

class ApprovedProduct : Fragment() {

    companion object {
        fun newInstance() = ApprovedProduct()
    }

    private lateinit var viewModel: ApprovedProductViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.approved_product_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ApprovedProductViewModel::class.java)
        // TODO: Use the ViewModel
    }

}