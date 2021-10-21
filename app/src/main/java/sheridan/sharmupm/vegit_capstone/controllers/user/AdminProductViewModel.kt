package sheridan.sharmupm.vegit_capstone.controllers.user

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import sheridan.sharmupm.vegit_capstone.models.products.DenyProduct
import sheridan.sharmupm.vegit_capstone.models.products.Product
import sheridan.sharmupm.vegit_capstone.services.network.APIClient
import sheridan.sharmupm.vegit_capstone.services.repository.ProductRepository
import kotlin.coroutines.CoroutineContext

class AdminProductViewModel : ViewModel() {

    private val parentJob = Job()

    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Default

    private val scope = CoroutineScope(coroutineContext)

    private val repository : ProductRepository = ProductRepository(APIClient.apiInterface)

    val productList = MutableLiveData<List<Product>>()

    val approveResult = MutableLiveData<Product>()

    fun getApproveProducts() {
        scope.launch {
            val products = repository.fetchApproveProducts()
            productList.postValue(products)
        }
    }

    fun setApproveProduct(product: Product) {
        approveResult.postValue(product)
    }

    fun acceptProduct(product: Product) {
        scope.launch {
            repository.acceptProduct(product.id!!)
            getApproveProducts()
        }
    }

    fun denyProduct(id: Int, reason: String) {
        scope.launch {
            val deny = DenyProduct()
            deny.id = id
            deny.reason = reason

            repository.denyProduct(deny)
            getApproveProducts()
        }
    }
}