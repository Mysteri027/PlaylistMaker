package com.example.playlistmaker.presentation.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentSettingsBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SettingsViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.switchThemeButton.isChecked = viewModel.getThemeSettings()

        binding.switchThemeButton.setOnCheckedChangeListener { _, checked ->
            viewModel.switchTheme(checked)
        }

        binding.shareContainer.setOnClickListener {
            val link = resources.getString(R.string.yandex_practicum_link)
            viewModel.shareApp(link)
        }

        binding.supportContainer.setOnClickListener {
            val email = resources.getString(R.string.my_email)
            val subject = resources.getString(R.string.email_subject)
            viewModel.sendEmailToSupport(email, subject)
        }

        binding.arrowForwardContainer.setOnClickListener {
            val link = resources.getString(R.string.practicum_offer_link)
            viewModel.checkOffer(link)
        }
    }
}