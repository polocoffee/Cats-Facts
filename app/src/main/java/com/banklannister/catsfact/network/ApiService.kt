package com.banklannister.catsfact.network

import com.banklannister.catsfact.model.CatsModel
import com.banklannister.catsfact.utils.END_POINT
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {

    @GET(END_POINT)
    suspend fun getCatsFacts(): Response<CatsModel>
}