package com.redone.app.resources

import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.simplexml.SimpleXmlConverterFactory

class Controller {

    private val BASE_URL = "http://www.cbr.ru/"

    fun start(): IRecords {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(SimpleXmlConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
        val iRecords = retrofit.create(IRecords::class.java)
        return iRecords
    }

}
