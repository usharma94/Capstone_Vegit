package sheridan.sharmupm.vegit_capstone.controllers.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import sheridan.sharmupm.vegit_capstone.models.ingredients.Ingredient
import sheridan.sharmupm.vegit_capstone.models.ingredients.SearchSingle
import sheridan.sharmupm.vegit_capstone.services.network.APIClient
import sheridan.sharmupm.vegit_capstone.services.repository.IngredientRepository
import kotlin.coroutines.CoroutineContext

class SearchViewModel : ViewModel() {

    private val parentJob = Job()

    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Default

    private val scope = CoroutineScope(coroutineContext)

    private val repository : IngredientRepository = IngredientRepository(APIClient.apiInterface)

    val searchResult = MutableLiveData<Ingredient>()

    fun searchIngredients(name: String) {
        val search = SearchSingle()
        search.name = name

        scope.launch {
            val ingredient = repository.searchIngredients(search)
            searchResult.postValue(ingredient)
        }
    }

}