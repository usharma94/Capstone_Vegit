package sheridan.sharmupm.vegit_capstone.models

import androidx.annotation.DrawableRes

data class DietModel(
    var isSelected: Boolean = false,
    @DrawableRes
    var dietImage: Int?,
    var dietName: String? = null,
    var dietDescription: String? = null,
)