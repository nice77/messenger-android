package com.example.messenger.ui.settings.notificationsAndSounds

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.messenger.databinding.FragmentChatFoldersBinding
import com.example.messenger.databinding.FragmentNotificationsAndSoundsBinding
import com.example.messenger.databinding.FragmentNotificationsBinding
import com.example.messenger.ui.settings.chatFolders.ChatFoldersViewModel

class NotificationsAndSoundsFragment: Fragment() {
    private var _binding: FragmentNotificationsAndSoundsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val notificationsViewModel = ViewModelProvider(this).get(NotificationsAndSoundsViewModel::class.java)

        _binding = FragmentNotificationsAndSoundsBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }
}