package sheridan.sharmupm.vegit_capstone.services.repository

class UserRepository {

    //private var apiInterface: ApiInterface?= null

    init {
        //apiInterface = APIClient.getApiClient().create(ApiInterface::class.java)
    }

/*    fun fetchAllUser():LiveData<List<UserModel>>{
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
    }*/
}