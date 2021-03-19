package sheridan.sharmupm.vegit_capstone

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import sheridan.sharmupm.vegit_capstone.models.UserModel
import sheridan.sharmupm.vegit_capstone.ui.user.UserProfileViewModel

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(setOf(
                R.id.navigation_home, R.id.navigation_classifyproducts, R.id.navigation_groceryList, R.id.navigation_diet, R.id.userProfile))
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        // TESTING PURPOSE
        val vm = ViewModelProvider(this)[UserProfileViewModel::class.java]
        vm.fetchAllUsers()
        vm.userModelListLiveData?.observe(this, Observer {
            if (it != null){
                println(it as ArrayList<UserModel>)
            }else{
                println("Something went wrong")
            }
        })
    }
}