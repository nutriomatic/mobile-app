package com.nutriomatic.app.presentation.scan

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.nutriomatic.app.data.remote.Result
import com.nutriomatic.app.databinding.FragmentPreviewBinding
import com.nutriomatic.app.presentation.factory.ViewModelFactory
import com.nutriomatic.app.presentation.helper.util.reduceFileSize
import com.nutriomatic.app.presentation.helper.util.uriToFile
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody

class PreviewFragment : Fragment() {
    private var _binding: FragmentPreviewBinding? = null
    private val binding get() = _binding!!
    private val args: PreviewFragmentArgs by navArgs()

    private val viewModel: PreviewViewModel by viewModels<PreviewViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }

    private var currentImageUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentPreviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            currentImageUri = Uri.parse(args.imageUriString).also { uri ->
                ivPhotoPreview.setImageURI(uri)
            }
            btnRetake.setOnClickListener {
                val navDirections = PreviewFragmentDirections.actionPreviewFragmentToScanFragment()
                findNavController().navigate(navDirections)
            }
            btnSubmit.setOnClickListener {
                submit()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun submit() {
        val name = "scan_${System.currentTimeMillis()}"
        val imageFile = uriToFile(requireContext(), currentImageUri!!).reduceFileSize()
        val requestFile = imageFile.asRequestBody("multipart/form-data".toMediaTypeOrNull())
        val filePart = MultipartBody.Part.createFormData("file", imageFile.name, requestFile)

        viewModel.createNutritionScan(name, filePart)

        viewModel.createNutritionScanResponse.observe(viewLifecycleOwner) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {

                    }

                    is Result.Success -> {
                        val navDirection =
                            PreviewFragmentDirections.actionPreviewFragmentToHistoryFragment(result.data.message.toString())
                        findNavController().navigate(navDirection)
                    }

                    is Result.Error -> {
                        Snackbar.make(
                            requireView(),
                            result.error,
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }

                }
            }
        }
    }
}