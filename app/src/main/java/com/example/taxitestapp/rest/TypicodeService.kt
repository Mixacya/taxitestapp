package com.example.taxitestapp.rest

import com.example.taxitestapp.data.Driver
import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface TypicodeService {

    @GET("/photos")
    fun getDriverArray(
        @Query("_start") start: Int,
        @Query("_end") end: Int): Observable<List<Driver>>

}