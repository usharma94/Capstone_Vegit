package sheridan.sharmupm.vegit_capstone

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import sheridan.sharmupm.vegit_capstone.helpers.getUserFromCache
import sheridan.sharmupm.vegit_capstone.helpers.isUserCached
import sheridan.sharmupm.vegit_capstone.models.login.LoggedInUserView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        visibilityNavElements()
    }

    private fun getAccountType(): Int {
        if (isUserCached()) {
            val cachedUser = getUserFromCache() as LoggedInUserView
            if (cachedUser.manufacturer == true) {
                return 1
            } else if (cachedUser.admin == true) {
                return 2
            }
        }
        return 0
    }

    //remove bottom appbar from login page
    private fun visibilityNavElements() {
        val navController = findNavController(R.id.nav_host_fragment)
        val navViewUser: BottomNavigationView = findViewById(R.id.nav_view_user)
        val navViewManufacturer: BottomNavigationView = findViewById(R.id.nav_view_manufacturer)
        val navViewAdmin: BottomNavigationView = findViewById(R.id.nav_view_admin)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.loginFragment -> {
                    navViewUser.visibility = View.GONE
                    navViewManufacturer.visibility = View.GONE
                    navViewAdmin.visibility = View.GONE
                }
                else -> {
                    when (getAccountType()) {
                        1 -> {
                            println("MANUFACTURER")
                            navViewUser.visibility = View.INVISIBLE
                            navViewManufacturer.visibility = View.VISIBLE
                            navViewAdmin.visibility = View.INVISIBLE
                            setupNavBar(navController, navViewManufacturer, setOf(
                                    R.id.navigation_home,
                                    R.id.approvedProduct,
                                    R.id.deniedProduct,
                                    R.id.adminProductFragment,
                                    R.id.userProfile))
                        }
                        2 -> {
                            println("ADMIN")
                            navViewUser.visibility = View.INVISIBLE
                            navViewManufacturer.visibility = View.INVISIBLE
                            navViewAdmin.visibility = View.VISIBLE
                            setupNavBar(navController, navViewAdmin, setOf(
                                    R.id.navigation_home,
                                    R.id.adminProductFragment,
                                    R.id.userProfile))
                        }
                        else -> {
                            println("USER")
                            navViewUser.visibility = View.VISIBLE
                            navViewManufacturer.visibility = View.INVISIBLE
                            navViewAdmin.visibility = View.INVISIBLE
                            setupNavBar(navController, navViewUser, setOf(
                                    R.id.navigation_home,
                                    R.id.cameraFragment,
                                    R.id.navigation_groceryList,
                                    R.id.searchFragment,
                                    R.id.userProfile))
                        }
                    }
                }
            }
        }
    }

    private fun setupNavBar(
        navController: NavController,
        navigationView: BottomNavigationView,
        navIds: Set<Int>
    ) {
        val appBarConfiguration = AppBarConfiguration(navIds)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navigationView.setupWithNavController(navController)
    }
}