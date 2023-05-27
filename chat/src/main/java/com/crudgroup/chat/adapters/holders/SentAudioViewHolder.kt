package com.crudgroup.chat.adapters.holders

import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.crudgroup.chat.adapters.listeners.ChatOnClickListener
import com.crudgroup.chat.utils.AudiBehavior
import com.crudgroup.chat.utils.ChatAudioPlayer
import com.example.chat.R
import com.example.chat.databinding.ItemSentAudioBinding
import uz.crud.data.model.ChatData

class SentAudioViewHolder(
    private val itemBinding: ItemSentAudioBinding
) : RecyclerView.ViewHolder(itemBinding.root), AudiBehavior {

    private var handler: Handler? = null

    private var mediaPlayer: MediaPlayer? = null
    private var isStopped = false

    fun bind(item: ChatData, listener: ChatOnClickListener) {
        itemBinding.apply {
            val date = item.date?.replace('T', ' ')
            dateTv.text = date
            if (item.downloadedUri.isNullOrEmpty()) {
                sentAudioIv.load(R.drawable.ic_arrow_downward) {
                    crossfade(true)
                }
            } else {
                onAudioStopped()
            }
            sentAudioIv.setOnClickListener {
                if (item.downloadedUri.isNullOrEmpty()) {
                    listener.onClick(item)
                } else {
                    if (mediaPlayer?.isPlaying == true) {
                        mediaPlayer?.pause()
                        isStopped = true
                        onAudioStopped()
                        handler?.removeCallbacks(runnable)
                    } else {
                        if (isStopped) {
                            mediaPlayer?.start()
                        } else if (!item.name.isNullOrEmpty()) {
                            mediaPlayer = ChatAudioPlayer.playAudio(
                                itemBinding.root.context,
                                item.name!!,
                                item.downloadedUri!!,
                                this@SentAudioViewHolder,
                                { duration: Int ->
                                    itemBinding.seekBarForAudio.apply {
                                        progress = 0
                                        max = duration
                                        setOnSeekBarChangeListener(seekBarChangeListener)
                                    }
                                    handler = Handler(Looper.getMainLooper())
                                }
                            ) {
                                handler?.removeCallbacks(runnable)
                                itemBinding.apply {
                                    seekBarForAudio.progress = 0
                                    onAudioStopped()
                                }
                            }
                        }
                        onAudioPlayed()
                        handler?.post(runnable)
                    }
                }
            }
        }
    }

    override fun clearResources() {
        mediaPlayer?.stop()
        mediaPlayer = null

        handler?.removeCallbacks(runnable)
        handler = null

        itemBinding.apply {
            seekBarForAudio.progress = 0
            onAudioStopped()
        }
    }

    private fun onAudioStopped() {
        itemBinding.sentAudioIv.load(R.drawable.ic_play) {
            crossfade(true)
        }
    }

    private fun onAudioPlayed() {
        itemBinding.sentAudioIv.load(R.drawable.ic_pause) {
            crossfade(true)
        }
    }

    private val runnable = object : Runnable {
        override fun run() {
            itemBinding.seekBarForAudio.progress = mediaPlayer?.currentPosition ?: 0
            handler?.postDelayed(this, 500)
        }
    }

    private val seekBarChangeListener = object : SeekBar.OnSeekBarChangeListener {
        override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            if (fromUser) {
                mediaPlayer?.seekTo(progress)
            }
        }

        override fun onStartTrackingTouch(p0: SeekBar?) {

        }

        override fun onStopTrackingTouch(p0: SeekBar?) {

        }
    }

    companion object {
        fun create(parent: ViewGroup): SentAudioViewHolder =
            SentAudioViewHolder(
                ItemSentAudioBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
    }
}