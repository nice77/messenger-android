package com.example.messenger.ui.settings.theme

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.messenger.R
import com.example.messenger.databinding.FragmentThemeBinding

class ThemeFragment : Fragment() {
    private var _binding: FragmentThemeBinding? = null
    private val binding get() = _binding!!
    private final val SWITCH_PREFS: String = "SWITCH"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentThemeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val switch = binding.switch1
        val pref = requireContext().getSharedPreferences("Default", Context.MODE_PRIVATE)
        if (pref.getBoolean(SWITCH_PREFS, false)) {
            switch.text = "Темная"
            val textColor = ContextCompat.getColor(requireContext(), R.color.textButton)
            switch.setTextColor(textColor)
            switch.isChecked = true
        } else {
            switch.text = "Светлая"
        }

        switch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                pref.edit().putBoolean(SWITCH_PREFS, isChecked).apply()
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                pref.edit().putBoolean(SWITCH_PREFS, isChecked).apply()
            }
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
