package sheridan.sharmupm.vegit_capstone.models

import androidx.annotation.DrawableRes
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class DietModel(
    @PrimaryKey var id:Int ?= 0,
    var isSelected: Boolean = false,
    @DrawableRes
    var dietImage: Int?,
    var dietName: String? = null,
    var dietDescription: String? = null,
    var dietType: Int?
)