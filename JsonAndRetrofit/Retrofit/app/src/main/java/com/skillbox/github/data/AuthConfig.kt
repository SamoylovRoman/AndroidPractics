package com.skillbox.github.data

import net.openid.appauth.ResponseTypeValues

object AuthConfig {

    const val AUTH_URI = "https://github.com/login/oauth/authorize"
    const val TOKEN_URI = "https://github.com/login/oauth/access_token"
    const val RESPONSE_TYPE = ResponseTypeValues.CODE
    const val SCOPE = "user,repo"

    const val CLIENT_ID = "c1b39c419b12cd28a38f"
    const val CLIENT_SECRET = "3b6213a3b2c8a83a19176eec24381391b04c1392"
    const val CALLBACK_URL = "skillbox://skillbox.ru/callback"
}