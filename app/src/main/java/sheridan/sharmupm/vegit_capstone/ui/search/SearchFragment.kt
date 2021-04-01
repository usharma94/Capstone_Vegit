package sheridan.sharmupm.vegit_capstone.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
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

        // called via search button press
        // set loading icon visible... disable search button while searching...
        searchViewModel.searchIngredients("wool")

        searchViewModel.searchResult.observe(viewLifecycleOwner,
            { ingredient ->
                // hide loading icon here...
                // re-enable search button...
                if (ingredient != null) {
                    // display results of search in UI
                    println(ingredient)
                }
                else {
                    // display no data found message to user in UI
                    println("No data found")
                }
            })
    }

}