package sheridan.sharmupm.vegit_capstone.ui.login

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import sheridan.sharmupm.vegit_capstone.R
import sheridan.sharmupm.vegit_capstone.controllers.login.LoginViewModel
import sheridan.sharmupm.vegit_capstone.models.login.LoggedInUserView


class LoginFragment : Fragment() {

    private lateinit var loginViewModel: LoginViewModel


    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loginViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        // TODO: Use the ViewModel

        loginViewModel.isUserLoggedIn()

        val emailEditText = view.findViewById<EditText>(R.id.et_email)
        val passwordEditText = view.findViewById<EditText>(R.id.et_password)
        val loginButton = view.findViewById<Button>(R.id.btn_login)
        val rememberMe = view.findViewById<CheckBox>(R.id.saveLoginCheckBox)
//        val skipSignUpButton = view.findViewById<Button>(R.id.btn_skip_sign_up)
        val signUpButton = view.findViewById<Button>(R.id.btn_sign_up)
        val loadingProgressBar = view.findViewById<ProgressBar>(R.id.loading)

        loginViewModel.loginFormState.observe(viewLifecycleOwner,
            Observer { loginFormState ->
                if (loginFormState == null) {
                    return@Observer
                }
                loginButton.isEnabled = loginFormState.isDataValid
                loginFormState.emailError?.let {
                    emailEditText.error = getString(it)
                }
                loginFormState.passwordError?.let {
                    passwordEditText.error = getString(it)
                }
            })

        loginViewModel.loggedInUser.observe(viewLifecycleOwner,
            { user ->
                loadingProgressBar.visibility = View.GONE
                loginButton.isEnabled = true
                if (user != null) {
                    updateUiWithUser(user)
                } else {
                    showLoginFailed()
                }
            })

        val afterTextChangedListener = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // ignore
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // ignore
            }

            override fun afterTextChanged(s: Editable) {
                loginViewModel.loginDataChanged(
                    emailEditText.text.toString(),
                    passwordEditText.text.toString()
                )
            }
        }

        emailEditText.addTextChangedListener(afterTextChangedListener)
        passwordEditText.addTextChangedListener(afterTextChangedListener)

        passwordEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                loginViewModel.loginUser(
                    emailEditText.text.toString(),
                    passwordEditText.text.toString(),
                    rememberMe.isChecked
                )
            }
            false
        }

        loginButton.setOnClickListener {
            loadingProgressBar.visibility = View.VISIBLE
            loginButton.isEnabled = false
            loginViewModel.loginUser(
                emailEditText.text.toString(),
                passwordEditText.text.toString(),
                rememberMe.isChecked
            )
        }

        findNavController()

        signUpButton.setOnClickListener {
            this.findNavController().navigate(R.id.action_loginFragment_to_signUpFragment)
        }

//        skipSignUpButton.setOnClickListener {
//            this.findNavController().navigate(R.id.action_loginFragment_to_navigation_diet)
//        }
    }

    private fun updateUiWithUser(model: LoggedInUserView) {
        val welcome = getString(R.string.welcome) + " " +  model.firstName
        // TODO : initiate successful logged in experience
        val appContext = context?.applicationContext ?: return
        Toast.makeText(appContext, welcome, Toast.LENGTH_LONG).show()
        if (model.admin == true) {
            this.findNavController().navigate(R.id.adminProductFragment)
        } else if (model.manufacturer == true) {
            this.findNavController().navigate(R.id.approvedProduct)
        } else {
            this.findNavController().navigate(R.id.action_loginFragment_to_navigation_diet)
        }
    }

    private fun showLoginFailed() {
        val appContext = context?.applicationContext ?: return
        Toast.makeText(appContext, R.string.login_failed, Toast.LENGTH_LONG).show()
    }

}