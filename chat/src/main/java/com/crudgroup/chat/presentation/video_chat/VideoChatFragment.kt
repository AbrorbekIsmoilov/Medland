package com.crudgroup.chat.presentation.video_chat

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.crudgroup.chat.utils.Constants.CHANNEL
import com.example.chat.databinding.FragmentVideoChatBinding

class VideoChatFragment : Fragment() {

    private var _binding: FragmentVideoChatBinding? = null
    private val binding get() = requireNotNull(_binding)

//    @OptIn(ExperimentalUnsignedTypes::class)
//    private var agView: AgoraVideoViewer? = null

//     private var eventHandler = object : IRtcEngineEventHandler() {
//         override fun onLeaveChannel(stats: RtcStats?) {
//             super.onLeaveChannel(stats)
//             //parentFragmentManager.popBackStack()
//             findNavController().popBackStack()
//         }
//     }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentVideoChatBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val channel = arguments?.getString(CHANNEL)
        if (!channel.isNullOrEmpty()) {
//            initializeAndJoinChannel(channel)
        }
    }

    @OptIn(ExperimentalUnsignedTypes::class)
    override fun onDestroyView() {
//        agView?.leaveChannel()
//        agView?.agkit?.removeHandler(eventHandler)
        _binding = null
        super.onDestroyView()
    }

    @SuppressLint("SetTextI18n")
    @OptIn(ExperimentalUnsignedTypes::class)
            /*private fun initializeAndJoinChannel(channel: String) {
        //        try {
        //            agView = AgoraVideoViewer(
        //                requireContext(), AgoraConnectionData(APP_ID),
        //            )
        //            agView?.agkit?.addHandler(eventHandler)
        //        } catch (e: Exception) {
        //            Log.e("TAG", "Could not initialize AgoraVideoViewer. Check your App ID is valid.")
        //            Log.e("TAG", "initializeAndJoinChannel", e)
        //            return
        //        }
                binding.root.addView(
                    agView, FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT
                    )
                )
                if (AgoraVideoViewer.requestPermissions(requireContext())) {
                    agView!!.join(channel)
                    //agView?.agkit?.addHandler(agg)
                } else {
                    val joinButton = Button(requireContext())
                    joinButton.text = "Allow Camera and Microphone, then click here"
                    joinButton.setOnClickListener {
                        if (AgoraVideoViewer.requestPermissions(requireContext())) {
                            (joinButton.parent as ViewGroup).removeView(joinButton)
                            agView!!.join(channel)
                            //agView?.agkit?.addHandler(agg)
                        }
                    }
                    joinButton.setBackgroundColor(Color.GREEN)
                    joinButton.setTextColor(Color.RED)
                    binding.root.addView(
                        joinButton, FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, 300)
                    )
                }
            }*/

    fun newInstance(channel: String) =
        VideoChatFragment().apply {
            arguments = Bundle().apply {
                putString(CHANNEL, channel)
            }
        }
}