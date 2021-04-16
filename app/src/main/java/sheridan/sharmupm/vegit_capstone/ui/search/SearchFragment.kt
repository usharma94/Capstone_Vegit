package sheridan.sharmupm.vegit_capstone.ui.search

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import sheridan.sharmupm.vegit_capstone.R
import sheridan.sharmupm.vegit_capstone.controllers.search.SearchViewModel

class SearchFragment : Fragment() {

    private lateinit var searchViewModel: SearchViewModel


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.search_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        searchViewModel = ViewModelProvider(this).get(SearchViewModel::class.java)
        // TODO: Use the ViewModel

        searchViewModel.getIngredientNames()

        val searchText = view.findViewById<EditText>(R.id.search_bar)
        val clearButton = view.findViewById<Button>(R.id.clear_text)
        val searchResult = view.findViewById<TextView>(R.id.search_result_text)
        val recyclerView: RecyclerView = view.findViewById(R.id.searchResult)

        val afterTextChangedListener = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // ignore
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // ignore
                recyclerView.isVisible = true;

            }

            override fun afterTextChanged(s: Editable) {
                searchViewModel.searchDataChanged(
                        searchText.text.toString().trim()
                )
            }
        }

        searchText.addTextChangedListener(afterTextChangedListener)

        searchViewModel.searchList.observe(viewLifecycleOwner,
                { results ->
                    if (results != null) {
                        // UPMA UPMA UPMA UPMA UPMA UPMA UPMA
                        // display results of search in UI as a list
                        println(results)

//                        user clicks ingredient name
                        val searchAdapter = SearchAdapter(results, SearchAdapter.OnClickListener{

                            searchViewModel.searchIngredients(it.name)
                            searchViewModel.searchResult.observe(viewLifecycleOwner,
                                { ingredient ->
                                    // UPMA UPMA UPMA UPMA UPMA UPMA UPMA
                                    // hide loading icon here

                                    if (ingredient != null) {
                                        // UPMA UPMA UPMA UPMA UPMA UPMA UPMA
                                        // display results of singular food item
                                        println(ingredient)
                                        val ingredientDetail = ingredient.name + " - " + ingredient.diet_name
                                        val dataAdapter = SearchDialogAdapter(ingredientDetail, this)
                                        var customDialog = SearchDialogFragment(
                                            this@SearchFragment,
                                            dataAdapter,
                                            requireContext()
                                        )
                                        //if we know that the particular variable not null any time ,we can assign !! (not null operator ), then  it won't check for null, if it becomes null, it willthrow exception
                                        customDialog!!.show()
                                        customDialog!!.setCanceledOnTouchOutside(false)

                                    }
                                    else {
                                        println("Error fetching ingredient")
                                    }



                                })
//                            ingredientDetail.clear()
                            recyclerView?.adapter?.notifyDataSetChanged()


                        })
                        recyclerView.adapter = searchAdapter


                    }
                    else {
                        println("No data found")
                    }
                })

        // UPMA UPMA UPMA UPMA UPMA UPMA UPMA
        // upon user tapping an ingredient item, call the below with given item name
        //searchViewModel.searchIngredients("item name")
        // also show a loading icon for better feedback

//        searchViewModel.searchResult.observe(viewLifecycleOwner,
//            { ingredient ->
//                // UPMA UPMA UPMA UPMA UPMA UPMA UPMA
//                // hide loading icon here
//
//                if (ingredient != null) {
//                    // UPMA UPMA UPMA UPMA UPMA UPMA UPMA
//                    // display results of singular food item
//                    println(ingredient)
//                }
//                else {
//                    println("Error fetching ingredient")
//                }
//            })

        clearButton.setOnClickListener {
            searchText.text.clear()
            recyclerView.isVisible = false

        }
    }

}