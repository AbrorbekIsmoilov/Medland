package com.med.medland.presentation.fragment.loginFragment

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.med.medland.R
import com.med.medland.data.api.retrofit.ApiResult.Companion.error
import com.med.medland.data.api.retrofit.ApiResult.Companion.success
import com.med.medland.data.locale.Constants
import com.med.medland.databinding.FragmentLoginBinding
import com.med.medland.presentation.fragment.loginFragment.model.LoginViewModel
import com.med.medland.presentation.fragment.otherComponents.connection.ConnectionError
import com.med.medland.presentation.fragment.otherComponents.connection.ConnectivityManager
import com.med.medland.presentation.fragment.otherComponents.dialog.ConnectionDialog
import com.orhanobut.hawk.Hawk


class LoginFragment : Fragment(), ConnectionDialog.RefreshClicked {

    private lateinit var binding : FragmentLoginBinding
    private lateinit var viewModel : LoginViewModel
    private lateinit var connectionDialog: ConnectionDialog
    private lateinit var connectivityManager: ConnectivityManager
    private lateinit var connectionError: ConnectionError
    private var isConnected = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[LoginViewModel :: class.java]
        connectionDialog = ConnectionDialog(requireContext(),this)
        connectivityManager = ConnectivityManager(requireContext())
        connectionError = ConnectionError(requireContext())

        initObservers()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        connectivityManager.observe(viewLifecycleOwner) { isConnected = it }

        binding.loginSignUpBtn.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_signUpOneFragment)
        }

        binding.loginLogOnGuestBtn.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_patientFragment)
        }

        binding.loginSignInBtn.setOnClickListener {
            getToken()
        }
    }

    private fun getToken() {

        if (isConnected) {
            val username = binding.phoneNumberEt.text.toString()
            val password = binding.passwordEt.text.toString()

            when {
                username.isEmpty() -> {
                    binding.phoneNumberEt.error = "Loginni kiriting"
                }
                password.isEmpty() -> {
                    binding.passwordEt.error = "parolni kiriting"
                }
                else -> {
                    connectionDialog.showDialog("getToken",Constants.IS_LOADING,"Iltimos kuting malumotlaringiz tekshirilmoqda...")
                    viewModel.getToken(username, password)
                }
            }
        }
        else connectionDialog.showDialog(Constants.NO_INTERNET, Constants.NO_INTERNET, "")

    }

    private fun initObservers() {

        connectivityManager.checkConnection()

        viewModel.getTokenData.observe(this) { apiResult ->

            apiResult.success {
                if (apiResult.response?.code() == 200) {
                    connectionDialog.showDialog("", Constants.IS_CHECK_API,"Shaxsingiz tasdiqlandi! Xush kelibsiz")
                    Log.d("LoginFragment", "get token response code : ${apiResult.response.code()}")
                    Hawk.put(Constants.TOKEN, it?.access_token)
                    Hawk.put(Constants.LOGIN, Constants.LOGGED_IN)
                    Hawk.put(Constants.USERNAME, binding.phoneNumberEt.text.toString())
                    Hawk.put(Constants.PASSWORD, binding.passwordEt.text.toString())
                    findNavController().navigate(R.id.action_loginFragment_to_patientFragment)
                }
                else {
                    connectionError.checkConnectionError(apiResult.error,connectionDialog,"getToken", Constants.LOGIN_FRAGMENT)
                }
            }

            apiResult.error {
                connectionError.checkConnectionError(apiResult.error,connectionDialog,"getToken", Constants.LOGIN_FRAGMENT)
            }
        }

    }

    override fun connectDialogRefreshClicked(refreshType: String) {
        if (refreshType == Constants.NO_INTERNET) {
            if (isConnected) connectionDialog.dismissDialog()
            else connectionDialog.showDialog(Constants.NO_INTERNET, Constants.NO_INTERNET, "")
        }
        else {
            getToken()
        }
    }
}