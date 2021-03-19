package sheridan.sharmupm.vegit_capstone.network

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

// production url
const val BASEURL = "https://vegitapi.azurewebsites.net/"

class APIClient {
    companion object {
        private var retrofit:Retrofit ?= null
        fun getApiClient(): Retrofit {
            val gson = GsonBuilder()
                .setLenient()
                .create()
            var okHttpClient = OkHttpClient.Builder()
                .readTimeout(100, TimeUnit.SECONDS)
                .connectTimeout(100, TimeUnit.SECONDS)
                .build()
            if (retrofit == null) {
                retrofit = Retrofit.Builder()
                    .baseUrl(BASEURL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build()
            }
            return retrofit!!
        }
    }
}