package sheridan.sharmupm.vegit_capstone.controllers.market

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import sheridan.sharmupm.vegit_capstone.R
import sheridan.sharmupm.vegit_capstone.helpers.getSearchIngredientList
import sheridan.sharmupm.vegit_capstone.helpers.setSearchIngredientList
import sheridan.sharmupm.vegit_capstone.models.ingredients.Ingredient
import sheridan.sharmupm.vegit_capstone.models.ingredients.IngredientId
import sheridan.sharmupm.vegit_capstone.models.ingredients.IngredientName
import sheridan.sharmupm.vegit_capstone.models.products.Product
import sheridan.sharmupm.vegit_capstone.models.products.SubmitProduct
import sheridan.sharmupm.vegit_capstone.models.products.SubmitProductFormState
import sheridan.sharmupm.vegit_capstone.services.network.APIClient
import sheridan.sharmupm.vegit_capstone.services.repository.IngredientRepository
import sheridan.sharmupm.vegit_capstone.services.repository.ProductRepository
import kotlin.coroutines.CoroutineContext

class SubmitProductViewModel : ViewModel() {
    private val parentJob = Job()

    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Default

    private val scope = CoroutineScope(coroutineContext)

    private val productRepository : ProductRepository = ProductRepository(APIClient.apiInterface)
    private val ingredientRepository : IngredientRepository = IngredientRepository(APIClient.apiInterface)

    private val _productForm = MutableLiveData<SubmitProductFormState>()
    val submitProductFormState: LiveData<SubmitProductFormState> = _productForm
    private var dietType: Int = 0
    private val ingredients: MutableList<Ingredient> = mutableListOf()
    private val selectedIngredientNames: MutableList<IngredientName> = mutableListOf()
    private var ingredientNames: List<IngredientName> = mutableListOf()
    val ingredientList = MutableLiveData<List<IngredientName>>()
    val searchList = MutableLiveData<List<IngredientName>>()
    val productResponse = MutableLiveData<Product>()

    private fun submitProduct(name: String) {
        val product = SubmitProduct(
            name,
            dietType(),
            "null",
            "null",
            ingredients.map { IngredientId(it.id) }.toList()
        )

        scope.launch {
            val response = productRepository.submitMarketProduct(product)
            productResponse.postValue(response)
        }
    }

    fun submitProductDataChanged(name: String) {
        if (!isNameValid(name)) {
            _productForm.value = SubmitProductFormState(nameError = R.string.invalid_name)
        } else if (dietType == 0) {
            _productForm.value = SubmitProductFormState(dietError = R.string.invalid_diet)
        } else if (ingredients.isEmpty()) {
            _productForm.value = SubmitProductFormState(ingredientError = R.string.invalid_ingredients)
        } else {
             submitProduct(name)
        }
    }

    fun getIngredientNames() {
        val cachedNames = getSearchIngredientList()
        if (cachedNames != null) {
            ingredientNames = cachedNames as List<IngredientName>
        } else {
            // fetch names from API
            scope.launch {
                val names = ingredientRepository.fetchIngredientNames()
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

    fun addIngredient(name: String) {
        if (ingredients.any { it.name == name }) return

        val search = IngredientName(name)
        scope.launch {
            val ingredient = ingredientRepository.searchIngredients(search)
            selectedIngredientNames.add(search)
            ingredientList.postValue(selectedIngredientNames)

            ingredients.add(ingredient!!)
            println(ingredients)
        }
    }

    fun removeIngredient(name: String) {
        val selected = IngredientName(name)
        selectedIngredientNames.remove(selected)
        ingredientList.postValue(selectedIngredientNames)
        ingredients.removeAll { it.name == name }
        println(ingredients)
    }

    private fun isNameValid(name: String): Boolean {
        return name.isNotBlank() && name.length > 2
    }

    fun submitProductDietChanged(position: Int) {
        dietType = position
    }

    private fun dietType(): Int {
        if (dietType == 1) return 1
        if (dietType == 2) return 3
        if (dietType == 3) return 6
        return 5
    }

    fun cancelRequest() = coroutineContext.cancel()
}