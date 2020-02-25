package com.android.todo.todoantoinegouteix.network

import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object Api {

    private const val BASE_URL = "https://android-tasks-api.herokuapp.com/api/"
    private const val TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VyX2lkIjoyNTMsImV4cCI6MTYxNDI1ODY0MH0.pGfCZaShccheinpec9xJq_XVT9M3sXGYb6o372hXnQg"

    // Instance Moshi
    private val moshi = Moshi.Builder().build()

    // Créer le client HTTP
    private val okHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor { chain ->
                val newRequest = chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer $TOKEN")
                    .build()
                chain.proceed(newRequest)
            }
            .build()
    }

    // Création de l'instance de rétrofit
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    val userService: UserService by lazy { retrofit.create(UserService::class.java) }
    val tasksWebService: TasksWebService by lazy { retrofit.create(TasksWebService::class.java)}

}