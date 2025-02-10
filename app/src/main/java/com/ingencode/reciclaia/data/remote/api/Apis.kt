package com.ingencode.reciclaia.data.remote.api

import com.ingencode.reciclaia.data.remote.dto.TestResponseDb
import retrofit2.Response
import retrofit2.http.GET

/**
Created with ❤ by Jesús Martín (jesusmarvaz@gmail.com) on 2025-02-03.
 */
interface Apis {
    interface TestApi {
        @GET(Routes.ApisEndpoints.TestApi.TEST)
        suspend fun getTest(): Response<String>

        @GET(Routes.ApisEndpoints.TestApi.TESTDB)
        suspend fun getTestDb(): Response<List<TestResponseDb>>
    }
}