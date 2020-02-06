package com.example.taxitestapp.rest

import com.example.taxitestapp.data.Driver
import com.google.gson.GsonBuilder
import io.reactivex.disposables.Disposable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


object ApiRepository {

    const val BASE_URL = "https://jsonplaceholder.typicode.com"
    val service: TypicodeService

    init {

        val gson = GsonBuilder()
            .setPrettyPrinting()
            .create()

        var retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()

        service = retrofit.create(TypicodeService::class.java)
    }

}