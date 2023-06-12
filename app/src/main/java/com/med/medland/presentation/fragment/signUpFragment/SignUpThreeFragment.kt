package com.med.medland.presentation.fragment.signUpFragment

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.med.medland.R
import com.med.medland.data.api.retrofitCreate.ApiResult.Companion.error
import com.med.medland.data.api.retrofitCreate.ApiResult.Companion.success
import com.med.medland.data.locale.Constants
import com.med.medland.databinding.FragmentSignUpThreeBinding
import com.med.medland.presentation.fragment.otherComponents.MyCustomSnackBar
import com.med.medland.presentation.fragment.otherComponents.connection.ConnectionError
import com.med.medland.presentation.fragment.otherComponents.connection.ConnectivityManager
import com.med.medland.presentation.fragment.otherComponents.dialog.ConnectionDialog
import com.med.medland.presentation.fragment.signUpFragment.model.PatientCreateModel
import com.med.medland.presentation.fragment.signUpFragment.model.SignUpViewModel
import retrofit2.HttpException


class SignUpThreeFragment : Fragment(), ConnectionDialog.RefreshClicked {

    private lateinit var binding: FragmentSignUpThreeBinding
    private lateinit var signUpViewModel: SignUpViewModel
    private lateinit var connectionDialog: ConnectionDialog
    private lateinit var connectivityManager: ConnectivityManager
    private lateinit var connectionError: ConnectionError
    private lateinit var customSnackBar: MyCustomSnackBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        signUpViewModel = ViewModelProvider(this)[SignUpViewModel :: class.java]
        connectivityManager = ConnectivityManager(requireContext())
        connectionDialog = ConnectionDialog(requireContext(), this)
        connectionError = ConnectionError(requireContext())

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentSignUpThreeBinding.inflate(inflater, container, false)


        initObservers()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        customSnackBar = MyCustomSnackBar(view, layoutInflater)
        binding.verificationCodeOne.requestFocus()

        binding.recoveryCodeReplay.setOnClickListener {
            signUpViewModel.sendPhoneNumber(arguments?.getString("phoneNumber").toString(), "update_password")
            signUpViewModel.replaySendSmsRequest.postValue(true)
        }

        binding.signUpSignInBtn.setOnClickListener {
            findNavController().navigate(R.id.action_signUpThreeFragment_to_loginFragment)
        }

        binding.signUpThreeNextBtn.setOnClickListener {
            findNavController().navigate(R.id.action_signUpThreeFragment_to_pinCodeFragment)
        }

        binding.verificationCodeOne.addTextChangedListener {
            if (binding.verificationCodeOne.text.toString().isNotEmpty()) {
                binding.verificationCodeTwo.text.clear()
                binding.verificationCodeTwo.requestFocus()
            }
        }
        binding.verificationCodeTwo.addTextChangedListener {
            if (binding.verificationCodeTwo.text.toString().isNotEmpty()) {
                binding.verificationCodeThree.text.clear()
                binding.verificationCodeThree.requestFocus()
            }
        }
        binding.verificationCodeThree.addTextChangedListener {
            if (binding.verificationCodeThree.text.toString().isNotEmpty()) {
                binding.verificationCodeFour.text.clear()
                binding.verificationCodeFour.requestFocus()
            }
        }

