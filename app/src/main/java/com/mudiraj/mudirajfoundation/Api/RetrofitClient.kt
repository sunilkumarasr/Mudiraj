package com.mudiraj.mudirajfoundation.Api

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {

    private const val BASE_URL = "https://mudirajfoundation.com/api/"
    public const val Image_URL = "https://mudirajfoundation.com/api/"
    public const val Image_URL2 = "https://mudirajfoundation.com/app/uploads/products/"
    public const val URL = "https://mudirajfoundation.com/"

    // Initialize logging interceptor
    private val interceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    // Initialize OkHttpClient with interceptors
    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS) // Increase connection timeout
        .readTimeout(60, TimeUnit.SECONDS)    // Increase read timeout
        .writeTimeout(60, TimeUnit.SECONDS)   // Increase write timeout
        .addInterceptor(interceptor)
        .addInterceptor(RequestInterceptor) // Add the custom interceptor
        .build()

    // Initialize Retrofit with OkHttpClient
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    // Create ApiInterface instance
    val apiInterface: ApiInterface = retrofit.create(ApiInterface::class.java)
}

object RequestInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val builder = originalRequest.newBuilder()

        // Don't set Content-Type manually for multipart/form-data
        if (originalRequest.body?.contentType()?.subtype != "form-data") {
            builder.header("Content-Type", "application/json")
        }

        // Add User-Agent and other headers that don't conflict
        builder.header("User-Agent", "Mozilla/5.0 (AndroidApp)")
        builder.header("Accept", "*/*")

        return chain.proceed(builder.build())
    }
}