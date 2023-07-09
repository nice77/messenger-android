package com.example.messenger.ui.settings.notificationsAndSounds

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.messenger.MyFirebaseMessagingService
import com.example.messenger.databinding.FragmentNotificationsAndSoundsBinding
import com.example.messenger.messanger.CreateBeseda
import com.example.messenger.messanger.DataRepository

class NotificationsAndSoundsFragment : Fragment() {
    private var _binding: FragmentNotificationsAndSoundsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNotificationsAndSoundsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.button.setOnClickListener {

            println("From notifications" + DataRepository.getInstance().getUser())

            var listOfUserId: List<String> = emptyList()
            DataRepository.getInstance().fetchUsersFromDatabase {users ->
                users?.let {usersList ->
                    listOfUserId = usersList.map {it.id}
                }
            }
            val createBeseda = CreateBeseda()
            createBeseda.createBeseda(listOfUserId)
        }

        setHasOptionsMenu(true)


        return root
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                requireActivity().onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
