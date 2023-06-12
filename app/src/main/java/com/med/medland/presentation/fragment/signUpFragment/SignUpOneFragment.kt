package com.med.medland.presentation.fragment.signUpFragment

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.view.marginBottom
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.med.medland.R
import com.med.medland.data.api.retrofitCreate.ApiResult
import com.med.medland.data.api.retrofitCreate.ApiResult.Companion.error
import com.med.medland.data.api.retrofitCreate.ApiResult.Companion.success
import com.med.medland.data.locale.Constants
import com.med.medland.databinding.FragmentSignUpOneBinding
import com.med.medland.presentation.fragment.otherComponents.GetImageBottomSheet
import com.med.medland.presentation.fragment.otherComponents.MyCustomSnackBar
import com.med.medland.presentation.fragment.otherComponents.PhoneMaskManager
import com.med.medland.presentation.fragment.otherComponents.SetCustomization.margin
import com.med.medland.presentation.fragment.otherComponents.adapter.SelectCountryCodeAdapter
import com.med.medland.presentation.fragment.otherComponents.dialog.DatePickerDialog
import com.med.medland.presentation.fragment.otherComponents.dialog.SelectCountryCodeDialog
import com.med.medland.presentation.fragment.otherComponents.dialog.SelectRegionDialog
import com.med.medland.presentation.fragment.otherComponents.model.GetDistrictModel
import com.med.medland.presentation.fragment.otherComponents.model.GetRegionModel
import com.med.medland.presentation.fragment.otherComponents.model.PhoneMaskModel
import com.med.medland.presentation.fragment.signUpFragment.model.SignUpViewModel


class SignUpOneFragment : Fragment(), SelectCountryCodeAdapter.SelectedCountryListener, SelectRegionDialog.SelectRegionListener {

    private lateinit var binding : FragmentSignUpOneBinding
    private lateinit var selectCountryCodeDialog: SelectCountryCodeDialog
    private lateinit var selectRegionDialog: SelectRegionDialog
    private lateinit var getImageBottomSheet: GetImageBottomSheet
    private lateinit var datePickerDialog: DatePickerDialog
    private lateinit var customSnackBar: MyCustomSnackBar
    private lateinit var viewModel: SignUpViewModel
    private var phoneMaskManager = PhoneMaskManager()
    private var regionList = ArrayList<GetRegionModel>()
    private var selectCountryCodeAdapter = SelectCountryCodeAdapter(phoneMaskManager.loadPhoneMusk(), this, false)
    private var genMan = true
    private var countryUzbekistan = true
    private var regionId = 0
    private var districtId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[SignUpViewModel :: class.java]
        datePickerDialog = DatePickerDialog(requireContext())
        selectRegionDialog = SelectRegionDialog(requireContext(), this)
        viewModel.getRegions()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentSignUpOneBinding.inflate(inflater, container, false)
        binding.signUpSelectedImage.setImageResource(R.drawable.carton_man_image_one)
        Glide.with(requireContext()).load("https://cdn-icons-png.flaticon.com/128/330/330495.png")
            .error(R.drawable.ic_online).into(binding.selectedCountryFlagImg)
        initObservers()
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.selectedCountryTv.setOnClickListener {
            selectCountryCodeDialog = SelectCountryCodeDialog(requireContext(),
                selectCountryCodeAdapter, phoneMaskManager, false)
            selectCountryCodeDialog.showDialog()
        }

        binding.selectedRegionTv.setOnClickListener {
            selectRegionDialog.showDialog(regionList)
        }

        binding.signUpChangeImageBtn.setOnClickListener {
            getImageBottomSheet = GetImageBottomSheet()
            getImageBottomSheet.setSelectedImage(binding.signUpSelectedImage)
            getImageBottomSheet.show(childFragmentManager, "Get Image")
        }

        binding.signUpGenManBtn.setOnClickListener {
            if (!genMan) {
                binding.signUpGenManBtn.setBackgroundResource(R.drawable.gender_blue_background)
                binding.signUpGenGirlBtn.setBackgroundResource(R.drawable.gender_gray_background)
                binding.signUpGenManBtn.setTextColor(Color.WHITE)
                binding.signUpGenGirlBtn.setTextColor(Color.BLACK)
                genMan = true
            }
        }

