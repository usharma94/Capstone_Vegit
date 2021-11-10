package sheridan.sharmupm.vegit_capstone.controllers.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import sheridan.sharmupm.vegit_capstone.helpers.getDiet
import sheridan.sharmupm.vegit_capstone.models.products.Product
import sheridan.sharmupm.vegit_capstone.services.network.APIClient
import sheridan.sharmupm.vegit_capstone.services.repository.ProductRepository
import kotlin.coroutines.CoroutineContext

class AdvertisementViewModel : ViewModel() {
    private val parentJob = Job()

    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Default

    private val scope = CoroutineScope(coroutineContext)

    private val repository : ProductRepository = ProductRepository(APIClient.apiInterface)

    val productList = MutableLiveData<List<Product>>()

    val selectedProduct = MutableLiveData<Product>()

    fun setAdvertisementProduct(product: Product) {
        scope.launch {
            repository.viewProduct(product.id!!)
            selectedProduct.postValue(product)
        }
    }

    fun getAdvertisementProducts() {
        scope.launch {
            val diet = getDiet()
            if (diet != null) {
                val products = repository.fetchAdvertisementProducts(diet.dietType!!)
                productList.postValue(products)
            } else {
                val products = repository.fetchAdvertisementProducts(0)
                productList.postValue(products)
            }
        }
    }
}