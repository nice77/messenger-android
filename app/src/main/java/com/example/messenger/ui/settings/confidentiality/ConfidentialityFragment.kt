package com.example.messenger.ui.settings.confidentiality

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.messenger.databinding.FragmentChatFoldersBinding
import com.example.messenger.databinding.FragmentConfidentialityBinding
import com.example.messenger.databinding.FragmentNotificationsBinding
import com.example.messenger.ui.settings.chatFolders.ChatFoldersViewModel

class ConfidentialityFragment: Fragment() {
    private var _binding: FragmentConfidentialityBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val notificationsViewModel = ViewModelProvider(this).get(ConfidentialityViewModel::class.java)

        _binding = FragmentConfidentialityBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }
}