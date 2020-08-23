package com.example.rescodytask.data.Api

import com.example.rescodytask.data.models.FeedModel
import retrofit2.Call
import retrofit2.http.GET

interface FeedApi {

    @GET("/feeds/list/0Ai2EnLApq68edEVRNU0xdW9QX1BqQXhHRl9sWDNfQXc/od6/public/basic?alt=json")
      fun getFeeds(): Call<FeedModel>
}