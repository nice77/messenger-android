package com.example.messenger.ui.chats

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.messenger.DataRepository
import com.example.messenger.R
import com.example.messenger.databinding.FragmentChatsBinding
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.values
import com.google.firebase.ktx.Firebase
import java.util.LinkedList

class ChatsFragment : Fragment(R.layout.fragment_chats) {

//    private var _binding: FragmentChatsBinding? = null
    private var binding: FragmentChatsBinding ?= null
    private var adapter: ChatsAdapter ?= null
    private var fbDB: DatabaseReference = Firebase.database.reference
    private var sessionId : String? = DataRepository.getInstance().user

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
        val chatsId = LinkedList<String>()
        sessionId?.let {
            fbDB.child("users")
            .child(it) // поиск среди всех пользователей "нашего"
            .child("beseda") // собираем все беседы
            .get().addOnSuccessListener {
                for (snapshot in it.children) {
                    val chat = snapshot.getValue(String::class.java)
                    println("Chat: " + chat)
                    chat?.let { chatsId.add(chat) }
                }
            }
        }

        println("adding" + chatsId.size)
        println("added")

        adapter = ChatsAdapter(
            LinkedList()
        ) { chat ->
            findNavController().navigate(
                R.id.action_navigation_chats_to_navigation_dialog,
                this.sessionId?.let { onNavigation(it) }
            )
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