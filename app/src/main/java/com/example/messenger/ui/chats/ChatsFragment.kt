package com.example.messenger.ui.chats

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.messenger.R
import com.example.messenger.databinding.FragmentChatsBinding

class ChatsFragment : Fragment(R.layout.fragment_chats) {

//    private var _binding: FragmentChatsBinding? = null
    private var binding: FragmentChatsBinding ?= null
    private var adapter: ChatsAdapter ?= null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentChatsBinding.bind(view)
        initAdapter()
    }

    // This property is only valid between onCreateView and
    // onDestroyView.
//    private val binding get() = _binding!!

//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        val homeViewModel =
//            ViewModelProvider(this).get(HomeViewModel::class.java)
//
//        _binding = FragmentChatsBinding.inflate(inflater, container, false)
//        val root: View = binding.root
//
//        val textView: TextView = binding.tvTitle
//        homeViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
//        }
//        val database = Firebase.database
//        val myRef = database.getReference("message")
//
//        myRef.setValue("Hello, World!")
//        return root
//    }

//    override fun onDestroyView() {
//        super.onDestroyView()
//        _binding = null
//    }

    private fun initAdapter() {
        adapter = ChatsAdapter(
            ChatsRepo.chatsList,
            { chat ->
                findNavController().navigate(
                    R.id.action_navigation_chats_to_navigation_dialog,
                    onNavigation(chat)
                )
            }
        )
        binding?.run {
            rvChatList.adapter = adapter
        }
    }

    companion object {
        private fun onNavigation(chat : Chat) : Bundle {
            val bundle = Bundle()
            bundle.putString("name", chat.name)
            return bundle
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}