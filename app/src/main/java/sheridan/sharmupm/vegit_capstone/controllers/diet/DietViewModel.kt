package sheridan.sharmupm.vegit_capstone.controllers.diet


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DietViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Please select your diet"
    }
    val text: LiveData<String> = _text
}