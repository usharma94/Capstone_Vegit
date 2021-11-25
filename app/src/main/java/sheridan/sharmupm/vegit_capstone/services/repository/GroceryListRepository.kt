package sheridan.sharmupm.vegit_capstone.services.repository

import sheridan.sharmupm.vegit_capstone.models.groceryList.Grocery
import sheridan.sharmupm.vegit_capstone.models.groceryList.SubmitGrocery
import sheridan.sharmupm.vegit_capstone.models.products.Product
import sheridan.sharmupm.vegit_capstone.models.products.SubmitProduct
import sheridan.sharmupm.vegit_capstone.services.network.ApiInterface

class GroceryListRepository(private val api: ApiInterface) : BaseRepository() {
    suspend fun getAllGroceryItems(): List<Grocery>? {
        return safeApiCall(
            call = {api.getAllGroceryItemsAsync().await()},
            errorMessage = "No data found"
        )
    }

    suspend fun addGroceryItem(submitGrocery: SubmitGrocery): Grocery? {
        return safeApiCall(
            call = {api.addGroceryItemAsync(submitGrocery).await()},

            errorMessage = "Failed to submit grocery"
        )
    }

    suspend fun deleteGrocery(id: Int): Int? {
        return safeApiCall(
            call = {api.deleteGroceryAsync(id).await()},
            errorMessage = "Failed to delete grocery"
        )
    }

    suspend fun flipGroceryStatus(id: Int): Int? {
        return safeApiCall(
            call = {api.flipGroceryStatus(id).await()},
            errorMessage = "Failed to flip grocery status"
        )
    }

    suspend fun updateGroceryItem(id: Int,submitGrocery: SubmitGrocery):Grocery?{
        return safeApiCall(
            call = {api.updateGroceryItemAsync(id,submitGrocery).await()},
            errorMessage = "Failed to update grocery item"
        )
    }

    suspend fun getGroceryItem(id: Int): Grocery? {
        return safeApiCall(
            call = {api.getGroceryItemAsync(id).await()},
            errorMessage = "Failed to get Grocery Item"
        )
    }







}