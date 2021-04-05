package sheridan.sharmupm.vegit_capstone.controllers.classifyProducts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import sheridan.sharmupm.vegit_capstone.models.ingredients.Ingredient
import sheridan.sharmupm.vegit_capstone.models.ingredients.IngredientName
import sheridan.sharmupm.vegit_capstone.services.network.APIClient
import sheridan.sharmupm.vegit_capstone.services.repository.IngredientRepository
import kotlin.coroutines.CoroutineContext

class ClassifyproductsViewModel : ViewModel() {

    private val parentJob = Job()

    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Default

    private val scope = CoroutineScope(coroutineContext)

    private val repository : IngredientRepository = IngredientRepository(APIClient.apiInterface)

    val ingredientResults = MutableLiveData<List<Ingredient>>()

    private val _text = MutableLiveData<String>().apply {
        value = "This is dashboard Fragment"
    }
    val text: LiveData<String> = _text

    // must pass in a list of IngredientName data objects
    fun searchIngredientList(ingredientNames: List<IngredientName>) {
        scope.launch {
            val results = repository.searchIngredientList(ingredientNames)
            ingredientResults.postValue(results)

            // showing results in log output
            println(results)
        }
    }
}