package sheridan.sharmupm.vegit_capstone.services.network

import kotlinx.coroutines.Deferred
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import sheridan.sharmupm.vegit_capstone.models.UserModel
import sheridan.sharmupm.vegit_capstone.models.login.LoggedInUserView
import sheridan.sharmupm.vegit_capstone.models.login.LoginModel

interface ApiInterface {

    @GET("users/getall")
    fun fetchAllUsers(): Call<List<UserModel>>

    @GET("users/get/{id}")
    fun fetchUser(@Path("id") id:Int):Call<UserModel>

    @POST("users/login")
    fun loginUserAsync(@Body loginModel: LoginModel): Deferred<Response<LoggedInUserView>>
}