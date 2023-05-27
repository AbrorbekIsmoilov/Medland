package com.med.medland.presentation.fragment.chatFragment

import android.os.Bundle
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.med.medland.R
import com.med.medland.databinding.FragmentChatBinding
import com.med.medland.presentation.fragment.chatFragment.adapter.OnlineDoctorsAdapter
import com.med.medland.presentation.fragment.chatFragment.adapter.RecentChatsAdapter
import uz.crud.data.model.UserDataInfo


class ChatFragment : Fragment() {

    private lateinit var binding : FragmentChatBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.onlineDoctorsRv.adapter = OnlineDoctorsAdapter()
        binding.recentChatsRv.adapter = RecentChatsAdapter()
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
        bundle.putInt("type2", 1)
        Navigation.findNavController(binding.root)
            .navigate(R.id.action_chatFragment_to_chat_nav_graph, bundle)
    }
}