package sheridan.sharmupm.vegit_capstone.controllers.market

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import sheridan.sharmupm.vegit_capstone.models.products.Product
import sheridan.sharmupm.vegit_capstone.services.network.APIClient
import sheridan.sharmupm.vegit_capstone.services.repository.ProductRepository
import kotlin.coroutines.CoroutineContext

class DeniedProductViewModel : ViewModel() {
    private val parentJob = Job()

    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Default

    private val scope = CoroutineScope(coroutineContext)

    private val repository : ProductRepository = ProductRepository(APIClient.apiInterface)

    val productList = MutableLiveData<List<Product>>()

    val selectedProduct = MutableLiveData<Product>()

    fun setSelectedProduct(product: Product) {
        scope.launch {
            selectedProduct.postValue(product)
        }
    }

    fun getDeniedProducts() {
        scope.launch {
            val products = repository.fetchDeniedProducts()
            productList.postValue(products)
        }
    }
}