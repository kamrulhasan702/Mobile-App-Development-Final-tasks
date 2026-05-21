package com.example.usersettingsapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat

class MainActivity : AppCompatActivity() {

    lateinit var etStudentName: EditText
    lateinit var rbLight: RadioButton
    lateinit var rbDark: RadioButton
    lateinit var rbSystem: RadioButton
    lateinit var switchNotifications: SwitchCompat
    lateinit var spinnerLanguage: Spinner
    lateinit var seekFontSize: SeekBar
    lateinit var tvFontSize: TextView

    val languages = arrayOf(
        "English",
        "Bangla",
        "Arabic",
        "French"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        etStudentName = findViewById(R.id.etStudentName)

        rbLight = findViewById(R.id.rbLight)

        rbDark = findViewById(R.id.rbDark)

        rbSystem = findViewById(R.id.rbSystem)

        switchNotifications =
            findViewById(R.id.switchNotifications)

        spinnerLanguage =
            findViewById(R.id.spinnerLanguage)

        seekFontSize =
            findViewById(R.id.seekFontSize)

        tvFontSize =
            findViewById(R.id.tvFontSize)

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            languages
        )

        spinnerLanguage.adapter = adapter

        seekFontSize.setOnSeekBarChangeListener(
            object : SeekBar.OnSeekBarChangeListener {

                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {

                    tvFontSize.text =
                        "Font Size: ${progress + 12}sp"
                }

                override fun onStartTrackingTouch(
                    seekBar: SeekBar?
                ) {
                }

                override fun onStopTrackingTouch(
                    seekBar: SeekBar?
                ) {
                }
            }
        )

        findViewById<Button>(R.id.btnSave)
            .setOnClickListener {

                saveSettings()
            }

        findViewById<Button>(R.id.btnReset)
            .setOnClickListener {

                resetSettings()
            }

        findViewById<Button>(R.id.btnView)
            .setOnClickListener {

                startActivity(
                    Intent(
                        this,
                        SettingsViewerActivity::class.java
                    )
                )
            }

        findViewById<Button>(R.id.btnProfile)
            .setOnClickListener {

                startActivity(
                    Intent(
                        this,
                        ProfileActivity::class.java
                    )
                )
            }
    }

    override fun onResume() {
        super.onResume()

        restoreSettings()
    }

    private fun saveSettings() {

        val prefs = getSharedPreferences(
            "AppSettings",
            Context.MODE_PRIVATE
        )

        var theme = "light"

        if (rbDark.isChecked) {
            theme = "dark"
        }

        if (rbSystem.isChecked) {
            theme = "system"
        }

        if (theme == "light") {

            AppCompatDelegate.setDefaultNightMode(
                AppCompatDelegate.MODE_NIGHT_NO
            )
        }

        if (theme == "dark") {

            AppCompatDelegate.setDefaultNightMode(
                AppCompatDelegate.MODE_NIGHT_YES
            )
        }

        if (theme == "system") {

            AppCompatDelegate.setDefaultNightMode(
                AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
            )
        }

        prefs.edit()
            .putString(
                "KEY_STUDENT_NAME",
                etStudentName.text.toString()
            )
            .putString(
                "KEY_THEME",
                theme
            )
            .putBoolean(
                "KEY_NOTIFICATIONS",
                switchNotifications.isChecked
            )
            .putString(
                "KEY_LANGUAGE",
                spinnerLanguage.selectedItem.toString()
            )
            .putInt(
                "KEY_FONT_SIZE",
                seekFontSize.progress + 12
            )
            .putLong(
                "KEY_LAST_SAVED",
                System.currentTimeMillis()
            )
            .apply()

        Toast.makeText(
            this,
            "Settings Saved",
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun restoreSettings() {

        val prefs = getSharedPreferences(
            "AppSettings",
            Context.MODE_PRIVATE
        )

        etStudentName.setText(
            prefs.getString(
                "KEY_STUDENT_NAME",
                ""
            )
        )

        val theme = prefs.getString(
            "KEY_THEME",
            "light"
        )

        if (theme == "light") {
            rbLight.isChecked = true
        }

        if (theme == "dark") {
            rbDark.isChecked = true
        }

        if (theme == "system") {
            rbSystem.isChecked = true
        }

        switchNotifications.isChecked =
            prefs.getBoolean(
                "KEY_NOTIFICATIONS",
                true
            )

        val language = prefs.getString(
            "KEY_LANGUAGE",
            "English"
        )

        spinnerLanguage.setSelection(
            languages.indexOf(language)
        )

        val fontSize = prefs.getInt(
            "KEY_FONT_SIZE",
            16
        )

        seekFontSize.progress =
            fontSize - 12

        tvFontSize.text =
            "Font Size: ${fontSize}sp"
    }

    private fun resetSettings() {

        val prefs = getSharedPreferences(
            "AppSettings",
            Context.MODE_PRIVATE
        )

        prefs.edit().clear().apply()

        etStudentName.setText("")

        rbLight.isChecked = true

        switchNotifications.isChecked = true

        spinnerLanguage.setSelection(0)

        seekFontSize.progress = 4

        tvFontSize.text =
            "Font Size: 16sp"

        Toast.makeText(
            this,
            "Reset Successful",
            Toast.LENGTH_SHORT
        ).show()
    }
}