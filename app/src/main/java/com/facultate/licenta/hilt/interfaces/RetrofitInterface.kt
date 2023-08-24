package com.facultate.licenta.hilt.interfaces

import retrofit2.Response
import retrofit2.http.GET

public interface RetrofitInterface {
    @GET("")
    suspend fun getSomething(): Response<List<Any>>
}