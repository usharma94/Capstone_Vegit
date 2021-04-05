package sheridan.sharmupm.vegit_capstone.services.network

import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import sheridan.sharmupm.vegit_capstone.models.ingredients.Ingredient
import sheridan.sharmupm.vegit_capstone.models.ingredients.IngredientName
import sheridan.sharmupm.vegit_capstone.models.login.LoggedInUserView
import sheridan.sharmupm.vegit_capstone.models.login.LoginModel

interface ApiInterface {

    // USERS

    @GET("users/get/{id}")
    fun fetchUserAsync(@Path("id") id:Int): Deferred<Response<LoggedInUserView>>

    @POST("users/login")
    fun loginUserAsync(@Body loginModel: LoginModel): Deferred<Response<LoggedInUserView>>

    // INGREDIENTS

    @GET("ingredients/getnames")
    fun fetchIngredientNamesAsync(): Deferred<Response<List<IngredientName>>>

    @POST("ingredients/search/single")
    fun searchIngredientsAsync(@Body ingredientName: IngredientName): Deferred<Response<Ingredient>>

    @POST("ingredients/search/list")
    fun searchIngredientListAsync(@Body ingredientNames: List<IngredientName>): Deferred<Response<List<Ingredient>>>
}