package sheridan.sharmupm.vegit_capstone.controllers.login

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import sheridan.sharmupm.vegit_capstone.App
import sheridan.sharmupm.vegit_capstone.R
import sheridan.sharmupm.vegit_capstone.helpers.isUserInRoom
import sheridan.sharmupm.vegit_capstone.helpers.setUserInCache
import sheridan.sharmupm.vegit_capstone.helpers.toLoggedInUserView
import sheridan.sharmupm.vegit_capstone.models.login.LoggedInUserView
import sheridan.sharmupm.vegit_capstone.models.login.LoginFormState
import sheridan.sharmupm.vegit_capstone.models.login.LoginModel
import sheridan.sharmupm.vegit_capstone.services.cache.CacheClient
import sheridan.sharmupm.vegit_capstone.services.network.APIClient
import sheridan.sharmupm.vegit_capstone.services.repository.LoginRepository
import kotlin.coroutines.CoroutineContext

class LoginViewModel : ViewModel(){

    private val parentJob = Job()

    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Default

    private val scope = CoroutineScope(coroutineContext)

    private val repository : LoginRepository = LoginRepository(APIClient.apiInterface, CacheClient.cache, App.db.userDao())

    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm

    val loggedInUser = MutableLiveData<LoggedInUserView>()

    fun loginUser(email: String, password: String, rememberMe: Boolean) {
        val login = LoginModel()
        login.email = email
        login.password = password
        login.rememberMe = rememberMe

        scope.launch {
            val user = repository.loginUser(login)
            loggedInUser.postValue(user)
        }
    }

    fun isUserLoggedIn() {
        scope.launch {
            val count = isUserInRoom()
            if (count > 0) {
                println("User already logged in, sending to home page")
                // get user from room
                val user = App.db.userDao().getUser().toLoggedInUserView()
                // save user in cache
                setUserInCache(user)

                loggedInUser.postValue(user)
            }
        }
    }

    fun loginDataChanged(email: String, password: String) {
        if (!isEmailValid(email)) {
            _loginForm.value = LoginFormState(emailError = R.string.invalid_username)
        } else if (!isPasswordValid(password)) {
            _loginForm.value = LoginFormState(passwordError = R.string.invalid_password)
        } else {
            _loginForm.value = LoginFormState(isDataValid = true)
        }
    }

    // A placeholder email validation check
    private fun isEmailValid(email: String): Boolean {
        return if (email.contains("@")) {
            Patterns.EMAIL_ADDRESS.matcher(email).matches()
        } else {
            email.isNotBlank()
        }
    }

    // A placeholder password validation check
    private fun isPasswordValid(password: String): Boolean {
        return password.length > 3
    }

    fun cancelRequest() = coroutineContext.cancel()
}