        binding.signUpThreeNextBtn.setOnClickListener {
            if (binding.verificationCodeOne.text.toString().isNotEmpty()
                && binding.verificationCodeTwo.text.toString().isNotEmpty()
                && binding.verificationCodeThree.text.toString().isNotEmpty()
                && binding.verificationCodeFour.text.toString().isNotEmpty())  {

                connectionDialog.showDialog("SendSmsCode",Constants.IS_LOADING,"Iltimos kuting, Siz yuborgan kod tekshirilmoqda...")

                val code = binding.verificationCodeOne.text.toString()+binding.verificationCodeTwo.text.toString() +
                        binding.verificationCodeThree.text.toString()+binding.verificationCodeFour.text.toString()
                signUpViewModel.sendSmsCode(arguments?.getString("phoneNumber").toString(), code)

            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun initObservers() {

        val inputMethodManager = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)

        if (signUpViewModel.sendSmsRequest.value == false) {

            if (arguments?.getString("email").toString().isEmpty()) {
                binding.signUpThreeTitleTwo.text = "Biz sizning ${arguments?.getString("phoneNumber").toString()}\naqamingizga SMS kod yubordik"
                signUpViewModel.sendPhoneNumber(arguments?.getString("phoneNumber").toString(), "update_password")
            }
            else {
                binding.signUpThreeTitleTwo.text = "Biz sizning ${arguments?.getString("email").toString()}\nemailingizga SMS kod yubordik"
                signUpViewModel.sendNumberAndEmail(arguments?.getString("phoneNumber").toString(),
                    arguments?.getString("email").toString())
            }

            signUpViewModel.sendSmsRequest.postValue(true)
        }

        signUpViewModel.replaySendSmsRequest.observe(viewLifecycleOwner) {
            if (it) {
                binding.signUpVerificationTimer.visibility = View.VISIBLE
                binding.recoveryCodeReplay.visibility = View.GONE
                signUpViewModel.countDownTimer(binding.signUpVerificationTimer)
            }
            else {
                binding.signUpVerificationTimer.visibility = View.GONE
                binding.recoveryCodeReplay.visibility = View.VISIBLE
            }
        }

        signUpViewModel.sendPhoneNumberResponse.observe(viewLifecycleOwner) { apiResult ->
            apiResult.success {
                customSnackBar.showAlertSnack("Telefon raqamingizga 4 xonalik kod yubormoqdamiz !")
            }
            apiResult.error {
                customSnackBar.showErrorSnack("So'rov bo'yicha xatolik mavjud")
            }
        }

        signUpViewModel.sendNumberAndEmailResponse.observe(viewLifecycleOwner) { apiResult ->
            apiResult.success {
                customSnackBar.showAlertSnack("Email pochtangizga 4 xonalik kod yubormoqdamiz !")
            }
            apiResult.error {
                customSnackBar.showErrorSnack("So'rov bo'yicha xatolik mavjud")
            }
        }

        signUpViewModel.sendSmsCodeResponse.observe(viewLifecycleOwner) { apiResult ->
            apiResult.success {
                connectionDialog.animationChanger(Constants.IS_LOADING, "Kod tasdiqlandi ! Sizning malumotlaringiz saqlanmoqda...")
                signUpRequest()
            }
            apiResult.error {
                if (it is HttpException) Log.e("${Constants.SIGNUP3_FRAGMENT}  send code validation : ", it.response().toString() +"\n Message : ${it.response()?.errorBody()?.string()}")
                else Log.e(Constants.SIGNUP3_FRAGMENT, "send code validation : ${it?.localizedMessage}")
            }
        }

        signUpViewModel.createPatientLiveData.observe(viewLifecycleOwner) {apiResult->
            apiResult.success {
                connectionDialog.showDialog("createPatient", Constants.IS_CHECK_API, "Siz ro'yxatdan muvaffaqiyatli o'tdingiz !!")
                val bundle = Bundle()
                bundle.putString("login", arguments?.getString("phoneNumber"))
                bundle.putString("password", arguments?.getString("user_password"))
                bundle.putBoolean("newPinCode", true)
                findNavController().navigate(R.id.action_signUpThreeFragment_to_pinCodeFragment, bundle)
            }
            apiResult.error {
                connectionError.checkConnectionError(it,connectionDialog,"createPatient", Constants.SIGNUP3_FRAGMENT,null)
            }
        }
    }

    private fun signUpRequest() {
        val name = arguments?.getString("user_name").toString()
        val brithDate = arguments?.getString("user_brith_date").toString()
        val countryName = arguments?.getString("countryName").toString()
        val regionId = arguments?.getInt("region_id")!!
        val districtId = arguments?.getInt("district_id")!!
        val genderReveal = arguments?.getString("genderReveal").toString()
        val phoneNumber = arguments?.getString("phoneNumber").toString()
        val password = arguments?.getString("user_password").toString()
        val address = arguments?.getString("address").toString()
        val email = arguments?.getString("email").toString()

        val patientCreateModel = PatientCreateModel(name, brithDate, genderReveal, address, countryName, regionId, districtId, phoneNumber, email, password, true)

        signUpViewModel.createPatientModel(patientCreateModel)
    }

    override fun connectDialogRefreshClicked(refreshType: String) {
        connectionDialog.dismissDialog()
        when(refreshType) {
            "createPatient" -> {
                signUpRequest()
            }
        }
    }
}