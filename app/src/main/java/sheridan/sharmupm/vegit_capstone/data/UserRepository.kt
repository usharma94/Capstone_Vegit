package sheridan.sharmupm.vegit_capstone.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import sheridan.sharmupm.vegit_capstone.models.UserModel
import sheridan.sharmupm.vegit_capstone.network.APIClient
import sheridan.sharmupm.vegit_capstone.network.ApiInterface

class UserRepository {

    private var apiInterface:ApiInterface ?= null

    init {
        apiInterface = APIClient.getApiClient().create(ApiInterface::class.java)
    }

    fun fetchAllUser():LiveData<List<UserModel>>{
        val data = MutableLiveData<List<UserModel>>()

        apiInterface?.fetchAllUsers()?.enqueue(object : Callback<List<UserModel>> {

            override fun onFailure(call: Call<List<UserModel>>, t: Throwable) {
                data.value = null
            }

            override fun onResponse(
                call: Call<List<UserModel>>,
                response: Response<List<UserModel>>
            ) {
                val res = response.body()

                if (response.code() == 200 && res != null) {
                    data.value = res
                } else {
                    data.value = null
                }
            }
        })

        return data
    }
}