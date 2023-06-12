package com.med.medland.presentation.fragment.otherComponents.connection

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import com.med.medland.data.api.retrofitCreate.RetrofitInstance
import com.med.medland.data.locale.Constants
import com.med.medland.presentation.fragment.otherComponents.dialog.ConnectionDialog
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.HttpException
import java.io.IOException
import java.io.InterruptedIOException
import java.net.SocketTimeoutException
import java.util.concurrent.TimeoutException

class ConnectionError(val context: Context) {

    private fun parseError(errorBody: ResponseBody?): String {
        val converter: Converter<ResponseBody, ErrorResponseDetail> = RetrofitInstance.retrofit
            .responseBodyConverter(ErrorResponseDetail::class.java, arrayOfNulls<Annotation>(0))
        return try { if (errorBody == null) ""
            else converter.convert(errorBody)!!.detail
        } catch (e: IOException) { return "" }
    }

    @SuppressLint("LongLogTag")
    fun checkConnectionError(t: Throwable?, connectionDialog : ConnectionDialog, refreshType : String, tag : String, myErrorMessage : String?){
        when (t) {
            is HttpException -> {
                Log.e("$tag  error : ", t.response().toString() +"\n Message : ${t.response()?.errorBody()?.string()}")

                val errorMassage = parseError(t.response()?.errorBody())

                if (errorMassage.isEmpty()) { connectionDialog.showDialog(refreshType, Constants.IS_NOT_CHECKED,"So'rov bilan xatolik mavjud-1 !!") }
                if (t.code() == 401) { connectionDialog.showDialog(refreshType, Constants.IS_NOT_CHECKED,"Login yoki parol noto'g'ri !!") }
                else if (t.code() == 500) { connectionDialog.showDialog(refreshType, Constants.IS_NOT_CHECKED, "Malumotlarar mavjuda emas !!") }
                else if (t.code() == 404) { connectionDialog.showDialog(refreshType, Constants.IS_NOT_CHECKED, "Malumotlar manzili topilmadi !!") }
                else { connectionDialog.showDialog(refreshType, Constants.IS_NOT_CHECKED, myErrorMessage?: "So'rov bilan xatolik mavjud-2 !!") }

            }
            is SocketTimeoutException, is InterruptedIOException, is TimeoutException ->{
                Log.d("$tag connection error :" ," TIME OUT" )
                connectionDialog.showDialog(refreshType, Constants.IS_NOT_CHECKED, "Tarmoqqa ulanish vaqti tugadi ! Qayta urunib ko'ring")
            }


            else -> {
                Log.e("$tag connection error: ", t?.localizedMessage.toString())
                connectionDialog.showDialog(refreshType, Constants.IS_NOT_CHECKED, "So'rov bilan xatolik mavjud-3 !!")
            }
        }
    }
}