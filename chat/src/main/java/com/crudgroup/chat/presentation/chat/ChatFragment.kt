package com.crudgroup.chat.presentation.chat

import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.ClipData
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaRecorder
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.*
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.work.*
import coil.load
import com.crudgroup.chat.adapters.ChatRecyclerAdapter
import com.crudgroup.chat.adapters.QuestionAdapter
import com.crudgroup.chat.adapters.decorators.ChatItemDecorator
import com.crudgroup.chat.adapters.decorators.QuestionItemDecoration
import com.crudgroup.chat.adapters.listeners.ChatOnClickListener
import com.crudgroup.chat.adapters.listeners.OnMessageClickListener
import com.crudgroup.chat.adapters.listeners.OnQuestionClickListener
import com.crudgroup.chat.core.NavigationDeps
import com.crudgroup.chat.core.PermissionManager
import com.crudgroup.chat.core.findDependencies
import com.crudgroup.chat.di.ChatComponentViewModel
import com.crudgroup.chat.presentation.rate.RateFragment
import com.crudgroup.chat.utils.Constants.AUDIO_MEDIA_TYPE
import com.crudgroup.chat.utils.Constants.CHANNEL
import com.crudgroup.chat.utils.Constants.CHAT_IMAGE
import com.crudgroup.chat.utils.Constants.IMAGE_MEDIA_TYPE
import com.crudgroup.chat.utils.Constants.KEY_FILE_NAME
import com.crudgroup.chat.utils.Constants.KEY_FILE_TYPE
import com.crudgroup.chat.utils.Constants.KEY_FILE_URI
import com.crudgroup.chat.utils.Constants.KEY_FILE_URL
import com.crudgroup.chat.utils.Constants.PDF_MEDIA_TYPE
import com.crudgroup.chat.utils.FileDownloadState
import com.crudgroup.chat.utils.FileDownloadWorker
import com.crudgroup.chat.utils.FilePicker
import com.crudgroup.chat.utils.OnFileSelected
import com.example.chat.R
import com.example.chat.databinding.FragmentChatBinding
import com.example.chat.databinding.SendFileLayoutBinding
import dagger.Lazy
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import uz.crud.data.model.ChatData
import uz.crud.data.model.Question
import uz.crud.data.model.UserDataInfo
import uz.crud.data.utils.Constants
import java.io.*
import javax.inject.Inject
import kotlin.random.Random

class ChatFragment : Fragment() {

    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!

    private var latestTmpUri: Uri? = null

    @Inject
    lateinit var viewModelFactory: Lazy<ChatViewModel.Factory>

    private val viewModel: ChatViewModel by viewModels {
        viewModelFactory.get()
    }

