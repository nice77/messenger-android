package com.example.messenger.ui.settings

import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.messenger.LoginActivity
import com.example.messenger.R
import com.google.firebase.auth.FirebaseAuth

import com.example.messenger.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val notificationsViewModel =
            ViewModelProvider(this).get(NotificationsViewModel::class.java)

        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textNotifications
        notificationsViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        binding?.run {
            btnTheme.setOnClickListener {
                findNavController().navigate(R.id.action_navigation_notifications_to_themeFragment)
            }
            btnChatFolders.setOnClickListener {
                findNavController().navigate(R.id.action_navigation_notifications_to_chatFoldersFragment)
            }
            btnConfidentiality.setOnClickListener {
                findNavController().navigate(R.id.action_navigation_notifications_to_confidentialityFragment)
            }
            btnEnergoyaving.setOnClickListener {
                findNavController().navigate(R.id.action_navigation_notifications_to_energoyavingFragment)
            }
            btnDataAndMemory.setOnClickListener {
                findNavController().navigate(R.id.action_navigation_notifications_to_dataAndMemoryFragment)
            }
            btnNotificationsAndSounds.setOnClickListener {
                findNavController().navigate(R.id.action_navigation_notifications_to_notificationsAndSoundsFragment)
            }
            //кнопку выхода из ака-47 реализуйте плез
            btnExit.setOnClickListener {
                // Выполните выход из аккаунта
                logout()

                // Переход на окно авторизации
                goToLoginActivity()
            }
        }

        return root
    }

    private fun logout() {
        // Выполните выход из аккаунта, используя FirebaseAuth
        FirebaseAuth.getInstance().signOut()
    }

    private fun goToLoginActivity() {
        val intent = Intent(requireContext(), LoginActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }
}