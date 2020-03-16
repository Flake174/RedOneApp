package com.redone.app.resources

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface IRecords {
    @GET("scripts/XML_dynamic.asp?VAL_NM_RQ=R01235")
    fun loadRecords(@Query("date_req1") dateRangeFrom: String?, @Query("date_req2") dateRangeTo: String?): Call<Records?>?
}