package com.example.usersettingsapp

import android.content.Context
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class SettingsViewerActivity : AppCompatActivity() {

    lateinit var container: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_settings_viewer)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Saved Settings"

        container = findViewById(R.id.containerSettings)

        findViewById<Button>(R.id.btnBack).setOnClickListener {
            finish()
        }

        findViewById<Button>(R.id.btnEdit).setOnClickListener {
            finish()
        }

        loadSettings()
    }

    private fun loadSettings() {

        val prefs = getSharedPreferences(
            "AppSettings",
            Context.MODE_PRIVATE
        )

        if (!prefs.contains("KEY_LAST_SAVED")) {

            addCard(
                "Message",
                "No settings saved yet"
            )

            return
        }

        val name = prefs.getString(
            "KEY_STUDENT_NAME",
            ""
        )

        val theme = prefs.getString(
            "KEY_THEME",
            ""
        )

        val language = prefs.getString(
            "KEY_LANGUAGE",
            ""
        )

        val font = prefs.getInt(
            "KEY_FONT_SIZE",
            16
        )

        val notifications =
            if (prefs.getBoolean(
                    "KEY_NOTIFICATIONS",
                    true
                )
            )
                "Enabled"
            else
                "Disabled"

        val time = prefs.getLong(
            "KEY_LAST_SAVED",
            0
        )

        val date = SimpleDateFormat(
            "dd MMM yyyy hh:mm a",
            Locale.getDefault()
        ).format(Date(time))

        addCard(
            "Student Name",
            name!!
        )

        addCard(
            "Theme",
            theme!!
        )

        addCard(
            "Language",
            language!!
        )

        addCard(
            "Notifications",
            notifications
        )

        addCard(
            "Font Size",
            "${font}sp"
        )

        addCard(
            "Last Saved",
            date
        )
    }

    private fun addCard(
        title: String,
        value: String
    ) {

        val card = CardView(this)

        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

        params.setMargins(
            0,
            20,
            0,
            0
        )

        card.layoutParams = params

        card.radius = 20f

        card.setContentPadding(
            30,
            30,
            30,
            30
        )

        val text = TextView(this)

        text.text = "$title : $value"

        text.textSize = 18f

        card.addView(text)

        container.addView(card)
    }

    override fun onOptionsItemSelected(
        item: MenuItem
    ): Boolean {

        if (item.itemId == android.R.id.home) {

            finish()
        }

        return super.onOptionsItemSelected(item)
    }
}