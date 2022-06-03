package com.skillbox.github.data

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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

    fun checkRepoIsStarred(
        owner: String,
        repo: String,
        onComplete: (Boolean) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        Networking.githubApi.checkRepoIsStarred(owner, repo).enqueue(object : Callback<Int> {
            override fun onResponse(call: Call<Int>, response: Response<Int>) {
                when (response.code()) {
                    REQUEST_CODE_IS_STARRED -> onComplete(true)
                    REQUEST_CODE_IS_NOT_STARRED -> onComplete(false)
                    else -> onError(Throwable("incorrect status code"))
                }
            }

            override fun onFailure(call: Call<Int>, t: Throwable) {
                onError(t)
            }
        })
    }

    fun searchUserInfo(
        onComplete: (RemoteUser?) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        Networking.githubApi.searchUsers().enqueue(object : Callback<RemoteUser> {
            override fun onResponse(call: Call<RemoteUser>, response: Response<RemoteUser>) {
                if (response.isSuccessful) {
                    onComplete(response.body())
                } else {
                    onError(Throwable("incorrect status code"))
                }
            }

            override fun onFailure(call: Call<RemoteUser>, t: Throwable) {
                onError(t)
            }

        })
    }

    fun searchRepositories(
        onComplete: (List<RemoteRepo>) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        Networking.githubApi.searchUserRepositories()
            .enqueue(object : Callback<List<RemoteRepo>> {
                override fun onResponse(
                    call: Call<List<RemoteRepo>>,
                    response: Response<List<RemoteRepo>>
                ) {
                    if (response.isSuccessful) {
                        onComplete(response.body().orEmpty())
                    } else {
                        onError(Throwable("incorrect status code"))
                    }
                }

                override fun onFailure(
                    call: Call<List<RemoteRepo>>,
                    t: Throwable
                ) {
                    onError(t)
                }
            })
    }

    companion object {
        const val REQUEST_CODE_OK = 200
        const val REQUEST_CODE_IS_STARRED = 204
        const val REQUEST_CODE_IS_NOT_STARRED = 404
    }
}