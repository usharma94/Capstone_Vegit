package sheridan.sharmupm.vegit_capstone.ui.market

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import sheridan.sharmupm.vegit_capstone.R
import sheridan.sharmupm.vegit_capstone.controllers.market.SubmitProductViewModel


class SubmitProduct : Fragment() {

    private lateinit var submitViewModel: SubmitProductViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.submit_product_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        submitViewModel = ViewModelProvider(this).get(SubmitProductViewModel::class.java)
        submitViewModel.getIngredientNames()

        val nameEditText = view.findViewById<EditText>(R.id.et_name)
        val selectDiet = view.findViewById<Spinner>(R.id.diet_type)
        val selectCategory = view.findViewById<Spinner>(R.id.category)
        val imgEditText = view.findViewById<EditText>(R.id.et_img)
        val searchText = view.findViewById<EditText>(R.id.search_bar)
        val clearButton = view.findViewById<Button>(R.id.clear_text)
        val searchRecyclerView: RecyclerView = view.findViewById(R.id.searchIngredients)
        val selectedRecyclerView: RecyclerView = view.findViewById(R.id.selectedIngredients)
        val submitButton = view.findViewById<Button>(R.id.btn_submit_product)

        searchRecyclerView.addItemDecoration(
            DividerItemDecoration(
                context,
                LinearLayoutManager.VERTICAL
            )
        )

        ArrayAdapter.createFromResource(
            view.context, R.array.diet_types, android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            selectDiet.adapter = adapter
        }

        ArrayAdapter.createFromResource(
            view.context, R.array.category, android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            selectCategory.adapter = adapter
        }

        submitViewModel.submitProductFormState.observe(viewLifecycleOwner,
            Observer { submitFormState ->
                if (submitFormState == null) {
                    return@Observer
                }
                submitButton.isEnabled = true
                submitFormState.nameError?.let {
                    nameEditText.error = getString(it)
                }
                submitFormState.dietError?.let {
                    val appContext = context?.applicationContext ?: return@Observer
                    Toast.makeText(appContext, getString(it), Toast.LENGTH_SHORT).show()
                }
                submitFormState.categoryError?.let {
                    val appContext = context?.applicationContext ?: return@Observer
                    Toast.makeText(appContext, getString(it), Toast.LENGTH_SHORT).show()
                }
                submitFormState.imgError?.let {
                    imgEditText.error = getString(it)
                }
                submitFormState.ingredientError?.let {
                    val appContext = context?.applicationContext ?: return@Observer
                    Toast.makeText(appContext, getString(it), Toast.LENGTH_SHORT).show()
                }
            })

        selectDiet.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>?, selectedItemView: View,
                position: Int, id: Long) {
                submitViewModel.submitProductDietChanged(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
//                code here
            }
        }

        selectCategory.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>?, selectedItemView: View,
                                        position: Int, id: Long) {
                submitViewModel.submitProductCategoryChanged(position, selectCategory.selectedItem.toString())
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
//                code here
            }
        }

        val afterTextChangedListener = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // ignore
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // ignore
                searchRecyclerView.isVisible = true

            }

            override fun afterTextChanged(s: Editable) {
                submitViewModel.searchDataChanged(searchText.text.toString().trim())
            }
        }

        searchText.addTextChangedListener(afterTextChangedListener)

        submitViewModel.searchList.observe(viewLifecycleOwner,
            { results ->
                if (results != null) {
                    val searchAdapter = ProductIngredientAdapter(results, ProductIngredientAdapter.OnClickListener{
                        submitViewModel.addIngredient(it.name)
                    }, true)
                    searchRecyclerView.adapter = searchAdapter
                }
                else {
                    println("No data found")
                }
            })

        submitViewModel.ingredientList.observe(viewLifecycleOwner,
            { ingredients ->
                if (ingredients != null) {
                    val searchAdapter = ProductIngredientAdapter(ingredients, ProductIngredientAdapter.OnClickListener{
                        submitViewModel.removeIngredient(it.name)
                    }, false)
                    selectedRecyclerView.adapter = searchAdapter
                }
                else {
                    println("Error fetching ingredient")
                }
            })

        clearButton.setOnClickListener {
            searchText.text.clear()
            searchRecyclerView.isVisible = false
        }

        submitViewModel.productResponse.observe(viewLifecycleOwner,
            { product ->
                submitButton.isEnabled = true
                if (product != null) {
                    showSubmitProductSuccessful()
                } else {
                    showSubmitProductFailed()
                }
            })

        submitButton.setOnClickListener {
            submitButton.isEnabled = false
            submitViewModel.submitProductDataChanged(
                nameEditText.text.toString(),
                imgEditText.text.toString()
            )
        }
    }

    private fun showSubmitProductSuccessful() {
        val appContext = context?.applicationContext ?: return
        Toast.makeText(appContext, R.string.submit_product_success, Toast.LENGTH_LONG).show()
        this.findNavController().navigate(R.id.navigation_home)
    }

    private fun showSubmitProductFailed() {
        val appContext = context?.applicationContext ?: return
        Toast.makeText(appContext, R.string.submit_product_failed, Toast.LENGTH_LONG).show()
    }
}