package com.example.messenger.ui.settings.energoyaving

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.messenger.databinding.FragmentChatFoldersBinding
import com.example.messenger.databinding.FragmentEnergoyavingBinding
import com.example.messenger.databinding.FragmentNotificationsBinding
import com.example.messenger.ui.settings.chatFolders.ChatFoldersViewModel

class EnergoyavingFragment: Fragment() {
    private var _binding: FragmentEnergoyavingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val notificationsViewModel = ViewModelProvider(this).get(EnergoyavingViewModel::class.java)

        _binding = FragmentEnergoyavingBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }
}