package sheridan.sharmupm.vegit_capstone.controllers.diet


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import sheridan.sharmupm.vegit_capstone.models.DietModel
import sheridan.sharmupm.vegit_capstone.models.ingredients.Ingredient
import kotlin.coroutines.CoroutineContext

class DietViewModel : ViewModel() {
    private val parentJob = Job()

    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Default

    private val scope = CoroutineScope(coroutineContext)

    private val _text = MutableLiveData<String>().apply {
        value = "Please select your diet"
    }
    val text: LiveData<String> = _text
    val dietSelection = MutableLiveData<List<DietModel>>()


}