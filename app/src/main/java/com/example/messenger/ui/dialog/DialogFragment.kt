package com.example.messenger.ui.dialog

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.messenger.R
import com.example.messenger.databinding.FragmentDialogBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class DialogFragment : Fragment(R.layout.fragment_dialog) {

//    private var _binding: FragmentDialogBinding? = null
//
//    // This property is only valid between onCreateView and
//    // onDestroyView.
//    private val binding get() = _binding!!
//
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        val dashboardViewModel =
//            ViewModelProvider(this).get(DashboardViewModel::class.java)
//
//        _binding = FragmentDialogBinding.inflate(inflater, container, false)
//        val root: View = binding.root
//
//        val textView: TextView = binding.textDashboard
//        dashboardViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
//        }
//        return root
//    }
//
//    override fun onDestroyView() {
//        super.onDestroyView()
//        _binding = null
//    }

    private var binding : FragmentDialogBinding ?= null
    private var adapter : MessagesAdapter ?= null
    private lateinit var firebaseDB : FirebaseDatabase

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentDialogBinding.bind(view)
        firebaseDB = FirebaseDatabase.getInstance()
        initAdapter()
    }

    fun initAdapter() {
        adapter = MessagesAdapter(MessagesRepo.messagesList, firebaseDB)
        binding?.run {
            rvMessages.adapter = adapter
        }
    }
}