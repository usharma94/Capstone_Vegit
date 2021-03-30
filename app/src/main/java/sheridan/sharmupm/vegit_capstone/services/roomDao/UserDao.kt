package sheridan.sharmupm.vegit_capstone.services.roomDao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import androidx.room.Update
import sheridan.sharmupm.vegit_capstone.models.UserModel

@Dao
interface UserDao {

    @Query("SELECT * FROM UserModel LIMIT 1")
    fun getUser(): UserModel

    @Insert(onConflict = REPLACE)
    fun insertUser(user: UserModel)

    @Update
    fun updateUser(user: UserModel)

    @Query("DELETE FROM UserModel")
    fun deleteUser()

    @Query("SELECT COUNT(*) FROM UserModel WHERE last_updated >= :timeout LIMIT 1")
    fun hasUser(timeout: Long): Int

    @Query("SELECT COUNT(*) FROM UserModel WHERE id = :id AND last_updated >= :timeout LIMIT 1")
    fun hasUserWithId(id: Int, timeout: Long): Int
}