package com.med.medland.data.locale

object Constants {

    //Medland Api Url
    const val BASE_URL = "https://projectmedland.uz/"
    const val BASE_URL_IMAGES = "${BASE_URL}images/"

    //Hawk keys
    const val TOKEN = "access_token"
    const val USERNAME = "my_username"
    const val PASSWORD = "my_password"
    const val LOGIN = "app_login"
    const val LOGGED_IN = 1
    const val WATCH_INTERVIEW = "watch_interview"
    const val WATCH_ENDED = 1
    const val SELECT_LANGUAGE = "select_language"
    const val DEFAULT_LANGUAGE = 0
    const val UZ_LANGUAGE = 1
    const val RU_LANGUAGE = 2
    const val EN_LANGUAGE = 3



    //ConnectionDialog status
    const val NO_INTERNET = "lost_connection"
    const val IS_LOADING = "is_loading"
    const val IS_CHECK_API = "check_done"
    const val IS_NOT_CHECKED = "not_done"

    //LOG TAG
    const val CONNECTIVITY_MANAGER = "TAG CONNECTIVITY MANAGER"
    const val SPLASH_FRAGMENT = "TAG SPLASH FRAGMENT"
    const val LOGIN_FRAGMENT = "TAG LOGIN FRAGMENT"

}