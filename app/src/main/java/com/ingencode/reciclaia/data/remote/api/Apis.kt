package com.ingencode.reciclaia.data.remote.api

import com.ingencode.reciclaia.data.remote.dto.ProcessingTypeDTO
import com.ingencode.reciclaia.data.remote.dto.TestResponseDb
import com.ingencode.reciclaia.data.remote.dto.UrlDTO
import com.ingencode.reciclaia.data.remote.dto.WasteTypeDto
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

    interface WasteInfoApi {
        @GET(Routes.ApisEndpoints.WasteInfo.WASTE_TYPES)
        suspend fun getWasteTypes(): Response<List<WasteTypeDto>>

        @GET(Routes.ApisEndpoints.WasteInfo.PROCESSING_TYPES)
        suspend fun getProcessingTypes(): Response<List<ProcessingTypeDTO>>

        @GET(Routes.ApisEndpoints.WasteInfo.URLS)
        suspend fun getUrls(): Response<List<UrlDTO>>
    }
}