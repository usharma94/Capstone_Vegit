package sheridan.sharmupm.vegit_capstone.models.groceryList

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Grocery(
    var grocery:String ?= "",
    var due:String ?= "",
    var status:Int ?= 0

):Parcelable