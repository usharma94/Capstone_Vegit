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
}