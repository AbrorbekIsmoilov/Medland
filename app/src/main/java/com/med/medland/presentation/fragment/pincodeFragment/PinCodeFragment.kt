package com.med.medland.presentation.fragment.pincodeFragment

import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import com.med.medland.data.locale.Constants
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.med.medland.R
import com.med.medland.data.api.retrofitCreate.ApiResult.Companion.error
import com.med.medland.data.api.retrofitCreate.ApiResult.Companion.success
import com.med.medland.data.room.database.UserDataBase
import com.med.medland.data.room.table.MyInfoTable
import com.med.medland.databinding.FragmentPinCodeBinding
import com.med.medland.presentation.fragment.loginFragment.model.LoginViewModel
import com.med.medland.presentation.fragment.otherComponents.MyCustomSnackBar
import com.med.medland.presentation.fragment.otherComponents.connection.ConnectionError
import com.med.medland.presentation.fragment.otherComponents.dialog.ConnectionDialog
import com.med.medland.presentation.fragment.profileFragment.model.MyInfoModel
import com.orhanobut.hawk.Hawk
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class PinCodeFragment : Fragment(), ConnectionDialog.RefreshClicked {

    private lateinit var binding: FragmentPinCodeBinding
    private lateinit var pinCodeViewModel: PinCodeViewModel
    private lateinit var myCustomSnackBar: MyCustomSnackBar
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var connectionDialog: ConnectionDialog
    private lateinit var connectionError: ConnectionError
    private lateinit var userDataBase: UserDataBase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pinCodeViewModel = ViewModelProvider(this)[PinCodeViewModel :: class.java]
        loginViewModel = ViewModelProvider(this)[LoginViewModel :: class.java]
        connectionDialog = ConnectionDialog(requireContext(), this)
        connectionError = ConnectionError(requireContext())
        userDataBase = UserDataBase.getDataBase(requireContext())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentPinCodeBinding.inflate(inflater, container, false)

        if (arguments?.getBoolean("newPinCode") == true) {
            binding.forgotTv.text = "Kerak emas, O'tkazib yuborish"
        }

        initObservers()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.forgotTv.setOnClickListener {
            if (arguments?.getBoolean("newPinCode") == true) {
                isDonePinCode()
            }
            else {
                val bundle = Bundle()
                bundle.putBoolean("pinCodeRecovery", true)
                findNavController().navigate(R.id.action_pinCodeFragment_to_passwordRecoveryFragment, bundle)
            }
        }


        binding.pinNumZero.setOnClickListener {
            myCustomSnackBar = MyCustomSnackBar(it, layoutInflater)
            pinCodeViewModel.addCode("0")
        }
        binding.pinNumOne.setOnClickListener {
            myCustomSnackBar = MyCustomSnackBar(it, layoutInflater)
            pinCodeViewModel.addCode("1")
        }
        binding.pinNumTwo.setOnClickListener {
            myCustomSnackBar = MyCustomSnackBar(it, layoutInflater)
            pinCodeViewModel.addCode("2")
        }
        binding.pinNumThree.setOnClickListener {
            myCustomSnackBar = MyCustomSnackBar(it, layoutInflater)
            pinCodeViewModel.addCode("3")
        }
        binding.pinNumFour.setOnClickListener {
            myCustomSnackBar = MyCustomSnackBar(it, layoutInflater)
            pinCodeViewModel.addCode("4")
        }
        binding.pinNumFive.setOnClickListener {
            myCustomSnackBar = MyCustomSnackBar(it, layoutInflater)
            pinCodeViewModel.addCode("5")
        }
        binding.pinNumSix.setOnClickListener {
            myCustomSnackBar = MyCustomSnackBar(it, layoutInflater)
            pinCodeViewModel.addCode("6")
        }
        binding.pinNumSeven.setOnClickListener {
            myCustomSnackBar = MyCustomSnackBar(it, layoutInflater)
            pinCodeViewModel.addCode("7")
        }
        binding.pinNumEight.setOnClickListener {
            myCustomSnackBar = MyCustomSnackBar(it, layoutInflater)
            pinCodeViewModel.addCode("8")
        }
        binding.pinNumNine.setOnClickListener {
            myCustomSnackBar = MyCustomSnackBar(it, layoutInflater)
            pinCodeViewModel.addCode("9")
        }
        binding.backTageBtn.setOnClickListener {
            myCustomSnackBar = MyCustomSnackBar(it, layoutInflater)
            pinCodeViewModel.removeCode()
        }
    }

    private fun isDonePinCode() {
        val login = arguments?.getString("login")
        val password = arguments?.getString("password")

        loginViewModel.getToken(login.toString(), password.toString())
        connectionDialog.showDialog("getToken", Constants.IS_LOADING,"Iltimos kuting, malumotlaringiz qidirilmoqda..")
    }

    private fun initObservers() {
        pinCodeViewModel.pinCodeText.observe(viewLifecycleOwner) {
            when(it.length) {
                0 -> {
                    binding.pinOne.setImageResource(R.drawable.ic_circle)
                    binding.pinTwo.setImageResource(R.drawable.ic_circle)
                    binding.pinThree.setImageResource(R.drawable.ic_circle)
                    binding.pinFour.setImageResource(R.drawable.ic_circle)
                    binding.pinOne.setColorFilter(Color.parseColor("#C5C5C5"))
                    binding.pinTwo.setColorFilter(Color.parseColor("#C5C5C5"))
                    binding.pinThree.setColorFilter(Color.parseColor("#C5C5C5"))
                    binding.pinFour.setColorFilter(Color.parseColor("#C5C5C5"))
                }
                1 -> {
                    binding.pinOne.setImageResource(R.drawable.background_sent_message_btn)
                    binding.pinTwo.setImageResource(R.drawable.ic_circle)
                    binding.pinThree.setImageResource(R.drawable.ic_circle)
                    binding.pinFour.setImageResource(R.drawable.ic_circle)
                    binding.pinOne.setColorFilter(Color.BLACK)
                    binding.pinTwo.setColorFilter(Color.parseColor("#C5C5C5"))
                    binding.pinThree.setColorFilter(Color.parseColor("#C5C5C5"))
                    binding.pinFour.setColorFilter(Color.parseColor("#C5C5C5"))
                }
                2 -> {
                    binding.pinOne.setImageResource(R.drawable.background_sent_message_btn)
                    binding.pinTwo.setImageResource(R.drawable.background_sent_message_btn)
                    binding.pinThree.setImageResource(R.drawable.ic_circle)
                    binding.pinFour.setImageResource(R.drawable.ic_circle)
                    binding.pinOne.setColorFilter(Color.BLACK)
                    binding.pinTwo.setColorFilter(Color.BLACK)
                    binding.pinThree.setColorFilter(Color.parseColor("#C5C5C5"))
                    binding.pinFour.setColorFilter(Color.parseColor("#C5C5C5"))
                }
                3 -> {
                    binding.pinOne.setImageResource(R.drawable.background_sent_message_btn)
                    binding.pinTwo.setImageResource(R.drawable.background_sent_message_btn)
                    binding.pinThree.setImageResource(R.drawable.background_sent_message_btn)
                    binding.pinFour.setImageResource(R.drawable.ic_circle)
                    binding.pinOne.setColorFilter(Color.BLACK)
                    binding.pinTwo.setColorFilter(Color.BLACK)
                    binding.pinThree.setColorFilter(Color.BLACK)
                    binding.pinFour.setColorFilter(Color.parseColor("#C5C5C5"))
                }
                4 -> {
                    binding.pinOne.setImageResource(R.drawable.background_sent_message_btn)
                    binding.pinTwo.setImageResource(R.drawable.background_sent_message_btn)
                    binding.pinThree.setImageResource(R.drawable.background_sent_message_btn)
                    binding.pinFour.setImageResource(R.drawable.background_sent_message_btn)
                    binding.pinOne.setColorFilter(Color.BLACK)
                    binding.pinTwo.setColorFilter(Color.BLACK)
                    binding.pinThree.setColorFilter(Color.BLACK)
                    binding.pinFour.setColorFilter(Color.BLACK)


                    if (pinCodeViewModel.savePinCode.value!!.isEmpty()) {
                        object : CountDownTimer(200, 200) {
                            override fun onTick(millisUntilFinished: Long) {}
                            override fun onFinish() {
                                pinCodeViewModel.savePinCode.postValue(it)
                                binding.pinTv.text = "Hozirgi kodni takrorlang"
                                pinCodeViewModel.pinCodeText.value = ""
                            }
                        }.start()
                    }
                    else {
                        object : CountDownTimer(200, 200) {
                            override fun onTick(millisUntilFinished: Long) {}
                            override fun onFinish() {
                                if (it == pinCodeViewModel.savePinCode.value) {
                                    myCustomSnackBar.showAlertSnack("Pin kodingiz saqlandi !")
                                    binding.backTageBtn.visibility = View.INVISIBLE
                                    isDonePinCode()
                                }
                                else {
                                    myCustomSnackBar.showErrorSnack("Avvalgi pin kod bilan bir xil emas, qayta urinib ko'ring")
                                    binding.pinTv.text = "Pin kod yarating"
                                    pinCodeViewModel.pinCodeText.value = ""
                                    pinCodeViewModel.savePinCode.value = ""
                                }
                            }
                        }.start()
                    }
                }
            }
        }

        loginViewModel.getTokenData.observe(viewLifecycleOwner) { apiResult->
            apiResult.success {
                connectionDialog.dismissDialog()
                connectionDialog.showDialog("isDone", Constants.IS_CHECK_API,"Shaxsingiz tasdiqlandi! Xush kelibsiz")
                Log.d("PinCodeFragment", "get token response code : ${apiResult.response!!.code()}")
                Hawk.put(Constants.TOKEN, it?.access_token)
                Hawk.put(Constants.LOGIN, Constants.LOGGED_IN)
                Hawk.put(Constants.USERNAME, arguments?.getString("login"))
                Hawk.put(Constants.PASSWORD, arguments?.getString("password"))
                Hawk.put(Constants.PIN_CODE, pinCodeViewModel.savePinCode.value)
                insertUserInfo(apiResult.data!!.user)
                findNavController().navigate(R.id.action_pinCodeFragment_to_congratulationsFragment)
            }
            apiResult.error {
                connectionError.checkConnectionError(it, connectionDialog, "getToken",
                    "PinCodeFragment", "Malumotlaringiz topilmadi, qayta urinib ko'ring")
            }
        }
    }

    override fun connectDialogRefreshClicked(refreshType: String) {
        connectionDialog.dismissDialog()
        when(refreshType) {
            "getToken" -> {
                isDonePinCode()
            }
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
}