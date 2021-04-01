package sheridan.sharmupm.vegit_capstone.services.repository

import sheridan.sharmupm.vegit_capstone.models.ingredients.Ingredient
import sheridan.sharmupm.vegit_capstone.models.ingredients.SearchSingle
import sheridan.sharmupm.vegit_capstone.services.network.ApiInterface

class IngredientRepository(private val api: ApiInterface) : BaseRepository() {
    suspend fun searchIngredients(searchSingle: SearchSingle): Ingredient? {
            return safeApiCall(
                    call = {api.searchIngredientsAsync(searchSingle).await()},
                    errorMessage = "No data found"
            )
        }
}