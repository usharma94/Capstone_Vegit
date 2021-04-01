package sheridan.sharmupm.vegit_capstone.controllers.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is the Home Fragment"
    }
    val text: LiveData<String> = _text
}