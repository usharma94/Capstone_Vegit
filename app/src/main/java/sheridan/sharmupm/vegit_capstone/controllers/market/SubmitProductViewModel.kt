package sheridan.sharmupm.vegit_capstone.controllers.market

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import sheridan.sharmupm.vegit_capstone.models.ingredients.IngredientName
import sheridan.sharmupm.vegit_capstone.models.products.Product
import sheridan.sharmupm.vegit_capstone.models.products.SubmitProduct
import sheridan.sharmupm.vegit_capstone.services.network.APIClient
import sheridan.sharmupm.vegit_capstone.services.repository.ProductRepository
import kotlin.coroutines.CoroutineContext

class SubmitProductViewModel : ViewModel() {
    private val parentJob = Job()

    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Default

    private val scope = CoroutineScope(coroutineContext)

    private val productRepository : ProductRepository = ProductRepository(APIClient.apiInterface)

    val productResponse = MutableLiveData<Product>()

    fun submitProduct(name: String, url: String, dietType: Int, category: String, ingredients: String) {
        val product = SubmitProduct(
            name,
            dietType(dietType),
            category,
            url,
            "null",
            "null",
            extractIngredients(ingredients)
        )

        scope.launch {
            val response = productRepository.submitMarketProduct(product)
            productResponse.postValue(response)
        }
    }

    private fun dietType(dietType: Int): Int {
        if (dietType == 1) return 1
        if (dietType == 2) return 3
        if (dietType == 3) return 6
        return 5
    }

    private fun extractIngredients(ingredientsText: String): List<IngredientName> {
        var ingredientRaw = ""
        val ingredientNameList: MutableList<IngredientName> = mutableListOf()

        if (!checkNull(ingredientsText, ":", false)) {
            // grabbing text only after "ingredients:"
            ingredientRaw = ingredientsText.substring(ingredientsText.indexOf(":") + 1)
        }

        ingredientRaw = ingredientRaw.toLowerCase()

        // remove special characters from string
        ingredientRaw = ingredientRaw.replace(Regex("[*\"/]"), "")

        // remove new line with space
        ingredientRaw = ingredientRaw.replace(Regex("[\n\r]"), " ")

        // split string by delimiters
        val ingredientList = ingredientRaw.split(",", "(", ")", "[", "]", " and ", " or ", ".", ":")

        // creating list of ingredient name objects
        for (ingredient in ingredientList) {
            if (ingredient.trim() != "" && ingredient.trim().length <= 40) {
                ingredientNameList.add(IngredientName(ingredient.trim()))
            }
        }

        if (ingredientNameList.count() < 1) {
            return emptyList()
        }

        return ingredientNameList
    }

    private fun checkNull(rawString: String, delimiter: String, lastIndex: Boolean) : Boolean {
        if (lastIndex)
            if (rawString.lastIndexOf(delimiter) < 0) return true
            else
                if (rawString.indexOf(delimiter) < 0) return true
        return false
    }

    fun cancelRequest() = coroutineContext.cancel()
}