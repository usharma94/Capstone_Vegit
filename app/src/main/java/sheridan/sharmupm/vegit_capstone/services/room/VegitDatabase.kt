package sheridan.sharmupm.vegit_capstone.services.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import sheridan.sharmupm.vegit_capstone.models.DietModel
import sheridan.sharmupm.vegit_capstone.models.UserModel
import sheridan.sharmupm.vegit_capstone.services.roomDao.DietDao
import sheridan.sharmupm.vegit_capstone.services.roomDao.UserDao

@Database(entities = [UserModel::class, DietModel::class], version = 5)
abstract class VegitDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun dietDao(): DietDao

    companion object {
        @Volatile
        private var INSTANCE: VegitDatabase? = null

        fun getInstance(context: Context): VegitDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        VegitDatabase::class.java,
                        "user_database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}