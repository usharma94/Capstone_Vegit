package sheridan.sharmupm.vegit_capstone.services.network

import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.*
import sheridan.sharmupm.vegit_capstone.models.ingredients.Ingredient
import sheridan.sharmupm.vegit_capstone.models.ingredients.IngredientName
import sheridan.sharmupm.vegit_capstone.models.login.ClassifyModel
import sheridan.sharmupm.vegit_capstone.models.login.LoggedInUserView
import sheridan.sharmupm.vegit_capstone.models.login.LoginModel
import sheridan.sharmupm.vegit_capstone.models.products.DenyProduct
import sheridan.sharmupm.vegit_capstone.models.products.Product
import sheridan.sharmupm.vegit_capstone.models.products.SubmitProduct
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

    @GET("ingredients/safe/{diet}")
    fun fetchSafeIngredientsAsync(@Path("diet") diet:Int): Deferred<Response<List<Ingredient>>>

    @GET("ingredients/not-safe/{diet}")
    fun fetchNotSafeIngredientsAsync(@Path("diet") diet:Int): Deferred<Response<List<Ingredient>>>

    @GET("ingredients/classify/get")
    fun fetchIngredientsToClassifyAsync(): Deferred<Response<List<Ingredient>>>

    @GET("ingredients/classify/update/{id}/{diet}")
    fun updateClassificationAsync(@Path("id") id:Int, @Path("diet") diet:Int): Deferred<Response<Int>>

    // PRODUCTS

    @GET("/products/advertising/{diet}")
    fun fetchAdvertisementProductsAsync(@Path("diet") diet:Int): Deferred<Response<List<Product>>>

    @GET("/products/advertising-avoid/{diet}")
    fun fetchAvoidProductsAsync(@Path("diet") diet:Int): Deferred<Response<List<Product>>>

    @GET("/products/scan-history")
    fun fetchScanHistoryAsync(): Deferred<Response<List<Product>>>

    @GET("products/get-product-appoval")
    fun fetchApproveProductsAsync(): Deferred<Response<List<Product>>>

    @GET("products/get-product-appoved")
    fun fetchApprovedProductsAsync(): Deferred<Response<List<Product>>>

    @GET("products/get-product-denied")
    fun fetchDeniedProductsAsync(): Deferred<Response<List<Product>>>

    @POST("products/approve/{id}")
    fun acceptProductAsync(@Path("id") id:Int): Deferred<Response<Int>>

    @POST("products/deny")
    fun denyProductAsync(@Body denyProduct: DenyProduct): Deferred<Response<Int>>

    @POST("products/add/market-product")
    fun submitMarketProductAsync(@Body submitProduct: SubmitProduct): Deferred<Response<Product>>

    @POST("products/views/{id}")
    fun viewProductAsync(@Path("id") id:Int): Deferred<Response<Int>>

    @DELETE("products/delete/{id}")
    fun deleteProductAsync(@Path("id") id:Int): Deferred<Response<Int>>
}