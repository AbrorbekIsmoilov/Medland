package com.med.medland.presentation.fragment.splashFragment

import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.med.medland.R
import com.med.medland.data.api.retrofit.ApiResult.Companion.error
import com.med.medland.data.api.retrofit.ApiResult.Companion.success
import com.med.medland.data.locale.Constants
import com.med.medland.databinding.FragmentSplashBinding
import com.med.medland.presentation.fragment.loginFragment.model.LoginViewModel
import com.med.medland.presentation.fragment.otherComponents.connection.ConnectionError
import com.med.medland.presentation.fragment.otherComponents.connection.ConnectivityManager
import com.med.medland.presentation.fragment.otherComponents.dialog.ConnectionDialog
import com.med.medland.presentation.fragment.splashFragment.model.SplashViewModel
import com.orhanobut.hawk.Hawk


class SplashFragment : Fragment(), ConnectionDialog.RefreshClicked {


    private lateinit var binding : FragmentSplashBinding
    private lateinit var viewModel : LoginViewModel
    private lateinit var connectivityManager: ConnectivityManager
    private lateinit var connectionError : ConnectionError
    private lateinit var connectionDialog: ConnectionDialog
    private var isConnected = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this)[LoginViewModel :: class.java]
        connectivityManager = ConnectivityManager(requireContext())
        connectionDialog = ConnectionDialog(requireContext(), this)
        connectionError = ConnectionError(requireContext())

        initObservers()
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSplashBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        connectivityManager.observe(viewLifecycleOwner) { isConnected = it }

        object : CountDownTimer(2000, 2000) {
            override fun onTick(millisUntilFinished: Long) {}

            override fun onFinish() {
                goNextPage()
            }
        }.start()
    }

    private fun goNextPage() {

        if (isConnected) {
            if (Hawk.get<Int>(Constants.LOGIN) == Constants.LOGGED_IN) {
                getToken()
            }
            else {

                if(Hawk.get(Constants.SELECT_LANGUAGE,0) == Constants.DEFAULT_LANGUAGE){
                    findNavController().navigate(R.id.action_splashFragment_to_selectLanguageFragment)
                }
                else {
                    if (Hawk.get<Int>(Constants.WATCH_INTERVIEW) == Constants.WATCH_ENDED)
                        findNavController().navigate(R.id.action_splashFragment_to_loginFragment)
                    else findNavController().navigate(R.id.action_splashFragment_to_interViewFragment)
                }
            }
        }
        else connectionDialog.showDialog("",Constants.NO_INTERNET, "")
    }

    private fun getToken() {

        connectionDialog.dismissDialog()

        val username = Hawk.get<String>(Constants.USERNAME)
        val password = Hawk.get<String>(Constants.PASSWORD)

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(requireContext(), "Iltimos qayta logindan o'ting", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_splashFragment_to_loginFragment)
        }
        else viewModel.getToken(username, password)

    }

    private fun initObservers() {
        connectivityManager.checkConnection()

        viewModel.getTokenData.observe(this) { apiResult ->

            apiResult.success {
                if (apiResult.response?.code() == 200) {
                    Log.d(Constants.SPLASH_FRAGMENT, "get token response code : ${apiResult.response.code()}")
                    Hawk.put(Constants.TOKEN, it?.access_token)
                    Log.e("TAG", "initObservers: ${it?.access_token}", )
                    findNavController().navigate(R.id.action_splashFragment_to_patientFragment)
                }
                else {
                    Log.e(Constants.SPLASH_FRAGMENT, "get token response code : ${apiResult.response?.code()}")
                    findNavController().navigate(R.id.action_splashFragment_to_loginFragment)
                }
            }

            apiResult.error {
                connectionError.checkConnectionError(apiResult.error, connectionDialog, "", Constants.SPLASH_FRAGMENT)
                Log.e(Constants.SPLASH_FRAGMENT, "get token on failure localizedMessage : ${it?.localizedMessage}")
            }
        }
    }

    override fun connectDialogRefreshClicked(refreshType: String) {
        goNextPage()
    }
}