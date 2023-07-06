package com.example.messenger.ui.settings.theme

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.messenger.databinding.FragmentNotificationsBinding
import com.example.messenger.databinding.FragmentThemeBinding
import com.example.messenger.ui.notifications.NotificationsViewModel

class ThemeFragment: Fragment() {
    private var _binding: FragmentThemeBinding? = null
    private val binding get() = _binding!!
    private final val SWITCH_PREFS: String = "SWITCH";

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val notificationsViewModel = ViewModelProvider(this).get(ThemeViewModel::class.java)

        _binding = FragmentThemeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        var switch = binding.switch1
        val pref = requireContext().getSharedPreferences("Default", Context.MODE_PRIVATE)
        if(pref.getBoolean(SWITCH_PREFS,false)){
            switch.text = "Темная"
            switch.isChecked = true
        }else{switch.text = "Cветлая"}//Нужно доработать для возможности менять язык

        switch.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                pref.edit().putBoolean(SWITCH_PREFS,isChecked).apply()
            }else{
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                pref.edit().putBoolean(SWITCH_PREFS,isChecked).apply()
            }
        }


        return root
    }
}