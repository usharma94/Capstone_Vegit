package sheridan.sharmupm.vegit_capstone.ui.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import sheridan.sharmupm.vegit_capstone.R
import sheridan.sharmupm.vegit_capstone.controllers.user.AdminProductViewModel
import sheridan.sharmupm.vegit_capstone.models.products.Product
import sheridan.sharmupm.vegit_capstone.ui.home.AdvertisementAdapter

class AdminProductFragment : Fragment() {

    private lateinit var adminProductViewModel: AdminProductViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.admin_product_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        adminProductViewModel = ViewModelProvider(this).get(AdminProductViewModel::class.java)
        adminProductViewModel.getApproveProducts()

        val recyclerView: RecyclerView = view.findViewById(R.id.approveProducts)

        recyclerView.addItemDecoration(
            DividerItemDecoration(
                context,
                LinearLayoutManager.VERTICAL
            )
        )

        adminProductViewModel.productList.observe(viewLifecycleOwner,
            { results ->
                if (results != null) {
                    // display results of search in UI as a list
                    println(results)

                    val searchAdapter = AdvertisementAdapter(results, AdvertisementAdapter.OnClickListener{
                        adminProductViewModel.setApproveProduct(it)
                    })
                    recyclerView.adapter = searchAdapter
                }
                else {
                    recyclerView.adapter = null
                    println("No data found")
                }
            })

        adminProductViewModel.approveResult.observe(viewLifecycleOwner,
            { product ->
                if (product != null) {
                    showProductDialog(product)
                } else {
                    println("Error fetching product")
                }
        })
    }

    private fun showProductDialog(product: Product) {
        val customDialog = ApproveDialogFragment(
            this@AdminProductFragment,
            product,
            requireContext()
        )

        customDialog.show()
        customDialog.setCanceledOnTouchOutside(false)

        val acceptProductBtn = customDialog.findViewById<Button>(R.id.approve_dialog_accept)
        acceptProductBtn.setOnClickListener {
            adminProductViewModel.acceptProduct(product)
            customDialog.dismiss()
        }
        val denyProductBtn = customDialog.findViewById<Button>(R.id.deny_dialog_accept)
        denyProductBtn.setOnClickListener {
            showDenyDialog(product.id!!)
            customDialog.dismiss()
        }
    }

    private fun showDenyDialog(id: Int) {
        val denyDialog = DenyDialogFragment(
            this@AdminProductFragment,
            requireContext()
        )
        denyDialog.show()
        denyDialog.setCanceledOnTouchOutside(false)

        val submitReasonBtn = denyDialog.findViewById<Button>(R.id.submit_reason)
        val reason = denyDialog.findViewById<EditText>(R.id.reason)
        submitReasonBtn.setOnClickListener {
            adminProductViewModel.denyProduct(id, reason.text.toString())
            denyDialog.dismiss()
        }
    }
}