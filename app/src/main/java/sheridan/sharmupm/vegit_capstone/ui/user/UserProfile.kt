package sheridan.sharmupm.vegit_capstone.ui.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import sheridan.sharmupm.vegit_capstone.R
import sheridan.sharmupm.vegit_capstone.controllers.user.UserProfileViewModel
import sheridan.sharmupm.vegit_capstone.models.login.LoggedInUserView

class UserProfile : Fragment() {

    private lateinit var userProfileViewModel: UserProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.user_profile_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        userProfileViewModel = ViewModelProvider(this).get(UserProfileViewModel::class.java)
        // TODO: Use the ViewModel

        val email = view.findViewById<TextView>(R.id.profile_email)
        val logoutButton = view.findViewById<Button>(R.id.btnProfileLogout)

        userProfileViewModel.fetchUser()

        userProfileViewModel.loggedInUser.observe(viewLifecycleOwner,
            { user ->
                if (user != null) {
                    updateUiWithUser(email, user)
                }
                else {
                    println("error fetching user")
                }
            })

        logoutButton.setOnClickListener {
            userProfileViewModel.logoutUser()
            this.findNavController().navigate(R.id.action_global_nav_login)
        }
    }

    private fun updateUiWithUser(email: TextView, model: LoggedInUserView) {
        email.text = model.email
    }
}