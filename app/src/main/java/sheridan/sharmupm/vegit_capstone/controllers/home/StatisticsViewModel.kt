package sheridan.sharmupm.vegit_capstone.controllers.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import sheridan.sharmupm.vegit_capstone.helpers.getDiet
import sheridan.sharmupm.vegit_capstone.models.ingredients.Ingredient
import sheridan.sharmupm.vegit_capstone.models.products.Product
import sheridan.sharmupm.vegit_capstone.services.network.APIClient
import sheridan.sharmupm.vegit_capstone.services.repository.IngredientRepository
import sheridan.sharmupm.vegit_capstone.services.repository.ProductRepository
import kotlin.coroutines.CoroutineContext

class StatisticsViewModel : ViewModel() {
    private val parentJob = Job()

    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Default

    private val scope = CoroutineScope(coroutineContext)

    private val repository : ProductRepository = ProductRepository(APIClient.apiInterface)
    private val ingredientRepository : IngredientRepository = IngredientRepository(APIClient.apiInterface)

    val avoidProductList = MutableLiveData<List<Product>>()
    val ingredientSafe = MutableLiveData<List<Ingredient>>()
    val ingredientNotSafe = MutableLiveData<List<Ingredient>>()
    val scanHistoryList = MutableLiveData<List<Product>>()

    val selectedProduct = MutableLiveData<Product>()
    val selectedScanHistoryProduct = MutableLiveData<Product>()

    fun getAvoidProducts() {
        scope.launch {
            val diet = getDiet()
            if (diet != null) {
                val products = repository.fetchAvoidProducts(diet.dietType!!)
                avoidProductList.postValue(products)
            } else {
                val products = repository.fetchAvoidProducts(0)
                avoidProductList.postValue(products)
            }
        }
    }

    fun setAdvertisementProduct(product: Product) {
        scope.launch {
            repository.viewProduct(product.id!!)
            selectedProduct.postValue(product)
        }
    }

    fun getIngredientSafe() {
        scope.launch {
            val diet = getDiet()
            if (diet != null) {
                val ingredients = ingredientRepository.fetchSafeIngredients(diet.dietType!!)
                ingredientSafe.postValue(ingredients)
            } else {
                val ingredients = ingredientRepository.fetchSafeIngredients(0)
                ingredientSafe.postValue(ingredients)
            }
        }
    }

    fun getIngredientNotSafe() {
        scope.launch {
            val diet = getDiet()
            if (diet != null) {
                val ingredients = ingredientRepository.fetchNotSafeIngredients(diet.dietType!!)
                ingredientNotSafe.postValue(ingredients)
            } else {
                val ingredients = ingredientRepository.fetchNotSafeIngredients(0)
                ingredientNotSafe.postValue(ingredients)
            }
        }
    }

    fun getScanHistory() {
        scope.launch {
            val products = repository.fetchScanHistory()
            scanHistoryList.postValue(products)
        }
    }

    fun setProduct(product: Product) {
        scope.launch {
            repository.viewProduct(product.id!!)
            selectedScanHistoryProduct.postValue(product)
        }
    }
}