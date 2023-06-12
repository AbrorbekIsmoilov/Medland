package com.med.medland.presentation.fragment.signUpFragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.med.medland.R
import com.med.medland.data.api.retrofitCreate.ApiResult.Companion.error
import com.med.medland.data.api.retrofitCreate.ApiResult.Companion.success
import com.med.medland.data.locale.Constants
import com.med.medland.databinding.FragmentSignUpTwoBinding
import com.med.medland.presentation.fragment.otherComponents.MyCustomSnackBar
import com.med.medland.presentation.fragment.otherComponents.PhoneMaskManager
import com.med.medland.presentation.fragment.otherComponents.adapter.SelectCountryCodeAdapter
import com.med.medland.presentation.fragment.otherComponents.connection.ConnectionError
import com.med.medland.presentation.fragment.otherComponents.connection.ConnectivityManager
import com.med.medland.presentation.fragment.otherComponents.dialog.ConnectionDialog
import com.med.medland.presentation.fragment.otherComponents.dialog.SelectCountryCodeDialog
import com.med.medland.presentation.fragment.otherComponents.model.PhoneMaskModel
import com.med.medland.presentation.fragment.signUpFragment.model.SignUpViewModel
import com.santalu.maskara.Mask
import com.santalu.maskara.MaskChangedListener
import com.santalu.maskara.MaskStyle


class SignUpTwoFragment : Fragment(), SelectCountryCodeAdapter.SelectedCountryListener, ConnectionDialog.RefreshClicked {

    private lateinit var binding: FragmentSignUpTwoBinding
    private lateinit var signUpViewModel: SignUpViewModel
    private lateinit var selectCountryCodeDialog: SelectCountryCodeDialog
    private lateinit var connectionDialog: ConnectionDialog
    private lateinit var connectivityManager: ConnectivityManager
    private lateinit var connectionError: ConnectionError
    private lateinit var myCustomSnackBar: MyCustomSnackBar
    private var phoneMaskManager = PhoneMaskManager()
    private var selectCountryCodeAdapter = SelectCountryCodeAdapter(phoneMaskManager.loadPhoneMusk(), this, true)
    private var phoneNumberLength = 12
    private var isConnected = true
    private var otherCountry = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        signUpViewModel = ViewModelProvider(this)[SignUpViewModel :: class.java]
        connectionDialog = ConnectionDialog(requireContext(), this)
        connectivityManager = ConnectivityManager(requireContext())
        connectionError = ConnectionError(requireContext())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentSignUpTwoBinding.inflate(inflater, container, false)

        initObserver()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        connectivityManager.observe(viewLifecycleOwner) { isConnected = it }

        binding.signUpCountryNumTv.setOnClickListener {
            selectCountryCodeDialog = SelectCountryCodeDialog(requireContext(),
                selectCountryCodeAdapter, phoneMaskManager, true)
            selectCountryCodeDialog.showDialog()
        }

        binding.signUpTwoNextBtn.setOnClickListener {
            myCustomSnackBar = MyCustomSnackBar(it, layoutInflater)
            if (filledInformation() && isConnected) {
                val phoneNumber = binding.signUpCountryNumTv.text.toString() +binding.signUpPhoneNumberEt.text.toString().replace(" ","")
                signUpViewModel.sendPhoneNumber(phoneNumber, "bemor")
                connectionDialog.showDialog(Constants.IS_LOADING, Constants.IS_LOADING,"Iltimos kuting!!\nTelefon raqamingiz tekshirilmoqda...")
            }
            else {
                if (!isConnected) connectionDialog.showDialog(Constants.NO_INTERNET, Constants.NO_INTERNET, "")
            }

        }

