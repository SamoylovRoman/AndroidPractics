package com.skillbox.github.data

import retrofit2.Call
import retrofit2.http.*

interface GithubApi {

    @GET("/user")
    fun searchUsers(
    ): Call<RemoteUser>

    @GET("/repositories")
    fun searchUserRepositories(): Call<List<RemoteRepo>>

    @GET("/user/starred/{owner}/{repo}")
    fun checkRepoIsStarred(
        @Path("owner") owner: String,
        @Path("repo") repo: String
    ): Call<Int>

    @PUT("/user/starred/{owner}/{repo}")
    fun setRepoIsStarred(
        @Path("owner") owner: String,
        @Path("repo") repo: String
    ): Call<Int>

    @DELETE("/user/starred/{owner}/{repo}")
    fun unsetRepoIsStarred(
        @Path("owner") owner: String,
        @Path("repo") repo: String
    ): Call<Int>

    @PATCH("/user")
    fun updateCompany(
        @Body user: RemoteUser
    ): Call<RemoteUser>
}