        binding.signUpGenGirlBtn.setOnClickListener {
            if (genMan) {
                binding.signUpGenGirlBtn.setBackgroundResource(R.drawable.gender_blue_background)
                binding.signUpGenManBtn.setBackgroundResource(R.drawable.gender_gray_background)
                binding.signUpGenManBtn.setTextColor(Color.BLACK)
                binding.signUpGenGirlBtn.setTextColor(Color.WHITE)
                genMan = false
            }
        }

        binding.signUpOneNextBtn.setOnClickListener {
            customSnackBar = MyCustomSnackBar(it.rootView, layoutInflater)
            val bundle = Bundle()
            if (filledInformation() && countryUzbekistan) {
                if (regionId != 0 && districtId !=0) {
                    bundle.putString("user_name", binding.signUpFirstnameEt.text.toString())
                    bundle.putString("user_brith_date", binding.etBrithDate.text.toString())
                    bundle.putString("countryName", binding.selectedCountryTv.text.toString())
                    bundle.putInt("region_id", regionId)
                    bundle.putInt("district_id", districtId)
                    bundle.putString("address", binding.selectedRegionTv.text.toString())
                    if (genMan) bundle.putString("genderReveal","Erkak")
                    else bundle.putString("genderReveal","Ayol")
                    findNavController().navigate(R.id.action_signUpOneFragment_to_signUpTwoFragment, bundle)
                }
                else customSnackBar.showErrorSnack("Viloyatingizni belgilang !")
            }
            else if (filledInformation() && !countryUzbekistan) {
                bundle.putString("user_name", binding.signUpFirstnameEt.text.toString())
                bundle.putString("user_brith_date", binding.etBrithDate.text.toString())
                bundle.putString("countryName", binding.selectedCountryTv.text.toString())
                bundle.putInt("region_id", 0)
                bundle.putInt("district_id", 0)
                bundle.putString("address", binding.selectedCountryTv.text.toString())
                if (genMan) bundle.putString("genderReveal","Erkak")
                else bundle.putString("genderReveal","Ayol")
                findNavController().navigate(R.id.action_signUpOneFragment_to_signUpTwoFragment, bundle)
            }
        }


        binding.etBrithDate.setOnClickListener {
            datePickerDialog.showCalendarDialog(binding.etBrithDate)
        }

        binding.signUpSignInBtn.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun initObservers() {
        viewModel.getRegionsLiveData.observe(viewLifecycleOwner) { apiResult ->
            apiResult.success {
                regionList.clear()
                regionList.addAll(it!!)
            }
            apiResult.error {
                Log.e(Constants.SIGNUP2_FRAGMENT, "getRegions: ${it?.localizedMessage}")
            }
        }
    }

    private fun filledInformation() : Boolean {

        return if (binding.signUpFirstnameEt.text.toString().isNotEmpty() && binding.etBrithDate.text.toString().isNotEmpty() ) {
            true
        }
        else {
            if (binding.signUpFirstnameEt.text.toString().isEmpty()) {
                customSnackBar.showErrorSnack("Ismingizni kiriting !")
                binding.signUpFirstnameEt.requestFocus()
            } else {
                customSnackBar.showErrorSnack("Tug'ilgan sanangizni kiriting !")
            }

            false
        }
    }

    override fun selectedCountry(selected: PhoneMaskModel) {
        binding.selectedCountryTv.text = selected.name
        Glide.with(requireContext()).load(selected.imageUrl).error(R.drawable.ic_online).into(binding.selectedCountryFlagImg)
        if (selected.countryCode == "+998") {
            countryUzbekistan = true
            binding.selectedRegionTv.visibility = View.VISIBLE
            binding.selectedRegionImg.visibility = View.VISIBLE
            binding.selectedCountryTv.margin(null, null,null,15F)
        }
        else {
            countryUzbekistan = false
            binding.selectedRegionTv.visibility = View.GONE
            binding.selectedRegionImg.visibility = View.GONE
            binding.selectedCountryTv.margin(null, null,null,30F)
        }
        selectCountryCodeDialog.dismissDialog()
    }

    @SuppressLint("SetTextI18n")
    override fun selectedRegionIdAndDistrictId(regionName: String, regionId: Int, districtName: String, districtId: Int) {
        this.regionId = regionId
        this.districtId = districtId
        binding.selectedRegionTv.text = "$regionName / $districtName"
    }
}