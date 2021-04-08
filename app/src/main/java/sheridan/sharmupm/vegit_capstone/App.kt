package sheridan.sharmupm.vegit_capstone

import android.app.Application
import sheridan.sharmupm.vegit_capstone.services.room.VegitDatabase

class App : Application() {

    companion object {
        lateinit var db : VegitDatabase
    }

    override fun onCreate() {
        super.onCreate()
        db = VegitDatabase.getInstance(applicationContext)
    }
}