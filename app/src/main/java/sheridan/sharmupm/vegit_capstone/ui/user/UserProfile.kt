package sheridan.sharmupm.vegit_capstone.ui.user


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import sheridan.sharmupm.vegit_capstone.R
import sheridan.sharmupm.vegit_capstone.controllers.user.UserProfileViewModel
import sheridan.sharmupm.vegit_capstone.helpers.isUserCached
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
        val scanHistoryButton = view.findViewById<Button>(R.id.btnHistory)
//        val customDietButton = view.findViewById<Button>(R.id.btnCustomDiet)
        val profileImage = view.findViewById<ImageView>(R.id.profile_img)
        val updateAccountCard = view.findViewById<CardView>(R.id.upgradeAccountCard)
        val submitNewProductButton = view.findViewById<Button>(R.id.btnSubmitProduct)
        val adminAcceptProductButton = view.findViewById<Button>(R.id.btnAdminApprove)

        if (isUserCached()) {
//            enabling and disabling profile elements for logged in vs. logged out user. Will refactor later.
            logoutButton.isVisible = true
            updateAccountCard.isVisible = false
            scanHistoryButton.isEnabled = true
//            customDietButton.isEnabled = true
            logoutUser(logoutButton)

            userProfileViewModel.fetchUser()
        } else {
            // disable stuff here for non logged in users
            logoutButton.isVisible = false
            scanHistoryButton.isEnabled = false
//            customDietButton.isEnabled = false
            email.isVisible = false
            profileImage.isVisible = false
        }

        userProfileViewModel.loggedInUser.observe(viewLifecycleOwner,
            { user ->
                if (user != null) {
                    updateUiWithUser(email, user)
                    if (user.manufacturer == true) {
                        submitProduct(submitNewProductButton)
                        scanHistoryButton.isVisible = false
                    } else if (user.admin == true) {
                        adminProduct(adminAcceptProductButton)
                        scanHistoryButton.isVisible = false
                    }
                } else {
                    println("error fetching user")
                }
            })
    }

    private fun adminProduct(adminAcceptProductButton: Button) {
        adminAcceptProductButton.isEnabled = true
        adminAcceptProductButton.isVisible = true
        adminAcceptProductButton.setOnClickListener {
            this.findNavController().navigate(R.id.action_userProfile_to_adminProductFragment)
        }
    }

    private fun submitProduct(submitNewProductButton: Button) {
        submitNewProductButton.isEnabled = true
        submitNewProductButton.isVisible = true
        submitNewProductButton.setOnClickListener {
            this.findNavController().navigate(R.id.action_userProfile_to_submitProduct)
        }
    }

    private fun logoutUser(logoutButton: Button){
        logoutButton.setOnClickListener {
            userProfileViewModel.logoutUser()
            this.findNavController().navigate(R.id.action_global_nav_login)
        }
    }

    private fun updateUiWithUser(email: TextView, model: LoggedInUserView) {
        email.text = model.email
    }
}

// android:drawableLeft="@android:drawable/ic_menu_search"