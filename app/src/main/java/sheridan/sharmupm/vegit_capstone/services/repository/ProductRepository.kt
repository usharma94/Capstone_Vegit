package sheridan.sharmupm.vegit_capstone.services.repository

import sheridan.sharmupm.vegit_capstone.models.products.DenyProduct
import sheridan.sharmupm.vegit_capstone.models.products.Product
import sheridan.sharmupm.vegit_capstone.models.products.SubmitProduct
import sheridan.sharmupm.vegit_capstone.services.network.ApiInterface

class ProductRepository(private val api: ApiInterface) : BaseRepository() {
    suspend fun fetchAdvertisementProducts(diet: Int): List<Product>? {
        return safeApiCall(
            call = {api.fetchAdvertisementProductsAsync(diet).await()},
            errorMessage = "No data found"
        )
    }

    suspend fun fetchApproveProducts(): List<Product>? {
        return safeApiCall(
                call = {api.fetchApproveProductsAsync().await()},
                errorMessage = "No data found"
        )
    }

    suspend fun fetchApprovedProducts(): List<Product>? {
        return safeApiCall(
            call = {api.fetchApprovedProductsAsync().await()},
            errorMessage = "No data found"
        )
    }

    suspend fun fetchDeniedProducts(): List<Product>? {
        return safeApiCall(
            call = {api.fetchDeniedProductsAsync().await()},
            errorMessage = "No data found"
        )
    }

    suspend fun acceptProduct(id: Int): Int? {
        return safeApiCall(
            call = {api.acceptProductAsync(id).await()},
            errorMessage = "Failed to accept product"
        )
    }

    suspend fun denyProduct(denyProduct: DenyProduct): Int? {
        return safeApiCall(
            call = {api.denyProductAsync(denyProduct).await()},
            errorMessage = "Failed to deny product"
        )
    }

    suspend fun submitMarketProduct(submitProduct: SubmitProduct): Product? {
        return safeApiCall(
            call = {api.submitMarketProductAsync(submitProduct).await()},
            errorMessage = "Failed to submit product"
        )
    }

    suspend fun viewProduct(id: Int): Int? {
        return safeApiCall(
            call = {api.viewProductAsync(id).await()},
            errorMessage = "Failed to view product"
        )
    }

    suspend fun deleteProduct(id: Int): Int? {
        return safeApiCall(
            call = {api.deleteProductAsync(id).await()},
            errorMessage = "Failed to delete product"
        )
    }
}