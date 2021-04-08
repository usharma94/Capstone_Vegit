package sheridan.sharmupm.vegit_capstone.ui.signup

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import sheridan.sharmupm.vegit_capstone.R
import sheridan.sharmupm.vegit_capstone.controllers.signup.SignUpViewModel
import sheridan.sharmupm.vegit_capstone.models.login.LoggedInUserView


class SignUpFragment : Fragment() {

    private lateinit var signUpViewModel: SignUpViewModel

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_signup, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        signUpViewModel = ViewModelProvider(this).get(SignUpViewModel::class.java)
        // TODO: Use the ViewModel

        val emailEditText = view.findViewById<EditText>(R.id.email)
        val passwordEditText = view.findViewById<EditText>(R.id.password)
        val firstNameEditText = view.findViewById<EditText>(R.id.first_name)
        val lastNameEditText = view.findViewById<EditText>(R.id.last_name)
        val signUpButton = view.findViewById<Button>(R.id.btn_sign_up)
        val loadingProgressBar = view.findViewById<ProgressBar>(R.id.signUp_loading)

        signUpViewModel.signUpFormState.observe(viewLifecycleOwner,
                Observer { signUpFormState ->
                    if (signUpFormState == null) {
                        return@Observer
                    }
                    signUpButton.isEnabled = signUpFormState.isDataValid
                    signUpFormState.emailError?.let {
                        emailEditText.error = getString(it)
                    }
                    signUpFormState.passwordError?.let {
                        passwordEditText.error = getString(it)
                    }
                    signUpFormState.firstNameError?.let {
                        firstNameEditText.error = getString(it)
                    }
                    signUpFormState.lastNameError?.let {
                        lastNameEditText.error = getString(it)
                    }
                })

        signUpViewModel.loggedInUser.observe(viewLifecycleOwner,
                { user ->
                    loadingProgressBar.visibility = View.GONE
                    signUpButton.isEnabled = true
                    if (user != null) {
                        updateUiWithUser(user)
                    } else {
                        showSignUpFailed()
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
                signUpViewModel.signUpDataChanged(
                        emailEditText.text.toString(),
                        passwordEditText.text.toString(),
                        firstNameEditText.text.toString(),
                        lastNameEditText.text.toString()
                )
            }
        }

        emailEditText.addTextChangedListener(afterTextChangedListener)
        passwordEditText.addTextChangedListener(afterTextChangedListener)
        firstNameEditText.addTextChangedListener(afterTextChangedListener)
        lastNameEditText.addTextChangedListener(afterTextChangedListener)

        passwordEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                signUpViewModel.signUpUser(
                        emailEditText.text.toString(),
                        passwordEditText.text.toString(),
                        firstNameEditText.text.toString(),
                        lastNameEditText.text.toString()
                )
            }
            false
        }

        signUpButton.setOnClickListener {
            loadingProgressBar.visibility = View.VISIBLE
            signUpButton.isEnabled = false
            signUpViewModel.signUpUser(
                    emailEditText.text.toString(),
                    passwordEditText.text.toString(),
                    firstNameEditText.text.toString(),
                    lastNameEditText.text.toString()
            )
        }
    }

    private fun updateUiWithUser(model: LoggedInUserView) {
        val welcome = getString(R.string.welcome) + " " +  model.firstName
        // TODO : initiate successful logged in experience
        val appContext = context?.applicationContext ?: return
        Toast.makeText(appContext, welcome, Toast.LENGTH_LONG).show()
        this.findNavController().navigate(R.id.navigation_home)
    }

    private fun showSignUpFailed() {
        val appContext = context?.applicationContext ?: return
        Toast.makeText(appContext, R.string.sign_up_failed, Toast.LENGTH_LONG).show()
    }
}