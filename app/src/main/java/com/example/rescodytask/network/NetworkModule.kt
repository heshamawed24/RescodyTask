package com.example.rescodytask.network

import android.util.Log
import com.squareup.moshi.Moshi
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.internal.platform.Platform
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.BuildConfig
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import java.util.logging.Level
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

private val sLogLevel =
    HttpLoggingInterceptor.Level.BASIC



private fun getUnsafeOkHttpClient(): OkHttpClient {
    val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
        override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {
        }

        override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {
        }

        override fun getAcceptedIssuers() = arrayOf<X509Certificate>()
    })
    val sslContext = SSLContext.getInstance("SSL")
    sslContext.init(null, trustAllCerts, java.security.SecureRandom())
    val sslSocketFactory = sslContext.socketFactory
    return OkHttpClient.Builder()
     //   .addInterceptor(loggingInterceptor)
        .sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
        .hostnameVerifier { _, _ -> true }.build()
}


private const val baseUrl = "https://spreadsheets.google.com"



private fun getLogInterceptor() = HttpLoggingInterceptor().apply { level = sLogLevel }

class ErrorLoggingInterceptor() : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = chain.request()
        val response = chain.proceed(request)
        when (response.code()) {
            400 -> {
                Log.i(javaClass.simpleName, "bad Request")
            }
            401 -> {
                //Show UnauthorizedError Message
                Log.i(javaClass.simpleName, "bad Request")
            }
            403 -> {
                Log.i(javaClass.simpleName, "bad Request")
                //Show Forbidden Message
            }
            404 -> {
                //Show NotFound Message
                Log.i(javaClass.simpleName, "bad Request")
            }
            // ... and so on
        }
        return response
    }
}

fun createNetworkClient() =
    retrofitClient(baseUrl, okHttpClient(true))

private fun okHttpClient(addAuthHeader: Boolean) = OkHttpClient.Builder()

    .addInterceptor(getLogInterceptor()).apply { setTimeOutToOkHttpClient(this) }
 //   .addInterceptor(loggingInterceptor)
    .addInterceptor(ErrorLoggingInterceptor())
    .addInterceptor(headersInterceptor(addAuthHeader = addAuthHeader)).build()

var moshi = Moshi.Builder().build()

private fun retrofitClient(baseUrl: String, httpClient: OkHttpClient): Retrofit =
    Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(getUnsafeOkHttpClient())
//        .client(httpClient)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .build()


fun headersInterceptor(addAuthHeader: Boolean) = Interceptor { chain ->
    chain.proceed(
        chain.request().newBuilder()
            //     .addHeader("Content-Type", "application/json")
            .addHeader("accept", "*/*")
            .build()
    )
}

private fun setTimeOutToOkHttpClient(okHttpClientBuilder: OkHttpClient.Builder) =
    okHttpClientBuilder.apply {
        readTimeout(30L, TimeUnit.SECONDS)
        connectTimeout(30L, TimeUnit.SECONDS)
        writeTimeout(30L, TimeUnit.SECONDS)
    }