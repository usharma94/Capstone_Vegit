package sheridan.sharmupm.vegit_capstone.controllers.home

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

class HistoryViewModel : ViewModel() {
    private val parentJob = Job()

    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Default

    private val scope = CoroutineScope(coroutineContext)

    private val repository : ProductRepository = ProductRepository(APIClient.apiInterface)

    val scanHistoryList = MutableLiveData<List<Product>>()
    val selectedScanHistoryProduct = MutableLiveData<Product>()

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