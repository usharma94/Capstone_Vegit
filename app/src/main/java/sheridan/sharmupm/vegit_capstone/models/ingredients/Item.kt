package sheridan.sharmupm.vegit_capstone.models.ingredients

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Item(
    var name:String,
    var category:String,
    var imageUrl:String

): Parcelable