package com.bounswe.purposefulcommunity.Fragments

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bounswe.purposefulcommunity.R
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_community.*

class CommunityActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_community)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
    }

}
