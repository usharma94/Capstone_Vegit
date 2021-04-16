package sheridan.sharmupm.vegit_capstone.controllers.diet


import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import sheridan.sharmupm.vegit_capstone.R
import sheridan.sharmupm.vegit_capstone.helpers.DietTypes
import sheridan.sharmupm.vegit_capstone.helpers.getDiet
import sheridan.sharmupm.vegit_capstone.helpers.setDiet
import sheridan.sharmupm.vegit_capstone.models.DietModel
import kotlin.coroutines.CoroutineContext

class DietViewModel : ViewModel() {

    private val parentJob = Job()

    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Default

    private val scope = CoroutineScope(coroutineContext)

    fun setSelectedDiet(diet: DietModel) {
        scope.launch {
            setDiet(diet)
        }
    }

    fun populateDietList() : List<DietModel> {
        val dietList = mutableListOf<DietModel>()

        scope.launch {
            dietList.add(DietModel(0, false, false, R.drawable.vegetarian, "Vegetarian",
                    "This refers to a diet where egg and dairy products can be consumed", DietTypes.VEGETARIAN.value))
            dietList.add(DietModel(1, false, false, R.drawable.vegan, "Vegan",
                    "This refers to a diet where no animal products are consumed", DietTypes.VEGAN.value))
            dietList.add(DietModel(2, false, true, R.drawable.custom, "Custom",
                    "Custom Diet", -1))

            val diet = getDiet()
            if (diet != null) {
                dietList[diet.id!!] = diet
            }
        }

        return dietList
    }
}