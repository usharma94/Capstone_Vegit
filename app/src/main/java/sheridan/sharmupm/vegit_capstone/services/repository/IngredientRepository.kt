package sheridan.sharmupm.vegit_capstone.services.repository

import sheridan.sharmupm.vegit_capstone.models.ingredients.Ingredient
import sheridan.sharmupm.vegit_capstone.models.ingredients.IngredientName
import sheridan.sharmupm.vegit_capstone.models.login.ClassifyModel
import sheridan.sharmupm.vegit_capstone.services.network.ApiInterface

class IngredientRepository(private val api: ApiInterface) : BaseRepository() {
    suspend fun fetchIngredientNames(): List<IngredientName>? {
        return safeApiCall(
                call = {api.fetchIngredientNamesAsync().await()},
                errorMessage = "No data found"
        )
    }

    suspend fun searchIngredients(ingredientName: IngredientName): Ingredient? {
        return safeApiCall(
                call = {api.searchIngredientsAsync(ingredientName).await()},
                errorMessage = "No data found"
        )
    }

    suspend fun searchIngredientList(classifyModel: ClassifyModel): List<Ingredient>? {
        return safeApiCall(
                call = {api.searchIngredientListAsync(classifyModel).await()},
                errorMessage = "No data found"
        )
    }

    suspend fun fetchSafeIngredients(diet: Int): List<Ingredient>? {
        return safeApiCall(
            call = {api.fetchSafeIngredientsAsync(diet).await()},
            errorMessage = "No data found"
        )
    }

    suspend fun fetchNotSafeIngredients(diet: Int): List<Ingredient>? {
        return safeApiCall(
            call = {api.fetchNotSafeIngredientsAsync(diet).await()},
            errorMessage = "No data found"
        )
    }

    suspend fun fetchIngredientsToClassify(): List<Ingredient>? {
        return safeApiCall(
            call = {api.fetchIngredientsToClassifyAsync().await()},
            errorMessage = "No data found"
        )
    }

    suspend fun updateClassificationAsync(id: Int, diet: Int): Int? {
        return safeApiCall(
            call = {api.updateClassificationAsync(id, diet).await()},
            errorMessage = "Failed to update diet"
        )
    }
}