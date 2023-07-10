package com.example.messenger.ui.dialog

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.messenger.R
import com.example.messenger.databinding.FragmentDialogBinding
import com.example.messenger.messanger.Beseda
import com.example.messenger.messanger.DataRepository
import com.example.messenger.messanger.Messeng
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class DialogFragment : Fragment(R.layout.fragment_dialog) {
    private var binding : FragmentDialogBinding ?= null
    var adapter : MessagesAdapter ?= null
    private var dataUpdated = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        DataRepository.getInstance().setDialogFragment(this)
        binding = FragmentDialogBinding.bind(view)
        initAdapter()
        startDataUpdateLoop()
    }

    fun initAdapter() {
        var messages = mutableListOf<Messeng>()
        getMessages {
            messages.addAll(it)
        }
        println("Passing messages in adapter: " + messages.size)
        adapter = MessagesAdapter(messages)

        binding?.run {
            rvMessages.adapter = adapter

            btnSend.setOnClickListener {
                val text = tiMessage.text
                val fromId = DataRepository.getInstance().getUser()
                val message = Messeng(from = fromId, msg = text.toString())

                val mss = mutableListOf<Messeng>()
                getMessages {
                    mss.addAll(it)
                    mss.add(message)
                    val ref = DataRepository
                        .getInstance()
                        .getDB()
                        .child("besedas")
                        .child(DataRepository.getInstance().getBeseda())
                        .child("messengs")
                        .push()

                    ref.setValue(message)
                    dataUpdated = false
                    adapter!!.setData(mss)
                    rvMessages.scrollToPosition(mss.size - 1)
                }
            }
        }
    }

    fun updateMessages(messages: List<Messeng>) {
        println("Called updateMessages from DataRepo. Messages: "  + messages)
        adapter?.setData(messages)
    }

    private fun startDataUpdateLoop() {
        val handler = Handler(Looper.getMainLooper())
        val runnable = object : Runnable {
            override fun run() {
                if (!dataUpdated) {
                    val mss = mutableListOf<Messeng>()
                    getMessages { mss.addAll(it) }
                    println("received messages from loop: " + mss.size)
                    if (mss.size != 0) {
                        updateMessages(mss)
                    }
                    handler.postDelayed(this, 2000) // Update every 2 seconds until data is updated
                    dataUpdated = true
                }
            }
        }
        handler.postDelayed(runnable, 2000)
    }

    private fun getMessages(callback: (List<Messeng>) -> Unit){
        val mss = mutableListOf<Messeng>()
        DataRepository
            .getInstance()
            .getDB()
            .child("besedas")
            .child(arguments?.getInt("name").toString())
            .child("messengs").get().addOnSuccessListener {
                for (s in it.children) {
                    mss.add(s.getValue(Messeng::class.java)!!)
                }
                callback(mss)
            }
    }
}
