package com.sriyank.globochat

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.NavHostFragment
import androidx.preference.*


class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings, rootKey)

        val dataStore = DataStore()

        val accSettingPref = findPreference<Preference>(getString(R.string.key_account_settings))

        accSettingPref?.setOnPreferenceClickListener {

            val navHostFragment = activity?.supportFragmentManager?.findFragmentById(R.id.nav_host_frag) as NavHostFragment
            val navController = navHostFragment.navController
            val action = SettingsFragmentDirections.actionSettingsToAccSettings()
            navController.navigate(action)
            true

        }

        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

        val autoReplyTime = sharedPreferences.getString(getString(R.string.key_auto_reply_time),"")
        Log.i("SettingsFragment","Auto reply time $autoReplyTime")

        val autoDownload = sharedPreferences.getBoolean(getString(R.string.key_auto_download),false)
        Log.i("SettingsFragment","Auto download $autoDownload")

        val statusPref = findPreference<EditTextPreference>(getString(R.string.key_status))
        statusPref?.setOnPreferenceChangeListener { preference, newValue ->

            val  newStatus = newValue as String
            if (newStatus.contains("bad")) {

                Toast.makeText(context,"Inappropriate status. Please maintain community guidelines.", Toast.LENGTH_LONG ).show()
                Log.i("SettingsFragment", "toast")

                false
            }
            else {
                true
            }

        }

        val notifPref = findPreference<SwitchPreferenceCompat>(getString(R.string.key_new_msg_notif))
        notifPref?.summaryProvider = Preference.SummaryProvider<SwitchPreferenceCompat>{ switchPref ->

            if (switchPref?.isChecked!!)
                "Status : ON"
            else
                "Status: OFF"
        }
        notifPref?.preferenceDataStore = dataStore

        val isNotifEnabled = dataStore.getBoolean("key_new_msg_notif",false)
        Log.i("DataStore", "getBoolean executed here $isNotifEnabled ")




    }

    class DataStore : PreferenceDataStore (){

        override fun getBoolean(key: String?, defValue: Boolean): Boolean {
            if (key == "key_new_msg_notif")
                Log.i("DataStore", "getBoolean executed for $key ")
            return defValue
        }

        override fun putBoolean(key: String?, value: Boolean) {
            if (key == "key_new_msg_notif")
                Log.i("DataStore", "putBoolean executed for $key with new value: $value")
        }

    }



}