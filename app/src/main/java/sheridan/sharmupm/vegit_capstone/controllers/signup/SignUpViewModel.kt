package sheridan.sharmupm.vegit_capstone.controllers.signup

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import sheridan.sharmupm.vegit_capstone.App
import sheridan.sharmupm.vegit_capstone.R
import sheridan.sharmupm.vegit_capstone.models.login.LoggedInUserView
import sheridan.sharmupm.vegit_capstone.models.signup.SignUpFormState
import sheridan.sharmupm.vegit_capstone.models.signup.SignUpModel
import sheridan.sharmupm.vegit_capstone.services.cache.CacheClient
import sheridan.sharmupm.vegit_capstone.services.network.APIClient
import sheridan.sharmupm.vegit_capstone.services.repository.SignUpRepository
import kotlin.coroutines.CoroutineContext

class SignUpViewModel : ViewModel() {

    private val parentJob = Job()

    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Default

    private val scope = CoroutineScope(coroutineContext)

    private val repository : SignUpRepository = SignUpRepository(APIClient.apiInterface, CacheClient.cache, App.db.userDao())

    private val _signUpForm = MutableLiveData<SignUpFormState>()
    val signUpFormState: LiveData<SignUpFormState> = _signUpForm

    val loggedInUser = MutableLiveData<LoggedInUserView>()

    fun signUpUser(email: String, password: String, firstName: String, lastName: String) {
        val signUp = SignUpModel()
        signUp.email = email
        signUp.password = password
        signUp.firstName = firstName
        signUp.lastName = lastName

        scope.launch {
            val user = repository.signUpUser(signUp)
            loggedInUser.postValue(user)
        }
    }

    fun signUpDataChanged(email: String, password: String, firstName: String, lastName: String) {
        if (!isEmailValid(email)) {
            _signUpForm.value = SignUpFormState(emailError = R.string.invalid_username)
        } else if (!isPasswordValid(password)) {
            _signUpForm.value = SignUpFormState(passwordError = R.string.invalid_password)
        } else if (!isNameValid(firstName)) {
            _signUpForm.value = SignUpFormState(firstNameError = R.string.invalid_firstName)
        } else if (!isNameValid(lastName)) {
            _signUpForm.value = SignUpFormState(lastNameError = R.string.invalid_lastName)
        } else {
            _signUpForm.value = SignUpFormState(isDataValid = true)
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

    private fun isNameValid(name: String): Boolean {
        return name.length > 2
    }

    fun cancelRequest() = coroutineContext.cancel()
}