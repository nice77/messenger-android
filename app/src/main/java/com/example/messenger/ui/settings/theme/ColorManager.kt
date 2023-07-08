//package com.example.messenger.ui.settings.theme
//
//import android.content.Context
//import android.content.SharedPreferences
//import android.preference.PreferenceManager
//import androidx.core.content.ContextCompat
//import com.example.messenger.R
//
//class ColorManager(private val context: Context) {
//
//    private val sharedPrefs: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
//    private val colorKey = "app_color"
//
//    fun setAppColor(color: Int) {
//        sharedPrefs.edit().putInt(colorKey, color).apply()
//        applyColorToApp(color)
//    }
//
//    fun getAppColor(): Int {
//        return sharedPrefs.getInt(colorKey, getDefaultColor())
//    }
//
//    private fun applyColorToApp(color: Int) {
//        // Примените выбранный цвет к вашему приложению
//        // Например, используйте методы для изменения цветовых значений элементов пользовательского интерфейса
//    }
//
//    private fun getDefaultColor(): Int {
//        // Верните цвет по умолчанию для вашего приложения
//        // Например, можно вернуть значение ресурса цвета
//        return ContextCompat.getColor(context, R.color.defoultColor)
//    }
//
//    private fun setDefaultColor() {
//        val greenColor = ContextCompat.getColor(context, R.color.)
//    }
//}
