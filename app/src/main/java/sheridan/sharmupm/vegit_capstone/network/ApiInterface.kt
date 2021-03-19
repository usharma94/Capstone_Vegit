package sheridan.sharmupm.vegit_capstone.network

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import sheridan.sharmupm.vegit_capstone.models.UserModel

interface ApiInterface {

    @GET("users/getall")
    fun fetchAllUsers(): Call<List<UserModel>>

    @GET("users/get/{id}")
    fun fetchUser(@Path("id") id:Int):Call<UserModel>
}