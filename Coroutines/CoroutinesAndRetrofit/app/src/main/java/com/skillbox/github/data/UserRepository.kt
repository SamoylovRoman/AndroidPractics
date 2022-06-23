package com.skillbox.github.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class UserRepository {
    fun updateCompany(
        user: RemoteUser,
        onComplete: (RemoteUser) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        Networking.githubApi.updateCompany(user).enqueue(object : Callback<RemoteUser> {
            override fun onResponse(call: Call<RemoteUser>, response: Response<RemoteUser>) {
                when (response.code()) {
                    REQUEST_CODE_OK -> onComplete(response.body()!!)
                    else -> onError(Throwable("incorrect status code"))
                }
            }

            override fun onFailure(call: Call<RemoteUser>, t: Throwable) {
                onError(t)
            }
        })
    }

    fun unsetRepoIsStarred(
        owner: String,
        repo: String,
        onComplete: (Boolean) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        Networking.githubApi.unsetRepoIsStarred(owner, repo).enqueue(object : Callback<Int> {
            override fun onResponse(call: Call<Int>, response: Response<Int>) {
                when (response.code()) {
                    REQUEST_CODE_IS_STARRED -> onComplete(true)
                    else -> onError(Throwable("incorrect status code"))
                }
            }

            override fun onFailure(call: Call<Int>, t: Throwable) {
                onError(t)
            }
        })
    }

    fun setRepoIsStarred(
        owner: String,
        repo: String,
        onComplete: (Boolean) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        Networking.githubApi.setRepoIsStarred(owner, repo).enqueue(object : Callback<Int> {
            override fun onResponse(call: Call<Int>, response: Response<Int>) {
                when (response.code()) {
                    REQUEST_CODE_IS_STARRED -> onComplete(true)
                    else -> onError(Throwable("incorrect status code"))
                }
            }

            override fun onFailure(call: Call<Int>, t: Throwable) {
                onError(t)
            }
        })
    }

    suspend fun checkRepoIsStarred(
        owner: String,
        repo: String
    ): Boolean {
        return withContext(Dispatchers.Default) {
            suspendCancellableCoroutine { continuation ->
                val call = Networking.githubApi.checkRepoIsStarred(owner, repo)
                call.enqueue(object : Callback<Int> {
                    override fun onResponse(call: Call<Int>, response: Response<Int>) {
                        when (response.code()) {
                            REQUEST_CODE_OK -> continuation.resume(true)
                            REQUEST_CODE_NOT_MODIFIED -> continuation.resumeWithException(
                                Exception("Not modified")
                            )
                            REQUEST_CODE_REQUIRES_AUTHENTICATION -> continuation.resumeWithException(
                                Exception("Requires authentication")
                            )
                            REQUEST_CODE_FORBIDDEN -> continuation.resumeWithException(
                                Exception("Forbidden")
                            )
                            REQUEST_CODE_IS_NOT_STARRED -> continuation.resume(false)
                        }
                    }

                    override fun onFailure(call: Call<Int>, t: Throwable) {
                        continuation.resumeWithException(t)
                    }
                })
                continuation.invokeOnCancellation {
                    call.cancel()
                }
            }
        }
    }

    suspend fun searchUserInfo(): RemoteUser {
        return Networking.githubApi.searchUserInfo()
    }

    suspend fun searchUsersFollowings(): List<RemoteFollowing> {
        return Networking.githubApi.getFollowers()
    }

    suspend fun searchRepositories(): List<RemoteRepo> {
        return Networking.githubApi.searchUserRepositories()
    }

    companion object {
        const val REQUEST_CODE_OK = 200
        const val REQUEST_CODE_IS_STARRED = 204
        const val REQUEST_CODE_NOT_MODIFIED = 304
        const val REQUEST_CODE_REQUIRES_AUTHENTICATION = 401
        const val REQUEST_CODE_FORBIDDEN = 403
        const val REQUEST_CODE_IS_NOT_STARRED = 404
    }
}