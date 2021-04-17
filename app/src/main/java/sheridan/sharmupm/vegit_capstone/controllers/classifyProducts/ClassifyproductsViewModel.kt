package sheridan.sharmupm.vegit_capstone.controllers.classifyProducts

import android.util.SparseArray
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.vision.text.TextBlock
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import sheridan.sharmupm.vegit_capstone.models.ingredients.Ingredient
import sheridan.sharmupm.vegit_capstone.models.ingredients.IngredientName
import sheridan.sharmupm.vegit_capstone.services.network.APIClient
import sheridan.sharmupm.vegit_capstone.services.repository.IngredientRepository
import kotlin.coroutines.CoroutineContext

class ClassifyproductsViewModel : ViewModel() {

    private val parentJob = Job()

    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Default

    private val scope = CoroutineScope(coroutineContext)

    private val repository : IngredientRepository = IngredientRepository(APIClient.apiInterface)

    val ingredientResults = MutableLiveData<List<Ingredient>>()

    private val _text = MutableLiveData<String>().apply {
        value = "This is dashboard Fragment"
    }
    val text: LiveData<String> = _text

    // must pass in a list of IngredientName data objects
    fun searchIngredientList(ingredientNames: List<IngredientName>) {
        scope.launch {
            val results = repository.searchIngredientList(ingredientNames)
            ingredientResults.postValue(results)
        }
    }

    fun extractIngredientText(rawData: SparseArray<TextBlock>) : List<IngredientName>? {
        val sb = StringBuilder()
        var ingredientRaw = ""
        val ingredientNameList: MutableList<IngredientName> = mutableListOf()

        // creating single string from scanned text
        for (i in 0 until rawData.size()) {
            val myItem = rawData.valueAt(i).value
            sb.append(myItem)
        }

        if (checkNull(ingredientRaw, ":", false)) return null

        // grabbing text only after "ingredients:"
        ingredientRaw = sb.substring(sb.indexOf(":") + 1)

        // stopping at first occurance of a .
        // may or may not be best way to extract up to end of ingredient text only
        // From research, found most ingredients have a . at end of ingredients
        if (checkNull(ingredientRaw, ".", true)) return null

        ingredientRaw = ingredientRaw.substring(0, ingredientRaw.lastIndexOf("."))

        // remove special characters from string
        ingredientRaw = ingredientRaw.replace(Regex("[*\"/]"), "")

        // remove new line with space
        ingredientRaw = ingredientRaw.replace(Regex("[\n\r]"), " ")

        // split string by delimiters
        val ingredientList = ingredientRaw.split(",", "(", ")", "[", "]", " and ", " or ")

        // creating list of ingredient name objects
        for (ingredient in ingredientList) {
            if (ingredient.trim() != "") {
                ingredientNameList.add(IngredientName(ingredient.trim()))
            }
        }

        if (ingredientNameList.count() < 1) {
            return null
        }
        return ingredientNameList
    }

    private fun checkNull(rawString: String, delimiter: String, lastIndex: Boolean) : Boolean {
        if (lastIndex) {
            if (rawString.lastIndexOf(delimiter) < 0) {
                println("ERROR NO INGREDIENT DATA WAS ABLE TO BE EXTRACTED")
                return true
            }
        } else {
            if (rawString.indexOf(delimiter) < 0) {
                println("ERROR NO INGREDIENT DATA WAS ABLE TO BE EXTRACTED")
                return true
            }
        }
        return false
    }
}