        binding.signUpSignInBtn.setOnClickListener {
            findNavController().navigate(R.id.action_signUpTwoFragment_to_loginFragment)
        }
    }

    override fun selectedCountry(selected: PhoneMaskModel) {

        if (selected.countryCode != "+998") {
            binding.signUpEmailEt.visibility = View.VISIBLE
            otherCountry = true
        }
        else {
            binding.signUpEmailEt.visibility = View.GONE
            otherCountry = false
        }

        binding.signUpCountryNumTv.text = selected.countryCode
        binding.signUpPhoneNumberEt.hint = selected.mask
        phoneNumberLength = selected.mask.trim().length
        val value = selected.mask.replace("0", "-")
        val mask = Mask(value, '-', MaskStyle.COMPLETABLE)
        val listener = MaskChangedListener(mask)
        binding.signUpPhoneNumberEt.addTextChangedListener(listener)
        selectCountryCodeDialog.dismissDialog()
    }

    private fun filledInformation() : Boolean{
        if (binding.signUpPhoneNumberEt.text.toString().length == phoneNumberLength && binding.signUpPasswordEt.text.length >= 8
            && !otherCountry && binding.signUpPasswordEt.text.toString() == binding.signUpConfirmPasswordEt.text.toString()) {
            return true
        }
        else if (binding.signUpPhoneNumberEt.text.toString().length == phoneNumberLength && binding.signUpPasswordEt.text.length >= 8
            && otherCountry && binding.signUpEmailEt.text.toString().isNotEmpty()
            && binding.signUpPasswordEt.text.toString() == binding.signUpConfirmPasswordEt.text.toString()) {
            return true
        }
        else {
            if (binding.signUpPhoneNumberEt.text.toString().length != phoneNumberLength) {
                myCustomSnackBar.showErrorSnack("Iltimos telefon raqamingizni to'liq kiriting !")
            }
            else if (otherCountry && binding.signUpEmailEt.text.toString().isEmpty()) {
                myCustomSnackBar.showErrorSnack("Iltimos e-mail pochtangizni kiriting !")
            }
            else if (binding.signUpPasswordEt.text.length < 8) {
                myCustomSnackBar.showErrorSnack("Parolingiz kamida 8 ta belgidan ibora bulishi kerak !")
            }
            else {
                myCustomSnackBar.showErrorSnack("Parolingizni to'gri tasdiqlamadingiz !")
            }
            return false
        }
    }

    private fun initObserver() {

        connectivityManager.checkConnection()

        signUpViewModel.sendPhoneNumberResponse.observe(viewLifecycleOwner) { apiResult ->
            apiResult.success {
                connectionDialog.dismissDialog()
                val bundle = Bundle()
                if (otherCountry) bundle.putString("email", binding.signUpEmailEt.text.toString())
                else bundle.putString("email", "")
                bundle.putString("phoneNumber", binding.signUpCountryNumTv.text.toString().replace("+","") +binding.signUpPhoneNumberEt.text.toString().replace(" ",""))
                bundle.putString("user_password", binding.signUpPasswordEt.text.toString())
                bundle.putString("user_name", arguments?.getString("user_name"))
                bundle.putString("user_brith_date", arguments?.getString("user_brith_date"))
                bundle.putString("countryName", arguments?.getString("countryName"))
                bundle.putString("genderReveal",arguments?.getString("genderReveal"))
                bundle.putInt("region_id", arguments?.getInt("region_id")!!)
                bundle.putInt("district_id", arguments?.getInt("district_id")!!)
                bundle.putString("address", arguments?.getString("address"))
                findNavController().navigate(R.id.action_signUpTwoFragment_to_signUpThreeFragment, bundle)
            }
            apiResult.error {
                connectionError.checkConnectionError(it,connectionDialog, Constants.IS_LOADING, Constants.SIGNUP2_FRAGMENT,"Kechirasiz bunday raqam avval ro'yxatdan o'tqazilgan!!")
            }
        }
    }

    override fun connectDialogRefreshClicked(refreshType: String) {
        connectionDialog.dismissDialog()
        if (refreshType == Constants.NO_INTERNET) {
            if (!isConnected) connectionDialog.showDialog(Constants.NO_INTERNET, Constants.NO_INTERNET, "")
        }
        else if (refreshType == Constants.IS_LOADING) {
            if (isConnected){
                signUpViewModel.sendPhoneNumber(binding.signUpCountryNumTv.text.toString() +
                            binding.signUpPhoneNumberEt.text.toString().replace(" ",""), "bemor")
            }
            else connectionDialog.showDialog(Constants.NO_INTERNET, Constants.NO_INTERNET, "")
        }
    }
}