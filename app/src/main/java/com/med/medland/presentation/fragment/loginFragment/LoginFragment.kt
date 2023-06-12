package com.med.medland.presentation.fragment.loginFragment

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.med.medland.R
import com.med.medland.data.api.retrofitCreate.ApiResult.Companion.error
import com.med.medland.data.api.retrofitCreate.ApiResult.Companion.success
import com.med.medland.data.locale.Constants
import com.med.medland.data.room.database.UserDataBase
import com.med.medland.data.room.table.MyInfoTable
import com.med.medland.databinding.FragmentLoginBinding
import com.med.medland.presentation.fragment.loginFragment.model.LoginViewModel
import com.med.medland.presentation.fragment.otherComponents.MyCustomSnackBar
import com.med.medland.presentation.fragment.otherComponents.PhoneMaskManager
import com.med.medland.presentation.fragment.otherComponents.adapter.SelectCountryCodeAdapter
import com.med.medland.presentation.fragment.otherComponents.connection.ConnectionError
import com.med.medland.presentation.fragment.otherComponents.connection.ConnectivityManager
import com.med.medland.presentation.fragment.otherComponents.dialog.ConnectionDialog
import com.med.medland.presentation.fragment.otherComponents.dialog.SelectCountryCodeDialog
import com.med.medland.presentation.fragment.otherComponents.model.PhoneMaskModel
import com.med.medland.presentation.fragment.profileFragment.model.MyInfoModel
import com.orhanobut.hawk.Hawk
import com.santalu.maskara.Mask
import com.santalu.maskara.MaskChangedListener
import com.santalu.maskara.MaskStyle
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class LoginFragment : Fragment(), ConnectionDialog.RefreshClicked, SelectCountryCodeAdapter.SelectedCountryListener {

    private lateinit var binding : FragmentLoginBinding
    private lateinit var viewModel : LoginViewModel
    private lateinit var connectionDialog: ConnectionDialog
    private lateinit var connectivityManager: ConnectivityManager
    private lateinit var connectionError: ConnectionError
    private lateinit var userDataBase: UserDataBase
    private lateinit var selectCountryCodeAdapter: SelectCountryCodeAdapter
    private lateinit var countryCodeDialog: SelectCountryCodeDialog
    private lateinit var myCustomSnackBar: MyCustomSnackBar
    private var phoneMaskManager = PhoneMaskManager()
    private var phoneNumberLength = 12
    private var isConnected = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[LoginViewModel :: class.java]
        connectionDialog = ConnectionDialog(requireContext(),this)
        connectivityManager = ConnectivityManager(requireContext())
        connectionError = ConnectionError(requireContext())
        userDataBase = UserDataBase.getDataBase(requireContext())
        selectCountryCodeAdapter = SelectCountryCodeAdapter(phoneMaskManager.loadPhoneMusk(), this, true)
        countryCodeDialog = SelectCountryCodeDialog(requireContext(), selectCountryCodeAdapter, phoneMaskManager, true)
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


        binding.loginCountryNumTv.setOnClickListener {
            countryCodeDialog.showDialog()
        }

        binding.loginSignUpBtn.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_signUpOneFragment)
        }

        binding.loginLogOnGuestBtn.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_patientFragment)
        }

        binding.loginSignInBtn.setOnClickListener {
            myCustomSnackBar = MyCustomSnackBar(it, layoutInflater)
            getToken()
        }
    }

    private fun getToken() {

        if (isConnected) {
            val username = binding.loginCountryNumTv.text.toString().replace("+","") +binding.phoneNumberEt.text.toString().replace(" ","")
            val password = binding.passwordEt.text.toString()

            when {
                binding.phoneNumberEt.text.toString().isEmpty() -> {
                    myCustomSnackBar.showErrorSnack("Raqamingizni kiriting !")
                    binding.phoneNumberEt.requestFocus()
                }
                password.isEmpty() -> {
                    myCustomSnackBar.showErrorSnack("Parolingizni kiriting !")
                    binding.passwordEt.requestFocus()
                }
                else -> {
                    connectionDialog.showDialog("getToken",Constants.IS_LOADING,"Iltimos kuting malumotlaringiz tekshirilmoqda...")
                    viewModel.getToken(username, password)
                }
            }
        }
        else connectionDialog.showDialog(Constants.NO_INTERNET, Constants.NO_INTERNET, "")

    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun initObservers() {

        connectivityManager.checkConnection()

        GlobalScope.launch(Dispatchers.IO) {
            userDataBase.myInfoDao().insertMyInfo(
                MyInfoTable(0, "0", "", 0,
                    0, "", "", false, false, "",
                    "", "", "", 0, 0.0, "", "")
            )
        }

        viewModel.getTokenData.observe(this) { apiResult ->

            apiResult.success {
                if (apiResult.response?.code() == 200) {
                    connectionDialog.showDialog("", Constants.IS_CHECK_API,"Shaxsingiz tasdiqlandi! Xush kelibsiz")
                    Log.d("LoginFragment", "get token response code : ${apiResult.response.code()}")
                    Hawk.put(Constants.TOKEN, it?.access_token)
                    Hawk.put(Constants.LOGIN, Constants.LOGGED_IN)
                    Hawk.put(Constants.USERNAME, binding.phoneNumberEt.text.toString())
                    Hawk.put(Constants.PASSWORD, binding.passwordEt.text.toString())
                    insertUserInfo(apiResult.data!!.user)
                    findNavController().navigate(R.id.action_loginFragment_to_patientFragment)
                }
                else {
                    connectionError.checkConnectionError(apiResult.error,connectionDialog,"getToken", Constants.LOGIN_FRAGMENT,null)
                }
            }

            apiResult.error {
                connectionError.checkConnectionError(apiResult.error,connectionDialog,"getToken", Constants.LOGIN_FRAGMENT, null)
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


    @OptIn(DelicateCoroutinesApi::class)
    private fun insertUserInfo(myInfoModel: MyInfoModel) {

        GlobalScope.launch(Dispatchers.IO) {
            userDataBase.myInfoDao().updateMyInfo(
                MyInfoTable(
                    0,
                    myInfoModel.tugilgan_sana,
                    myInfoModel.manzil,
                    myInfoModel.viloyat_id,
                    myInfoModel.tel,
                    myInfoModel.karta_raqami,
                    Hawk.get(Constants.PASSWORD),
                    myInfoModel.disabled,
                    myInfoModel.block,
                    myInfoModel.ism,
                    myInfoModel.jinsi,
                    myInfoModel.id,
                    myInfoModel.mamlakat,
                    myInfoModel.tuman_id,
                    myInfoModel.balance,
                    myInfoModel.username,
                    myInfoModel.online
                )
            )
        }
    }

    override fun selectedCountry(selected: PhoneMaskModel) {
        binding.loginCountryNumTv.text = selected.countryCode
        binding.phoneNumberEt.hint = selected.mask
        phoneNumberLength = selected.mask.trim().length
        val value = selected.mask.replace("0", "-")
        val mask = Mask(value, '-', MaskStyle.COMPLETABLE)
        val listener = MaskChangedListener(mask)
        binding.phoneNumberEt.addTextChangedListener(listener)
        countryCodeDialog.dismissDialog()
    }
}
