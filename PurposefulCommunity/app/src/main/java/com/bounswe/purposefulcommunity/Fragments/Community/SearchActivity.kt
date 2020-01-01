package com.bounswe.purposefulcommunity.Fragments.Community

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bounswe.purposefulcommunity.R

class SearchActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val communityID = intent.getStringExtra("comm_temp_id")

        val actionBar = supportActionBar
        actionBar!!.title = getString(R.string.search)
        actionBar.setDisplayHomeAsUpEnabled(true)
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
    override fun finish() {
        super.finish()
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
    }
}
