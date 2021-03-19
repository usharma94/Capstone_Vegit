package sheridan.sharmupm.vegit_capstone.ui.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import sheridan.sharmupm.vegit_capstone.data.UserRepository
import sheridan.sharmupm.vegit_capstone.models.UserModel

class UserProfileViewModel : ViewModel() {

    private var userRepository : UserRepository ?= null
    var userModelListLiveData : LiveData<List<UserModel>>?= null

    init {
        userRepository = UserRepository()
        userModelListLiveData = MutableLiveData()
    }

    fun fetchAllUsers() {
        userModelListLiveData = userRepository?.fetchAllUser()
    }
}