package sheridan.sharmupm.vegit_capstone.controllers.groceryList

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import sheridan.sharmupm.vegit_capstone.models.groceryList.Grocery
import sheridan.sharmupm.vegit_capstone.models.groceryList.SubmitGrocery
import sheridan.sharmupm.vegit_capstone.services.network.APIClient
import sheridan.sharmupm.vegit_capstone.services.repository.GroceryListRepository
import kotlin.coroutines.CoroutineContext

class GroceryListViewModel: ViewModel() {
    private val parentJob = Job()

    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Default

    private val scope = CoroutineScope(coroutineContext)

    private val repository : GroceryListRepository = GroceryListRepository(APIClient.apiInterface)

    val groceryList = MutableLiveData<List<Grocery>>()
    val grocery = MutableLiveData<Grocery>()


    fun getAllGroceryItems() {

        scope.launch {
            val products = repository.getAllGroceryItems()
            groceryList.postValue(products)
        }
    }

    fun getGroceryItem(id:Int){
        scope.launch {
           val product = repository.getGroceryItem(id)
            grocery.postValue(product)

        }
    }

    fun deleteGrocery(id:Int){
        scope.launch {
            val id = repository.deleteGrocery(id)
        }
    }

    fun flipGroceryStatus(id:Int){
        scope.launch {
            val id = repository.flipGroceryStatus(id)
        }
    }

    fun updateGroceryItem(id:Int,name:String,due:String,status:Int){
        val grocery = SubmitGrocery(
            name,
            due,
            status
        )
        scope.launch {
            val response = repository.updateGroceryItem(id,grocery)

        }
    }


    fun submitGrocery(name: String, due: String) {
        val grocery = SubmitGrocery(
            name,
            due,
            0
        )

        scope.launch {
            val response = repository.addGroceryItem(grocery)
            if (response != null){
                groceryList.postValue(listOf(response))

                val products = repository.getAllGroceryItems()
                groceryList.postValue(products)
            }
        }
    }
}