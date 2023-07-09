package com.example.messenger.ui.chats

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.messenger.R
import com.example.messenger.databinding.FragmentChatsBinding
import com.example.messenger.messanger.Beseda
import com.example.messenger.messanger.DataRepository

class ChatsFragment : Fragment(R.layout.fragment_chats) {

    private var dataUpdated = false

    private var binding: FragmentChatsBinding? = null
    private var adapter: ChatsAdapter? = null
    private var sessionId: String = DataRepository.getInstance().getUser()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sessionId = DataRepository.getInstance().getUser()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentChatsBinding.bind(view)
        initAdapter()
        startDataUpdateLoop()
    }

    private fun initAdapter() {
        adapter = ChatsAdapter(DataRepository.getInstance().getBesedas() ?: emptyList()) { chat ->
            val bundle = onNavigation(sessionId)
            findNavController().navigate(
                R.id.action_navigation_chats_to_navigation_dialog,
                bundle
            )
        }
        binding?.run {
            rvChatList.adapter = adapter
        }
    }

    private fun startDataUpdateLoop() {
        val handler = Handler(Looper.getMainLooper())
        val runnable = object : Runnable {
            override fun run() {
                updateData()
                if (!dataUpdated) {
                    handler.postDelayed(this, 2000) // Update every 2 seconds until data is updated
                }
            }
        }
        handler.postDelayed(runnable, 2000)
    }

    private fun updateData() {
        Log.i("Iter", "Yes")
        val besedas = DataRepository.getInstance().getBesedas()
        adapter?.setData(besedas ?: emptyList())
        adapter?.notifyDataSetChanged()

        // Check if data is updated
        if (!besedas.isNullOrEmpty()) {
            dataUpdated = true
        }
    }


    companion object {
        private fun onNavigation(sessionId: String): Bundle {
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
