package com.example.rescodytask.di

import com.example.rescodytask.data.Api.FeedApi
import com.example.rescodytask.network.createNetworkClient
import io.reactivex.schedulers.Schedulers.single
import org.koin.dsl.module
import retrofit2.Retrofit

val retrofit: Retrofit = createNetworkClient()

private val FEEDAPI: FeedApi = retrofit.create(FeedApi::class.java)

val networkModule = module {
    single { FEEDAPI }

}