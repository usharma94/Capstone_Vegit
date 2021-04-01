package sheridan.sharmupm.vegit_capstone.services.network

import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import sheridan.sharmupm.vegit_capstone.models.ingredients.Ingredient
import sheridan.sharmupm.vegit_capstone.models.ingredients.SearchSingle
import sheridan.sharmupm.vegit_capstone.models.login.LoggedInUserView
import sheridan.sharmupm.vegit_capstone.models.login.LoginModel

interface ApiInterface {

    // USERS

    //@GET("users/getall")
    //fun fetchAllUsers(): Call<List<UserModel>>

    @GET("users/get/{id}")
    fun fetchUserAsync(@Path("id") id:Int): Deferred<Response<LoggedInUserView>>

    @POST("users/login")
    fun loginUserAsync(@Body loginModel: LoginModel): Deferred<Response<LoggedInUserView>>

    // INGREDIENTS

    @GET("ingredients/getnames")
    fun fetchIngredientNamesAsync(): Deferred<Response<List<SearchSingle>>>

    @POST("ingredients/search/single")
    fun searchIngredientsAsync(@Body searchSingle: SearchSingle): Deferred<Response<Ingredient>>
}