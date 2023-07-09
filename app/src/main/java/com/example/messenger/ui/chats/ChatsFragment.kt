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
import com.example.messenger.messanger.CreateBeseda
import com.example.messenger.messanger.DataRepository
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ChatsFragment : Fragment(R.layout.fragment_chats) {

    private var dataUpdated = false
    private var binding: FragmentChatsBinding? = null
    private var adapter: ChatsAdapter? = null
    private var newDataAvailable = false
    private var usersDialog: AlertDialog? = null
    private var sessionId: String = DataRepository.getInstance().getUser()
    private val handler = Handler(Looper.getMainLooper())
    private val updateRunnable = Runnable { updateData() }

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
            val bundle = onNavigation(chat.besedaId)
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
        handler.postDelayed(updateRunnable, 2000)
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

        if (!dataUpdated) {
            handler.postDelayed(updateRunnable, 2000)
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

        listView.setOnItemClickListener { _, _, position, _ ->
            val selectedUser = users?.getOrNull(position)
            selectedUser?.let {
//                DataRepository.getInstance().setUsers()

                var hasCommonBeseda = DataRepository.getInstance().hasCommonBesedaWithUser(it)
                var message = ""
                if (hasCommonBeseda != null) {
                    message = "У вас есть общая беседа с пользователем $it"
                } else {
                    message = "У вас нет общей беседы с пользователем $it"
                    val createBeseda = CreateBeseda()
                    hasCommonBeseda = createBeseda.createBeseda(listOf(DataRepository.getInstance().getUser(), it.id))
                }

                Log.i("Clicked User Email", message)
                val bundle = hasCommonBeseda?.let { it1 -> onNavigation(it1) }
                findNavController().navigate(
                    R.id.action_navigation_chats_to_navigation_dialog,
                    bundle
                )
            }
            usersDialog?.dismiss()
        }
    }



    companion object {
        private fun onNavigation(besedaId: Int): Bundle {
            val bundle = Bundle()
            bundle.putInt("name", besedaId)
            return bundle
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(updateRunnable)
        usersDialog?.dismiss()
        binding = null
    }

    fun updateBesedas(besedas: List<Beseda>) {
        adapter?.setData(besedas)
        adapter?.notifyDataSetChanged()
    }
}

