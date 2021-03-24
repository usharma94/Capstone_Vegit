package sheridan.sharmupm.vegit_capstone.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import sheridan.sharmupm.vegit_capstone.data.UserRepository
import sheridan.sharmupm.vegit_capstone.models.LoginModel
import sheridan.sharmupm.vegit_capstone.models.UserModel

class LoginViewModel :ViewModel(){
    private var userRepository : UserRepository?= null
    var loginModelListLiveData : LiveData<UserModel>?= null

    init {
        userRepository = UserRepository()
        loginModelListLiveData = MutableLiveData()
    }

    fun userPost(loginModel: LoginModel){
        println(loginModel)
        loginModelListLiveData = userRepository?.userPost(loginModel)
    }
}