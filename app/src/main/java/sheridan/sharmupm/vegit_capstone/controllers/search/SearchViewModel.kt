package sheridan.sharmupm.vegit_capstone.controllers.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import sheridan.sharmupm.vegit_capstone.helpers.getDiet
import sheridan.sharmupm.vegit_capstone.helpers.getSearchIngredientList
import sheridan.sharmupm.vegit_capstone.helpers.setSearchIngredientList
import sheridan.sharmupm.vegit_capstone.models.DietModel
import sheridan.sharmupm.vegit_capstone.models.ingredients.Ingredient
import sheridan.sharmupm.vegit_capstone.models.ingredients.IngredientName
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

    private var ingredientNames: List<IngredientName> = mutableListOf()

    val searchList = MutableLiveData<List<IngredientName>>()

    val userDiet = MutableLiveData<DietModel>()

    fun getIngredientNames() {
        val cachedNames = getSearchIngredientList()
        if (cachedNames != null) {
            ingredientNames = cachedNames as List<IngredientName>
        } else {
            // fetch names from API
            scope.launch {
                val names = repository.fetchIngredientNames()
                if (names != null) {
                    ingredientNames = names

                    // set list in cache
                    setSearchIngredientList(names)
                }
            }
        }
    }

    fun searchDataChanged(name: String) {
        if (name.isNotEmpty()) {
            val filtered = ingredientNames.filter { it.name.contains(name, ignoreCase = true) }
            searchList.postValue(filtered)
        }
    }

    fun searchIngredients(name: String) {
        val search = IngredientName(name)

        scope.launch {
            val ingredient = repository.searchIngredients(search)
            searchResult.postValue(ingredient)
        }
    }

    fun getUserDiet() {
        scope.launch {
            val diet = getDiet()
            userDiet.postValue(diet)
        }
    }
}