package com.example.tp_game_androidavanced.service

import com.example.tp_game_androidavanced.model.Model_target
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @GET("iut/game-list")
    suspend fun getGameList(): Response<MutableList<String>>

    @GET("iut/game/{name}")
    suspend fun getGameByName(@Path("name") name : String): Response<MutableList<Model_target>>

    @POST("iut/game/{name}")
    suspend fun createGameByName(@Path("name") name : String,@Body game: MutableList<Model_target>?): Response<Void>

    @DELETE("iut/game/{id}")
    suspend fun deleteGameById(@Path("id") id : String): Response<Void>
}