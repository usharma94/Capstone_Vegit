package sheridan.sharmupm.vegit_capstone.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class UserModel (
        @PrimaryKey var id:Int ?= 0,
        var email:String ?= "",
        var password:String ?= "",
        var firstName:String ?= "",
        var lastName:String ?= "",
        var manufacturer: Boolean ?= false,
        var admin: Boolean ?= false,
        var last_updated:Long = System.currentTimeMillis()
)