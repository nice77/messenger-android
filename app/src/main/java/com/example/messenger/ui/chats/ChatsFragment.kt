package com.example.messenger.ui.chats

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.messenger.R
import com.example.messenger.databinding.FragmentChatsBinding
import com.example.messenger.messanger.Beseda
import com.example.messenger.messanger.DataRepository
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.values
import com.google.firebase.ktx.Firebase

class ChatsFragment : Fragment(R.layout.fragment_chats) {

    private var binding: FragmentChatsBinding ?= null
    private var adapter: ChatsAdapter ?= null
    private var sessionId : String = DataRepository.getInstance().getUser()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sessionId = DataRepository.getInstance().getUser()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentChatsBinding.bind(view)
        initAdapter()
    }

    private fun initAdapter() {
        println("Kek: " + DataRepository.getInstance().getBesedas() + " User: " + DataRepository.getInstance().getUser())
        adapter = DataRepository.getInstance().getBesedas()?.let {
            ChatsAdapter(
                it
            ) { chat ->
                findNavController().navigate(
                    R.id.action_navigation_chats_to_navigation_dialog,
                    sessionId.let { onNavigation(it) }
                )
            }
        }
        binding?.run {
            rvChatList.adapter = adapter
        }
    }

    companion object {
        private fun onNavigation(sessionId : String) : Bundle {
            val bundle = Bundle()
            bundle.putString("name", "KEK")
            return bundle
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}