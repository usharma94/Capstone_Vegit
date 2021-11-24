package sheridan.sharmupm.vegit_capstone.controllers.user

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import sheridan.sharmupm.vegit_capstone.models.ingredients.Ingredient
import sheridan.sharmupm.vegit_capstone.services.network.APIClient
import sheridan.sharmupm.vegit_capstone.services.repository.IngredientRepository
import kotlin.coroutines.CoroutineContext

class AdminClassifyViewModel : ViewModel() {
    private val parentJob = Job()

    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Default

    private val scope = CoroutineScope(coroutineContext)

    private val repository : IngredientRepository = IngredientRepository(APIClient.apiInterface)

    val ingredientList = MutableLiveData<List<Ingredient>>()

    val ingredient = MutableLiveData<Ingredient>()

    fun getIngredientsToClassify() {
        scope.launch {
            val ingredients = repository.fetchIngredientsToClassify()
            ingredientList.postValue(ingredients)
        }
    }

    fun setIngredient(item: Ingredient) {
        ingredient.postValue(item)
    }

    fun setIngredientClassification(item: Ingredient, diet: Int) {
        scope.launch {
            repository.updateClassificationAsync(item.id!!, dietType(diet))
            getIngredientsToClassify()
        }
    }

    private fun dietType(dietType: Int): Int {
        if (dietType == 1) return 1
        if (dietType == 2) return 3
        if (dietType == 3) return 6
        return 5
    }
}