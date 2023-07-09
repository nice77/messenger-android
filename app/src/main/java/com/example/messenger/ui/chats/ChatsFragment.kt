package com.example.messenger.ui.chats

import android.app.AlertDialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.messenger.R
import com.example.messenger.databinding.FragmentChatsBinding
import com.example.messenger.messanger.Beseda
import com.example.messenger.messanger.DataRepository
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ChatsFragment : Fragment(R.layout.fragment_chats) {

    private var dataUpdated = false

    private var binding: FragmentChatsBinding? = null
    private var adapter: ChatsAdapter? = null
    private var newDataAvailable = false
    private var usersDialog: AlertDialog? = null
    private var sessionId: String = DataRepository.getInstance().getUser()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DataRepository.getInstance().setChatsFragment(this)

        sessionId = DataRepository.getInstance().getUser()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentChatsBinding.bind(view)


        initAdapter()
        startDataUpdateLoop()
        setupFab()
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

    fun updateBesedas(besedas: List<Beseda>) {
        adapter?.setData(besedas)
        adapter?.notifyDataSetChanged()
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
            newDataAvailable = true
        }
    }


    private fun setupFab() {
        val fabAddUser: FloatingActionButton = requireView().findViewById(R.id.fab_add_user)
        fabAddUser.setOnClickListener {
            showUsersDialog()
        }
    }

    private fun showUsersDialog() {
        val users = DataRepository.getInstance().getUsers()
        val userList = users?.map { it.login } ?: emptyList()

        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, userList)
        val listView = ListView(requireContext())
        listView.adapter = adapter

        val builder = AlertDialog.Builder(requireContext())
            .setTitle(R.string.users_dialog_title)
            .setView(listView)

        usersDialog = builder.create()
        usersDialog?.show()
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
        usersDialog?.dismiss()
        binding = null
    }
}
