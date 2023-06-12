package com.med.medland.data.api.retrofit

import retrofit2.Response

data class ApiResult <out T>(
    val status: ApiStatus,
    val data: T?,
    val response: Response<out T>?,
    val error: Throwable?
    )
{

    companion object {
        fun <T> success(data: T?, response: Response<out T>): ApiResult<T> {
            return ApiResult(ApiStatus.SUCCESS, data, response, null)
        }

        fun <T> error(error: Throwable?): ApiResult<T> {
            return ApiResult(ApiStatus.ERROR, null, null, error)
        }


        fun <T> ApiResult<T>.success(body: (T?) -> Unit){
            if (status == ApiStatus.SUCCESS) body(data)
        }

        fun <T> ApiResult<T>.error(body: (Throwable?) -> T){
            if (status == ApiStatus.ERROR) body(error)
        }
    }

}