package com.med.medland.presentation.fragment.profileFragment

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.findNavController
import com.med.medland.R
import com.med.medland.data.room.database.UserDataBase
import com.med.medland.data.room.table.MyInfoTable
import com.med.medland.databinding.DialogGenderSelectBinding
import com.med.medland.databinding.FragmentMyProfileBinding
import com.med.medland.databinding.MyProfilNumberBinding
import com.med.medland.presentation.fragment.otherComponents.PhoneMaskManager
import com.med.medland.presentation.fragment.otherComponents.adapter.ProfileCountrySelectAdapter
import com.med.medland.presentation.fragment.otherComponents.dialog.DatePickerDialog
import com.med.medland.presentation.fragment.otherComponents.dialog.SelectCountryCodeDialog
import com.med.medland.presentation.fragment.otherComponents.model.PhoneMaskModel
import com.med.medland.presentation.fragment.profileFragment.dialog.ProfilSelectCountryCodeDialog
import com.santalu.maskara.Mask
import com.santalu.maskara.MaskChangedListener
import com.santalu.maskara.MaskStyle
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date


@Suppress("UNREACHABLE_CODE")
class MyProfileFragment : Fragment(),ProfileCountrySelectAdapter.ProfileSelectList {

    private lateinit var binding : FragmentMyProfileBinding
    private lateinit var datePickerDialog: DatePickerDialog
    private lateinit var selectCountryCodeDialog: SelectCountryCodeDialog
    private var phoneMaskManager = PhoneMaskManager()
    private var profileCountrySelectAdapter = ProfileCountrySelectAdapter(phoneMaskManager.loadPhoneMusk(), this, true)
    private var phoneNumberLength = 12
     val userDataBase: UserDataBase by lazy {
         UserDataBase.getDataBase(requireContext())
     }
    val list = ArrayList<MyInfoTable>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        datePickerDialog = DatePickerDialog(requireContext())
    }
    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n", "SuspiciousIndentation")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentMyProfileBinding.inflate(inflater, container, false)

        genderClick()
        isGallery()


            val alertDialog = AlertDialog.Builder(requireContext())
            val dialogView = MyProfilNumberBinding.inflate(layoutInflater)
            alertDialog.setView(dialogView.root)
            val dialog = alertDialog.create()
            binding.profileCallNumberTv.setOnClickListener {
                dialog.show()

                dialogView.myProfileNumberSave.setOnClickListener {
                    binding.profileCallNumberTv.text = "${dialogView.loginCountryNumTv.text} ${dialogView.phoneNumberEt.text}"
//                    findNavController().navigate(R.id.action_myProfileFragment_to_signUpThreeFragment2)
                    Toast.makeText(context, "Bu oyna xali ishga tushmagan", Toast.LENGTH_SHORT).show()
                        onDestroy()
//
                        dialog.cancel()

                }

                binding.profileBrithDate.setOnClickListener {
                    datePickerDialog.showCalendarDialogs(binding.profileBrithDate)
                }

        }

        binding.logOut.setOnClickListener {
            findNavController().navigate(R.id.action_myProfileFragment_to_loginFragment2)

        }

        binding.ilovaSettingis.setOnClickListener {
            findNavController().navigate(R.id.action_myProfileFragment_to_appSettingsFragment)
        }
        binding.diseaseBtn.setOnClickListener {
            findNavController().navigate(R.id.action_myProfileFragment_to_medicalHistoryFragment)
        }
        binding.genderEdit.setOnClickListener {

        }



        return binding.root
    }

 private fun genderClick(){

     val alertDialog = AlertDialog.Builder(requireContext())
     val dialogView = DialogGenderSelectBinding.inflate(layoutInflater)
     alertDialog.setView(dialogView.root)
     val dialog = alertDialog.create()
     binding.profileGenderTv.setOnClickListener {
         dialog.show()

         dialogView.signUpGenManBtn.setOnClickListener {
             binding.profileGenderTv.text = "${dialogView.signUpGenManBtn.text}"

//             findNavController().navigate(R.id.action_myProfileFragment_to_signUpThreeFragment2)
             onDestroy()
//
             dialog.cancel()

         }
         dialogView.signUpGenGirlBtn.setOnClickListener {
             binding.profileGenderTv.text = "${dialogView.signUpGenGirlBtn.text}"

//             findNavController().navigate(R.id.action_myProfileFragment_to_signUpThreeFragment2)
             onDestroy()
//
             dialog.cancel()

         }

     }
 }


    private fun countryNumber(selected: PhoneMaskModel){



    }

    override fun selectedCountry(selected: PhoneMaskModel) {
        val alertDialog = AlertDialog.Builder(requireContext())
        val dialogView = MyProfilNumberBinding.inflate(layoutInflater)
        alertDialog.setView(dialogView.root)
        val dialog = alertDialog.create()
        dialogView.phoneNumberEt.setOnClickListener {

        }
        binding.profileCallNumberTv.setOnClickListener {
            dialogView.loginCountryNumTv.setOnClickListener {
                dialogView.loginCountryNumTv.text = selected.countryCode
                dialogView.phoneNumberEt.hint = selected.mask
                phoneNumberLength = selected.mask.trim().length
                val value = selected.mask.replace("0", "-")
                val mask = Mask(value, '-', MaskStyle.COMPLETABLE)
                val listener = MaskChangedListener(mask)
                dialogView.phoneNumberEt.addTextChangedListener(listener)
                selectCountryCodeDialog.dismissDialog()
            }
        }
    }

    fun isGallery() {

        binding.imageView.setOnClickListener {
            getImageContent.launch("image/*")
        }
    }
       @SuppressLint("SimpleDateFormat")
       private val getImageContent = registerForActivityResult(ActivityResultContracts.GetContent()){
            it ?: return@registerForActivityResult
            binding.imageView.setImageURI(it)
           val inputStream = requireActivity().contentResolver.openInputStream(it)
           val vaqt = SimpleDateFormat("yyyyMMdd hh:mm:ss").format(Date())
           val file = File(requireActivity().filesDir, "$vaqt.jpg")
           val fileOutStream = FileOutputStream(file)
           inputStream?.copyTo(fileOutStream)
           fileOutStream.close()
           inputStream?.close()
        }
    }
