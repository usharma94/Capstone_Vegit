package sheridan.sharmupm.vegit_capstone.controllers.classifyProducts

import android.util.SparseArray
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.vision.text.TextBlock
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import sheridan.sharmupm.vegit_capstone.helpers.DietSafety
import sheridan.sharmupm.vegit_capstone.helpers.determineSafety
import sheridan.sharmupm.vegit_capstone.helpers.getDiet
import sheridan.sharmupm.vegit_capstone.models.ingredients.ClassifyIngredient
import sheridan.sharmupm.vegit_capstone.models.ingredients.Ingredient
import sheridan.sharmupm.vegit_capstone.models.ingredients.IngredientName
import sheridan.sharmupm.vegit_capstone.models.login.ClassifyModel
import sheridan.sharmupm.vegit_capstone.services.network.APIClient
import sheridan.sharmupm.vegit_capstone.services.repository.IngredientRepository
import java.util.*
import kotlin.coroutines.CoroutineContext

class ClassifyproductsViewModel : ViewModel() {

    private val parentJob = Job()

    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Default

    private val scope = CoroutineScope(coroutineContext)

    private val repository : IngredientRepository = IngredientRepository(APIClient.apiInterface)

    val results = MutableLiveData<List<Ingredient>>()
    val ingredientList = MutableLiveData<ArrayList<ClassifyIngredient>>()

    // must pass in a list of IngredientName data objects
    fun searchIngredientList(itemName: String, ingredientNames: List<IngredientName>) {
        scope.launch {
            val classifyModel = ClassifyModel()
            classifyModel.itemName = itemName
            classifyModel.category = "unknown"
            classifyModel.img_url = "unknown"
            classifyModel.searchList = ingredientNames

            val data = repository.searchIngredientList(classifyModel)
            results.postValue(data)
        }
    }

    fun searchBarcodeIngredientList(itemName: String, ingredientNames: List<IngredientName>, category:String, imgUrl:String){
        scope.launch {
            val classifyModel = ClassifyModel()
            classifyModel.itemName = itemName
            classifyModel.category = extractCategory(category)
            classifyModel.img_url = imgUrl
            classifyModel.searchList = ingredientNames

            val data = repository.searchIngredientList(classifyModel)
            results.postValue(data)
        }

    }

    private fun extractCategory(category: String) : String {
        if (category.contains(">")) {
            return category.substringAfterLast(">").trim().toLowerCase(Locale.ROOT)
        }
        return category.trim()
    }

    fun parseResults(ingredients: List<Ingredient>) {
        scope.launch {
            val diet = getDiet()
            if (diet != null) {
                val ingredientStringList = arrayListOf<ClassifyIngredient>()

                for (i in ingredients.indices){
                    when (determineSafety(diet, ingredients[i].diet_type!!)) {
                        DietSafety.SAFE -> ingredientStringList.add(ClassifyIngredient(ingredients[i].name, ingredients[i].diet_name, 1, "#ABEBC6"))
                        DietSafety.CAUTION -> ingredientStringList.add(ClassifyIngredient(ingredients[i].name, ingredients[i].diet_name, 2, "#F9E79F"))
                        DietSafety.AVOID -> ingredientStringList.add(ClassifyIngredient(ingredients[i].name, ingredients[i].diet_name, 3, "#F1948A"))
                        else -> ingredientStringList.add(ClassifyIngredient(ingredients[i].name, ingredients[i].diet_name, 4, "#F1948A"))
                    }
                }

                ingredientList.postValue(ingredientStringList)
            } else {
                println("something went wrong")
            }
        }
    }

    fun extractIngredientText(rawData: SparseArray<TextBlock>) : List<IngredientName>? {
        val sb = StringBuilder()
        val ingredientNameList: MutableList<IngredientName> = mutableListOf()

        // creating single string from scanned text
        for (i in 0 until rawData.size()) {
            val myItem = rawData.valueAt(i).value
            sb.append(myItem)
        }

        println(sb.toString())

        if (checkNull(sb.toString(), ":", false)) return null

        // grabbing text only after "ingredients:"
        var ingredientRaw: String = sb.substring(sb.indexOf(":") + 1)

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
        val ingredientList = ingredientRaw.split(",", "(", ")", "[", "]", " and ", " or ", ".", ":")

        // creating list of ingredient name objects
        for (ingredient in ingredientList) {
            if (ingredient.trim() != "" && ingredient.trim().length <= 40) {
                ingredientNameList.add(IngredientName(ingredient.trim()))
            }
        }

        if (ingredientNameList.count() < 1) {
            return null
        }
        println(ingredientNameList)
        return ingredientNameList
    }

    private fun checkNull(rawString: String, delimiter: String, lastIndex: Boolean) : Boolean {
        if (lastIndex)
            if (rawString.lastIndexOf(delimiter) < 0) return true
        else
            if (rawString.indexOf(delimiter) < 0) return true
        return false
    }
}