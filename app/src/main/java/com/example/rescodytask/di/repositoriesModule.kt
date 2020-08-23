package com.example.rescodytask.di

import com.example.rescodytask.data.repo.FeedRepo
import org.koin.dsl.module

val repositoriesModule = module {
    single { FeedRepo(get()) }
}