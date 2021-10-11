package sheridan.sharmupm.vegit_capstone.services.repository

import sheridan.sharmupm.vegit_capstone.models.products.Product
import sheridan.sharmupm.vegit_capstone.models.products.SubmitProduct
import sheridan.sharmupm.vegit_capstone.services.network.ApiInterface

class ProductRepository(private val api: ApiInterface) : BaseRepository() {
    suspend fun fetchApproveProducts(): List<Product>? {
        return safeApiCall(
                call = {api.fetchApproveProductsAsync().await()},
                errorMessage = "No data found"
        )
    }

    suspend fun acceptProduct(id: Int): Int? {
        return safeApiCall(
            call = {api.acceptProductAsync(id).await()},
            errorMessage = "Failed to accept product"
        )
    }

    suspend fun denyProduct(id: Int): Int? {
        return safeApiCall(
            call = {api.denyProductAsync(id).await()},
            errorMessage = "Failed to accept product"
        )
    }

    suspend fun submitMarketProduct(submitProduct: SubmitProduct): Product? {
        return safeApiCall(
            call = {api.submitMarketProductAsync(submitProduct).await()},
            errorMessage = "Failed to submit product"
        )
    }
}