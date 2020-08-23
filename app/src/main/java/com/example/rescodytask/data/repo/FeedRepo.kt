package com.example.rescodytask.data.repo

import com.example.rescodytask.data.Api.FeedApi

class FeedRepo(
    var feedApi: FeedApi
) {
   fun getFeeds() = feedApi.getFeeds()
}