    private val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        val name = coroutineContext[CoroutineName] ?: "unknown"
        val error = throwable.message
        Log.e("ChatFragment", "exceptionHandler: name = $name, error = $error")
    }
    private val scope = CoroutineScope(
        Dispatchers.Main +
                SupervisorJob() +
                exceptionHandler
    )

    private var isColored = false

    private var info: UserDataInfo? = null

    private var output: String? = null
    private var mediaRecorder: MediaRecorder? = null
    private var state: Boolean = false

    private var handler: Handler? = null

    private var channel: String? = null

    private val chatAdapter by lazy {
        ChatRecyclerAdapter(listener, onMessageClickListener)
    }

    private var questionAdapter: QuestionAdapter? = null

    private val fileDownloadState = MutableSharedFlow<FileDownloadState>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    private var filePicker: FilePicker? = null

    private var chatId: String? = null
    private var isEdit: Boolean = false
    private var position: Int = -1

    private var isClose: Boolean = false

    override fun onAttach(context: Context) {
        ViewModelProvider(this).get<ChatComponentViewModel>()
            .chatComponent.inject(this)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        info = arguments?.getSerializable("info") as UserDataInfo?
        Log.e("TAG", "onCreate: ${info?.token}")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentChatBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setDate()
        initRecyclerView()
        subscribeToFlow()
        bindQuestionAdapter()

        val file = File(Environment.getExternalStorageDirectory().absolutePath + "/medland")
        file.mkdir()

        handler = Handler(Looper.getMainLooper())
        handler?.post(runnable)

        binding.apply {
            backImage.setOnClickListener {
                requireActivity().onBackPressed()
            }
            contentFullChat.sendIv.setOnClickListener {
                val message = binding.contentFullChat.messageEt.text.trim().toString()
                if (message.isNotEmpty()) {
                    if (isEdit) {
                        viewModel.updateChat(message, chatId)
                        if (position >= 0) {
                            chatAdapter.currentList[position].name = message
                            chatAdapter.notifyItemChanged(position)
                            setEditVisibility(false)
                        }
                    } else {
                        sendMessage(message)
                    }
                }
            }
            contentFullChat.sendFileBtn.setOnClickListener {
                showDialog()
            }

            launchZoomIv.setOnClickListener {
                val charsOfRoomId = ArrayList<String>()
                val roomId = StringBuilder()
                for (i in 0 until 6 step 2) {
                    charsOfRoomId.add(Random.nextInt(97, 123).toChar().toString())
                    charsOfRoomId.add(Random.nextInt(0, 10).toString())
                }
                charsOfRoomId.shuffle()
                for (i in charsOfRoomId) {
                    roomId.append(i)
                }
                channel = roomId.toString()
                startVideChat()
            }

            doctorInfoContainer.setOnClickListener {
                Log.e("TAG", "onViewCreated: ${arguments?.getInt("type2")}")
                findDependencies<NavigationDeps>()
                    .chatNavigation.navigateToDoctorDetail(arguments)
            }

            contentFullChat.clearIv.setOnClickListener {
                setEditVisibility(false)
            }

            contentEmptyChat.questionFab.setOnClickListener {
                questionsVisibility()
            }

            contentEmptyChat.startMessageBtn.setOnClickListener {
                questionsVisibility()
            }
        }
    }

    private fun questionsVisibility() {
        if (isClose) {
            binding.contentEmptyChat.questionFab.setImageResource(R.drawable.baseline_question_mark)
        } else {
            if (questionAdapter?.currentList?.isEmpty() == true) {
                questionAdapter?.submitList(getTempQuestions())
            }
            binding.contentEmptyChat.questionFab.setImageResource(R.drawable.baseline_close_white)
        }
        isClose = !isClose
        binding.contentEmptyChat.questionsRv.isVisible = isClose
    }

    private fun startVideChat() {
        PermissionManager(
            requireActivity().activityResultRegistry,
            this
        ) {
            if (it) {
                viewModel.getFcmToken()
            }
        }.also {
            it.requestMultiplePermissions.launch(
                arrayOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.RECORD_AUDIO
                )
            )
        }
    }

    override fun onStart() {
        filePicker = FilePicker(
            activity?.activityResultRegistry,
            this,
            onFileSelected
        )
        super.onStart()
    }

    override fun onDestroyView() {
        handler?.removeCallbacks(runnable)
        handler = null
        scope.cancel()
        questionAdapter = null
        _binding = null
        super.onDestroyView()
    }

    private val onFileSelected = object : OnFileSelected {
        override fun onImageSelectedFromContent(imageUri: Uri?) {
            imageUri?.let { uri: Uri ->
                sendMessage(file = getFileFromUri(uri), mediaType = IMAGE_MEDIA_TYPE)
            }
        }

        override fun onImageSelectedFromCamera(isSuccess: Boolean) {
            if (isSuccess) {
                latestTmpUri?.let { uri ->
                    sendMessage(file = getFileFromUri(uri), mediaType = IMAGE_MEDIA_TYPE)
                }
            }
        }

        @SuppressLint("Recycle")
        override fun onPdfSelectedFromContent(pdfUri: Uri?) {
            pdfUri ?: return
            val inputStream: InputStream? =
                requireActivity().contentResolver.openInputStream(pdfUri)
            val bytesArray = ByteArray(inputStream?.available()!!)
            inputStream.read(bytesArray)

            val parent = requireContext().cacheDir
            val toWrite = File(parent, "${System.currentTimeMillis()}.pdf")
            val os = BufferedOutputStream(FileOutputStream(toWrite))
            os.write(bytesArray)
            os.close()

            sendMessage(file = toWrite, mediaType = PDF_MEDIA_TYPE)
        }
    }

    private fun initRecyclerView() {
        binding.chatRv.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, true)
            setHasFixedSize(true)
            addItemDecoration(ChatItemDecorator())
            adapter = chatAdapter
        }
    }

    private fun bindQuestionAdapter() {
        questionAdapter = QuestionAdapter(onQuestionClickListener)
        with(binding.contentEmptyChat.questionsRv) {
            setHasFixedSize(true)
            addItemDecoration(QuestionItemDecoration())
            adapter = questionAdapter
        }
    }

    private fun subscribeToFlow() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.result.collect { result ->
                    if (result.detail == "Success") {
                        binding.contentFullChat.messageEt.text.apply {
                            if (isNotEmpty()) {
                                clear()
                            }
                        }
                        getMessage()
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.chatList.collect { items: List<ChatData> ->
                    if (items.isEmpty()) {
                        with(binding) {
                            chatRv.isVisible = false
                            contentFullChat.root.isVisible = false
                            emptyLayout.isVisible = true
                            contentEmptyChat.root.isVisible = true
                        }
                    } else if (chatAdapter.currentList.size < items.size) {
                        with(binding) {
                            chatRv.isVisible = true
                            emptyLayout.isVisible = false
                            contentEmptyChat.root.isVisible = false
                            contentFullChat.root.isVisible = true
                            chatAdapter.submitList(items.reversed())
                            binding.chatRv.smoothScrollToPosition(0)
                        }
                    }
                    handler?.postDelayed(runnable, 5000)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.loading.collect { isLoading ->
                    isLoading(isLoading)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.videoChatToken.collect { token: String ->
                    if (channel != null) {
                        viewModel.sendNotification(
                            to = token,
                            title = info?.userName ?: "Bemor",
                            roomId = channel!!,
                            image = info?.userImage
                        )
                        findNavController().navigate(
                            R.id.navigateChatFragmentToVideoChatFragment,
                            bundleOf(CHANNEL to channel)
                        )
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.chatStatus.collect {
                    if (!it) {
                        showRatingDialog()
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                fileDownloadState.collect { state ->
                    when (state) {
                        is FileDownloadState.Success<*> -> {
                            val data = chatAdapter.currentList.find { it.id == state.data?.id }
                            data?.downloadedUri = state.data?.downloadedUri
                            data?.isDownloading = false
                            chatAdapter.notifyItemChanged(chatAdapter.currentList.indexOf(data))
                            viewModel.updateLocalChat(data)
                        }
                        is FileDownloadState.Running<*> -> {
                            chatAdapter.currentList
                                .find { it.id == state.data?.id }?.isDownloading = true
                        }
                        is FileDownloadState.Failed<*> -> {
                            chatAdapter.currentList
                                .find { it.id == state.data?.id }?.isDownloading = false
                        }
                    }
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setDate() {
        binding.apply {
            doctorNameTv.text = "${info?.doctorSurname} ${info?.doctorName}"
            doctorOnlineTv.text = info?.onlineTime?.replace('T', ' ')
            doctorImage.load(Constants.PDF_URL + info?.doctorImage) {
                crossfade(true)
            }
        }
    }

    private fun showRatingDialog() {
        val data = Bundle()
        data.putString("image", info?.doctorImage)
        data.putString("name", "${info?.doctorSurname} ${info?.doctorName}")
        val rateFragment = RateFragment.newInstance(data)
        rateFragment.show(parentFragmentManager, "rating")
    }

    private fun sendMessage(message: String = " ", file: File? = null, mediaType: String? = null) {
        handler?.removeCallbacks(runnable)
        viewModel.postMessage(
            info?.doctorId,
            info?.userId,
            message,
            file,
            mediaType
        )
    }

    private fun getMessage() {
        scope.launch {
            try {
                delay(700L)
                viewModel.getMessage(
                    info?.doctorId,
                    info?.userId,
                )
            } catch (e: Exception) {
                Log.e("ChatFragment", "getMessage: ${e.printStackTrace()}")
            }

        }
    }

    private fun takeImage() {
        lifecycleScope.launchWhenStarted {
            getTmpFileUri().let { uri ->
                latestTmpUri = uri
                filePicker?.pickImageFromCamera(uri)
            }
        }
    }

    private fun getTmpFileUri(): Uri {
        val tmpFile = File.createTempFile("tmp_image_file", ".png").apply {
            createNewFile()
            deleteOnExit()
        }

        return FileProvider.getUriForFile(
            context?.applicationContext!!,
            "com.crudgroup.medklinikademo",
            tmpFile
        )
    }

    private fun showDialog() {
        val dialog = context?.let { Dialog(it) }
        val dialogBinding: SendFileLayoutBinding =
            SendFileLayoutBinding.inflate(layoutInflater, binding.root, false)
        dialog?.setContentView(dialogBinding.root)
        dialogBinding.apply {
            cameraIv.setOnClickListener {
                PermissionManager(
                    requireActivity().activityResultRegistry,
                    this@ChatFragment
                ) {
                    if (it) {
                        dialog?.dismiss()
                        takeImage()
                    }
                }.also {
                    it.requestMultiplePermissions.launch(
                        arrayOf(
                            Manifest.permission.CAMERA,
                            Manifest.permission.RECORD_AUDIO
                        )
                    )
                }
            }
            galleryIv.setOnClickListener {
                dialog?.dismiss()
                filePicker?.pickImageFromContent()
            }
            pdfIv.setOnClickListener {
                dialog?.dismiss()
                filePicker?.pickPdfFromContent()
            }
            voiceIv.setOnClickListener { view: View ->
                if (isColored) {
                    isColored = false
                    context?.let { ContextCompat.getColor(it, R.color.white) }
                        ?.let { view.setBackgroundColor(it) }
                    dialog?.dismiss()
                    stopRecording()
                } else {
                    PermissionManager(
                        requireActivity().activityResultRegistry,
                        this@ChatFragment
                    ) { result ->
                        if (result) {
                            isColored = true
                            context?.let { ContextCompat.getColor(it, R.color.red) }
                                ?.let { view.setBackgroundColor(it) }
                            startRecording()
                        }
                    }.also {
                        it.requestMultiplePermissions.launch(
                            arrayOf(
                                Manifest.permission.CAMERA,
                                Manifest.permission.RECORD_AUDIO
                            )
                        )
                    }
                }
            }
        }
        dialog?.show()
    }

    private val runnable = Runnable { getMessage() }

    private fun startRecording() {
//        output = Environment.getExternalStorageDirectory().absolutePath + "/medland/${System.currentTimeMillis()}.mp3"
        val a = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC)
        val b = File(a, "${System.currentTimeMillis()}.mp3")
//        b.mkdir()
        b.createNewFile()
        output = b.absolutePath
        mediaRecorder = MediaRecorder()

        mediaRecorder?.setAudioSource(MediaRecorder.AudioSource.MIC)
        mediaRecorder?.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
        mediaRecorder?.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
        mediaRecorder?.setOutputFile(output)
        try {
            mediaRecorder?.prepare()
            mediaRecorder?.start()
            state = true
            Toast.makeText(requireContext(), "Recording started!", Toast.LENGTH_SHORT).show()
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun sendAudio() {
        val file = output?.let { File(it) }
        sendMessage(file = file, mediaType = AUDIO_MEDIA_TYPE)
    }

    private fun stopRecording() {
        if (state) {
            mediaRecorder?.stop()
            mediaRecorder?.release()
            mediaRecorder = null
            state = false
            sendAudio()
        } else {
            Toast.makeText(requireContext(), "You are not recording right now!", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun isLoading(isLoading: Boolean) {
        binding.progressBar.isVisible = isLoading
        binding.loadingView.isVisible = isLoading
    }

    private val listener = object : ChatOnClickListener {
        override fun onClick(item: ChatData) {
            if (item.isDownloading) return
            if (item.type == Constants.IMAGE_TYPE) {
                findNavController().navigate(
                    R.id.navigateChatFragmentToImageFragment, bundleOf(
                        CHAT_IMAGE to item.url
                    )
                )
            }
            if (item.downloadedUri.isNullOrEmpty()) {
                if (item.type in 3..4) {
                    startDownloadingFile(file = item)
                }
            } else if (item.type == Constants.PDF_TYPE) {
                openPdf(item.name)
            }
        }
    }

    private val onMessageClickListener = object : OnMessageClickListener {
        override fun onClick(adapterPosition: Int, message: String, id: String?) {
            position = adapterPosition
            chatId = id
            setEditVisibility(true, message)
        }
    }

    private val onQuestionClickListener = object : OnQuestionClickListener {
        override fun onClick(text: String) {
            sendMessage(text)
        }
    }

    private fun setEditVisibility(isVisible: Boolean, text: String? = null) {
        isEdit = isVisible
        with(binding.contentFullChat) {
            editLayout.isVisible = isVisible
            editTv.isVisible = isVisible
            editTv.text = text
            clearIv.isVisible = isVisible
        }

        if (isVisible) {
            with(binding.contentFullChat.messageEt) {
                setText(text)
                requestFocus()
                val imm =
                    activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
                imm?.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
                text?.length?.let { setSelection(it) }
            }
        } else {
            with(binding.contentFullChat.messageEt) {
                setText(text)
                val imm =
                    activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
                imm?.showSoftInput(this, InputMethodManager.HIDE_IMPLICIT_ONLY)
            }
        }
    }

    /*private val invitationOnClickListener = object : InvitationOnClickListener {
        override fun onClick(roomId: String) {
            channel = roomId
            askPermission(
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO
            ) {
                findNavController().navigate(
                    R.id.navigateChatFragmentToVideoChatFragment,
                    bundleOf(CHANNEL to roomId)
                )
            }.onDeclined { e ->
                if (e.hasDenied()) {
                    AlertDialog.Builder(requireContext())
                        .setMessage("Please accept our permissions")
                        .setPositiveButton("yes") { _, _ ->
                            e.askAgain()
                        }
                        .setNegativeButton("no") { dialog, _ ->
                            dialog.dismiss()
                        }
                        .show()
                }

                if (e.hasForeverDenied()) {
                    e.goToSettings()
                }
            }
        }
    } */

    private fun openPdf(downloadedUri: String?) {
        val file =
            File(Environment.getExternalStorageDirectory(), "Download/$downloadedUri")
        val uriPdfPath = FileProvider.getUriForFile(
            requireContext(),
            requireContext().applicationContext.packageName,
            file
        )
        val intent = Intent(Intent.ACTION_VIEW)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        intent.clipData = ClipData.newRawUri("", uriPdfPath)
        intent.setDataAndType(uriPdfPath, "application/pdf")
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        try {
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(
                requireContext(),
                "Can't open Pdf",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun getFileFromUri(uri: Uri): File? {
        var inputStream = context?.contentResolver?.openInputStream(uri)
        val onlyBoundsOptions = BitmapFactory.Options()
        onlyBoundsOptions.inJustDecodeBounds = true
        onlyBoundsOptions.inDither = true
        onlyBoundsOptions.inPreferredConfig = Bitmap.Config.ARGB_8888
        BitmapFactory.decodeStream(inputStream, null, onlyBoundsOptions)
        inputStream?.close()

        val originalWith = onlyBoundsOptions.outWidth
        val originalHeight = onlyBoundsOptions.outHeight
        if ((originalWith == -1) || (originalHeight == -1)) {
            return null
        }

        val hh = 800f
        val ww = 480f
        var be = 1
        if (originalWith > originalHeight && originalWith > ww) {
            be = (originalWith / ww).toInt()
        } else if (originalWith < originalHeight && originalHeight > hh) {
            be = (originalHeight / hh).toInt()
        }
        if (be <= 0) {
            be = 1
        }

        val bitmapOptions = BitmapFactory.Options()
        bitmapOptions.inSampleSize = be
        bitmapOptions.inDither = true
        bitmapOptions.inPreferredConfig = Bitmap.Config.ARGB_8888
        inputStream = context?.contentResolver?.openInputStream(uri)
        val bitmap = BitmapFactory.decodeStream(inputStream, null, bitmapOptions)
        inputStream?.close()

        return compressImage(bitmap)
    }

    private fun compressImage(image: Bitmap?): File {
        val baos = ByteArrayOutputStream()
        image?.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        var options = 100
        while (baos.toByteArray().size / 1024 > 100) {
            baos.reset()
            image?.compress(Bitmap.CompressFormat.JPEG, options, baos)
            options -= 10
        }

        val fileName = System.currentTimeMillis().toString() + ".png"
        val file = File(context?.cacheDir, fileName)
        val stream = FileOutputStream(file)

        stream.write(baos.toByteArray())
        stream.close()
        stream.flush()

        return file
    }

    private fun startDownloadingFile(file: ChatData) {
        val data = Data.Builder()
        data.apply {
            putString(KEY_FILE_NAME, file.name)
            putString(KEY_FILE_URL, file.url)
            putInt(KEY_FILE_TYPE, file.type)
        }

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresStorageNotLow(true)
            .setRequiresBatteryNotLow(true)
            .build()

        val fileDownloadWorker = OneTimeWorkRequestBuilder<FileDownloadWorker>()
            .setConstraints(constraints)
            .setInputData(data.build())
            .build()

        val workManager = WorkManager.getInstance(binding.root.context)

        workManager.enqueueUniqueWork(
            "oneFileDownloadWork_${System.currentTimeMillis()}",
            ExistingWorkPolicy.KEEP,
            fileDownloadWorker
        )

        workManager.getWorkInfoByIdLiveData(fileDownloadWorker.id)
            .observe(this) { info ->
                info?.let {
                    when (it.state) {
                        WorkInfo.State.SUCCEEDED -> {
                            file.downloadedUri = it.outputData.getString(KEY_FILE_URI)
                            fileDownloadState.tryEmit(
                                FileDownloadState.Success<String>(file)
                            )
                        }
                        WorkInfo.State.FAILED -> {
                            fileDownloadState.tryEmit(FileDownloadState.Failed<String>(file.id))
                        }
                        WorkInfo.State.RUNNING -> {
                            fileDownloadState.tryEmit(FileDownloadState.Running<String>(file.id))
                        }
                        else -> {
                            fileDownloadState.tryEmit(FileDownloadState.Failed<String>(file.id))
                        }
                    }
                }
            }
    }

    private fun getTempQuestions(): List<Question> {
        val list = arrayListOf<Question>()

        list.add(
            Question(
                question = "Test1"
            )
        )

        list.add(
            Question(
                question = "Test2"
            )
        )

        list.add(
            Question(
                question = "Test3"
            )
        )

        list.add(
            Question(
                question = "Test4"
            )
        )

        return list
    }
}