package com.example.playlistmaker.presentation.create

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.ColorRes
import androidx.core.net.toUri
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.CreatePlaylistFragmentBinding
import com.example.playlistmaker.domain.model.Playlist
import com.example.playlistmaker.utils.setImage
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.markodevcic.peko.PermissionResult
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.io.FileOutputStream

class CreatePlaylistFragment : Fragment() {

    companion object {
        private const val QUALITY_IMAGE = 30
    }

    private var _binding: CreatePlaylistFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CreatePlaylistViewModel by viewModel()

    private lateinit var pickMedia: ActivityResultLauncher<PickVisualMediaRequest>
    private var confirmDialog: MaterialAlertDialogBuilder? = null
    private var imageUri: Uri? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = CreatePlaylistFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListeners()
        initObservers()
        initPickMediaRegister()

        confirmDialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle("Завершить создание плейлиста?") // Заголовок диалога
            .setMessage("Все несохраненные данные будут потеряны") // Описание диалога
            .setNeutralButton("Отмена") { _, _ -> }
            .setPositiveButton("Завершить") { dialog, which ->
                findNavController().navigateUp()
            }
    }

    private fun initPickMediaRegister() {
        pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {

                val cornerRadius =
                    requireContext().resources.getDimensionPixelSize(R.dimen.corner_radius_8dp)

                binding.createPlaylistImagePicker.setImage(uri, cornerRadius)
                saveImageToPrivateStorage(uri)
            }
        }
    }

    private fun saveImageToPrivateStorage(uri: Uri) {
        val filePath = File(
            requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES),
            getString(R.string.playlists)
        )
        if (!filePath.exists()) {
            filePath.mkdirs()
        }
        val file = File(filePath, uri.lastPathSegment ?: "image")
        val inputStream = requireActivity().contentResolver.openInputStream(uri)
        val outputStream = FileOutputStream(file)

        BitmapFactory
            .decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, QUALITY_IMAGE, outputStream)

        imageUri = file.toUri()
    }

    private fun initListeners() {

        requireActivity().onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (isFieldsEmpty()) {
                    confirmDialog?.show()
                } else {
                    findNavController().navigateUp()
                }
            }

        })

        binding.createPlaylistBackButton.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.createPlaylistImagePicker.setOnClickListener {
            viewModel.onPlaylistCoverClicked()

        }

        binding.createPlaylistName.doOnTextChanged { text, _, _, _ ->
            if (text.isNullOrEmpty()) {
                viewModel.setEmptyState()
            } else {
                viewModel.setFilledState()
            }
        }

        binding.createPlaylistCreateButton.setOnClickListener {

            val playlist = Playlist(
                name = binding.createPlaylistName.text.toString(),
                description = binding.createPlaylistDescription.text.toString(),
                imageUri = imageUri,
                trackList = "",
                countTracks = 0

            )

            viewModel.savePlayList(playlist)

            Toast.makeText(
                requireContext(),
                "Плейлист ${playlist.name} создан",
                Toast.LENGTH_SHORT
            ).show()

            confirmDialog = null
            findNavController().navigateUp()
        }
    }

    private fun initObservers() {
        viewModel.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                is CreatePlaylistScreenState.Empty -> {
                    renderUi(isButtonEnabled = false, buttonColor = R.color.yp_text_gray)
                }

                is CreatePlaylistScreenState.Filled -> {
                    renderUi(isButtonEnabled = true, buttonColor = R.color.yp_blue)
                }
            }
        }

        viewModel.permissionState.observe(viewLifecycleOwner) { permissionState ->
            when (permissionState) {
                is PermissionResult.Granted -> {
                    pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                }

                is PermissionResult.Denied.NeedsRationale -> {
                    Toast
                        .makeText(
                            requireContext(),
                            getString(R.string.rationale_permission_message),
                            Toast.LENGTH_SHORT
                        )
                        .show()
                }

                is PermissionResult.Denied.DeniedPermanently -> {
                    pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
//                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
//                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//                    intent.data = Uri.fromParts("package", requireContext().packageName, null)
//                    requireContext().startActivity(intent)
                }

                else -> {
                    return@observe
                }
            }
        }
    }

    private fun renderUi(isButtonEnabled: Boolean, @ColorRes buttonColor: Int) {
        binding.createPlaylistCreateButton.isEnabled = isButtonEnabled
        binding.createPlaylistCreateButton.setBackgroundColor(resources.getColor(buttonColor, null))
    }

    private fun isFieldsEmpty(): Boolean {
        return binding.createPlaylistDescription.text.toString().isNotEmpty() ||
                binding.createPlaylistName.text.toString().isNotEmpty() || (imageUri != null)
    }
}