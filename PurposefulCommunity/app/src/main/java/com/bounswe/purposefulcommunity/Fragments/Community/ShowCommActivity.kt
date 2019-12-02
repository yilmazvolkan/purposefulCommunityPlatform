package com.bounswe.purposefulcommunity.Fragments.Community

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bounswe.purposefulcommunity.R
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_show_comm.*

class ShowCommActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_comm)

        val communityName = intent.getStringExtra("comm_name")
        val communityID = intent.getStringExtra("comm_id")

        val actionBar = supportActionBar
        actionBar!!.title = communityName
        actionBar.setDisplayHomeAsUpEnabled(true)

        Glide.with(this).load(R.drawable.tea).centerCrop().into(communityImage)


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
