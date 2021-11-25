package sheridan.sharmupm.vegit_capstone.models.groceryList

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Grocery(
    var id:Int?=0,
    var name:String ?= "",
    var due:String ?= "",
    var status:Int ?= 0

):Parcelable