package com.med.medland.presentation.fragment.chat

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.med.medland.databinding.FragmentChatBinding
import com.med.medland.domain.model.Doctor
import com.med.medland.presentation.app.ApplicationController
import com.med.medland.presentation.fragment.chat.adapter.DoctorsViewModel
import com.med.medland.presentation.fragment.chat.adapter.OnlineDoctorsAdapter
import com.med.medland.presentation.fragment.chat.adapter.RecentChatsAdapter
import kotlinx.coroutines.launch
import uz.crud.data.model.UserDataInfo
import javax.inject.Inject


class ChatFragment : Fragment() {

    private var _binding: FragmentChatBinding? = null
    private val binding: FragmentChatBinding get() = requireNotNull(_binding)

    @Inject
    lateinit var viewModelFactory: dagger.Lazy<DoctorsViewModel.Factory>
    private val viewModel by viewModels<DoctorsViewModel> {
        viewModelFactory.get()
    }

    private val onlineDoctorsAdapter: OnlineDoctorsAdapter by lazy(LazyThreadSafetyMode.NONE) {
        OnlineDoctorsAdapter()
    }
    private val recentMessagedDoctorsAdapter: RecentChatsAdapter by lazy(LazyThreadSafetyMode.NONE) {
        RecentChatsAdapter()
    }

    override fun onAttach(context: Context) {
        (requireActivity().application as ApplicationController).appComponent.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindAdapters()

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.onlineDoctors.collect { doctors: List<Doctor> ->
                    Log.e("TAG", "onViewCreated: $doctors", )
                    onlineDoctorsAdapter.submitList(doctors)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.recentMessagedDoctors.collect { doctors: List<Doctor> ->
                    Log.e("TAG", "onViewCreated: $doctors", )
                    recentMessagedDoctorsAdapter.submitList(doctors)
                }
            }
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun bindAdapters() {
        binding.onlineDoctorsRv.adapter = onlineDoctorsAdapter
        binding.recentChatsRv.adapter = recentMessagedDoctorsAdapter
    }

    private fun openChat() {
        val bundle = Bundle()
        bundle.putSerializable(
            "info", UserDataInfo(
                doctorSurname = "",
                doctorName = "",
                doctorImage = "",
                onlineTime = "",
                doctorId = "",
                userId = "",
                userName = "",
                userImage = "",
                token = "",
                categoryId = 0,
                type = ""
            )
        )
//        bundle.putSerializable("aboutDoctor", doctor)
//        bundle.putInt("type2", 1)
//        Navigation.findNavController(binding.root)
//            .navigate(R.id.action_chatFragment_to_chat_nav_graph, bundle)
    }
}