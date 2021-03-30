package sheridan.sharmupm.vegit_capstone

import android.app.Application
import sheridan.sharmupm.vegit_capstone.services.room.UserDatabase

class App : Application() {

    companion object {
        lateinit var db : UserDatabase
    }

    override fun onCreate() {
        super.onCreate()
        db = UserDatabase.getInstance(applicationContext)
    }
}