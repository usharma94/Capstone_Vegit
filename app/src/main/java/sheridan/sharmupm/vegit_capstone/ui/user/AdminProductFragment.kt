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
import sheridan.sharmupm.vegit_capstone.controllers.user.AdminProductViewModel

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

                    val searchAdapter = ProductAdapter(results, ProductAdapter.OnClickListener{
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
            { result ->
                if (result != null) {
                    adminProductViewModel.approveResult.observe(viewLifecycleOwner,
                        {
                            product->
                            // display results of singular product
                            var customDialog = ApproveDialogFragment(
                                this@AdminProductFragment,
                                product,
                                requireContext()
                            )
                            //if we know that the particular variable not null any time ,we can assign !! (not null operator ), then  it won't check for null, if it becomes null, it willthrow exception
                            customDialog.show()
                            customDialog.setCanceledOnTouchOutside(false)

                            val acceptProductBtn = customDialog.findViewById<Button>(R.id.approve_dialog_accept)
                            acceptProductBtn.setOnClickListener {
                                adminProductViewModel.acceptProduct(result)
                                customDialog.dismiss()
                            }
                            val denyProductBtn = customDialog.findViewById<Button>(R.id.deny_dialog_accept)
                            denyProductBtn.setOnClickListener {
                                adminProductViewModel.denyProduct(result)
                                customDialog.dismiss()
                            }
                        })
                }
                else {
                    println("Error fetching product")
                }
            })
    }
}