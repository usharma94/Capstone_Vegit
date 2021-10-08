package sheridan.sharmupm.vegit_capstone.services.network

import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.*
import sheridan.sharmupm.vegit_capstone.models.ingredients.Ingredient
import sheridan.sharmupm.vegit_capstone.models.ingredients.IngredientName
import sheridan.sharmupm.vegit_capstone.models.login.ClassifyModel
import sheridan.sharmupm.vegit_capstone.models.login.LoggedInUserView
import sheridan.sharmupm.vegit_capstone.models.login.LoginModel
import sheridan.sharmupm.vegit_capstone.models.products.Product
import sheridan.sharmupm.vegit_capstone.models.signup.SignUpModel

interface ApiInterface {

    // USERS

    @GET("users/get/{id}")
    fun fetchUserAsync(@Path("id") id:Int): Deferred<Response<LoggedInUserView>>

    @POST("users/login")
    fun loginUserAsync(@Body loginModel: LoginModel): Deferred<Response<LoggedInUserView>>

    @POST("users/add")
    fun signUpUserAsync(@Body signUpModel: SignUpModel): Deferred<Response<LoggedInUserView>>

    // INGREDIENTS

    @GET("ingredients/getnames")
    fun fetchIngredientNamesAsync(): Deferred<Response<List<IngredientName>>>

    @POST("ingredients/search/single")
    fun searchIngredientsAsync(@Body ingredientName: IngredientName): Deferred<Response<Ingredient>>

    @POST("ingredients/search/list")
    fun searchIngredientListAsync(@Body classifyModel: ClassifyModel): Deferred<Response<List<Ingredient>>>

    // PRODUCTS

    @GET("products/get-product-appoval")
    fun fetchApproveProductsAsync(): Deferred<Response<List<Product>>>

    @POST("products/approve/{id}")
    fun acceptProductAsync(@Path("id") id:Int): Deferred<Response<Int>>

    @DELETE("products/delete/{id}")
    fun denyProductAsync(@Path("id") id:Int): Deferred<Response<Int>>
}