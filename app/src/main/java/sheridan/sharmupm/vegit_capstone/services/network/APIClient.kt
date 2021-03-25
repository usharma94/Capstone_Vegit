package sheridan.sharmupm.vegit_capstone.services.network

import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

// production url
const val BASEURL = "https://vegitapi.azurewebsites.net/"

object APIClient {
    private var client = OkHttpClient().newBuilder()
        .readTimeout(100, TimeUnit.SECONDS)
        .connectTimeout(100, TimeUnit.SECONDS)
        .build()

    private var gson = GsonBuilder()
        .setLenient()
        .create()

    private fun retrofit() : Retrofit = Retrofit.Builder()
        .client(client)
        .baseUrl(BASEURL)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()

    val apiInterface : ApiInterface = retrofit().create(ApiInterface::class.java)
}