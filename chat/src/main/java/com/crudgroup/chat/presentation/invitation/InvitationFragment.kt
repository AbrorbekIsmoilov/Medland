package com.crudgroup.chat.presentation.invitation

import android.media.Ringtone
import android.media.RingtoneManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import coil.load
import com.crudgroup.chat.core.NavigationDeps
import com.crudgroup.chat.core.findDependencies
import com.crudgroup.chat.utils.Constants
import com.example.chat.R
import com.example.chat.databinding.FragmentInvitationBinding
import com.google.android.material.snackbar.Snackbar

class InvitationFragment : Fragment() {

    private var _binding: FragmentInvitationBinding? = null
    private val binding get() = requireNotNull(_binding)

    private var defaultRingtone: Ringtone? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentInvitationBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val name = arguments?.getString("name")
        val image = arguments?.getString("image")

        binding.apply {
            if (!image.isNullOrEmpty()) {
                accountIv.load(Constants.IMAGE_URL + image) {
                    crossfade(true)
                }
            }
            nameTv.text = name
        }

        binding.apply {
            acceptBtn.setOnClickListener {
                if (arguments != null) {
                    findDependencies<NavigationDeps>()
                        .chatNavigation.navigateToVideoChatFragment(
                            arguments
                        )
                } else {
                    Snackbar.make(
                        binding.root,
                        getString(R.string.not_found),
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }

            denialBtn.setOnClickListener {
                requireActivity().onBackPressed()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        playRingtone()
    }

    override fun onStop() {
        super.onStop()
        defaultRingtone?.stop()
        defaultRingtone = null
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun playRingtone() {
        val defaultRingtoneUri: Uri = RingtoneManager.getActualDefaultRingtoneUri(
            requireActivity().applicationContext,
            RingtoneManager.TYPE_RINGTONE
        )
        defaultRingtone = RingtoneManager.getRingtone(activity, defaultRingtoneUri)
        defaultRingtone?.play()
    }
}