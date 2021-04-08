package sheridan.sharmupm.vegit_capstone.services.roomDao

import androidx.room.*
import sheridan.sharmupm.vegit_capstone.models.DietModel

@Dao
interface DietDao {

    @Query("SELECT * FROM DietModel LIMIT 1")
    fun getDiet(): DietModel

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertDiet(diet: DietModel)

    @Update
    fun updateDiet(diet: DietModel)

    @Query("DELETE FROM DietModel")
    fun deleteDiet()
}