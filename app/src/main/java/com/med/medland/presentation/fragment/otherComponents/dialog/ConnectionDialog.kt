package com.med.medland.presentation.fragment.otherComponents.dialog

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import com.med.medland.data.locale.Constants
import com.med.medland.databinding.DialogConnectionBinding

class ConnectionDialog(val context: Context, private val refreshClicked : RefreshClicked) {

    private var connectionDialog : AlertDialog? = null
    private lateinit var connectionBinding : DialogConnectionBinding
    
    interface RefreshClicked{
        fun connectDialogRefreshClicked(refreshType:String)
    }

    private fun createDialog() {
        connectionDialog = AlertDialog.Builder(context).create()
        connectionBinding = DialogConnectionBinding.inflate(LayoutInflater.from(context))
        connectionDialog?.setView(connectionBinding.root)
        connectionDialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        connectionDialog?.setCancelable(false)
        connectionDialog?.show()
    }
    
    fun showDialog(refreshType: String, animationType : String, message : String) {
        dismissDialog()
        if (connectionDialog == null) {
            createDialog()
            animationChanger(animationType, message)
            connectionBinding.dgConnectionRetryBtn.setOnClickListener {
                refreshClicked.connectDialogRefreshClicked(refreshType)
            }
            connectionBinding.dgConectionDismissBtn.setOnClickListener {
                dismissDialog()
            }
        }
    }

    fun animationChanger(key: String, message: String) {

        if (connectionDialog == null) {
            createDialog()
        }

        when(key) {
            Constants.NO_INTERNET -> {
                connectionBinding.dgConnectionLottie.setAnimation("no_internet.json")
                connectionBinding.dgConnectionText.text = "Tarmoq bilan  aloqa mavjud emas!  "
                connectionBinding.dgConnectionBtnLayout.visibility = View.VISIBLE
            }
            Constants.IS_LOADING -> {
                connectionBinding.dgConnectionLottie.setAnimation("heart_beat_loading2.json")
                connectionBinding.dgConnectionText.text = message
            }
            Constants.IS_CHECK_API -> {
                connectionBinding.dgConnectionLottie.setAnimation("check_done.json")
                connectionBinding.dgConnectionLottie.loop(false)
                connectionBinding.dgConnectionLottie.playAnimation()
                connectionBinding.dgConnectionText.text = message
                object : CountDownTimer(1000,1000) {
                    override fun onTick(millisUntilFinished: Long) {}
                    override fun onFinish() {
                        dismissDialog()
                    }
                }.start()
            }
            Constants.IS_NOT_CHECKED -> {
                connectionBinding.dgConnectionLottie.setAnimation("not_done.json")
                connectionBinding.dgConnectionLottie.loop(false)
                connectionBinding.dgConnectionLottie.playAnimation()
                connectionBinding.dgConnectionText.text = message
                connectionBinding.dgConnectionBtnLayout.visibility = View.VISIBLE
            }
        }
    }

    fun dismissDialog() {
        connectionDialog?.dismiss()
        connectionDialog = null
    }
}