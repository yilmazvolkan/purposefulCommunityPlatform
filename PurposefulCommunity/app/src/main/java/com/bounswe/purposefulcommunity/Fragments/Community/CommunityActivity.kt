package com.bounswe.purposefulcommunity.Fragments.Community

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bounswe.purposefulcommunity.Fragments.Templates.CreateTemplateActivity
import com.bounswe.purposefulcommunity.R
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_community.*
import kotlinx.android.synthetic.main.activity_show_comm.communityImage

class CommunityActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_community)

        val communityName = intent.getStringExtra("comm_name")
        val communityID = intent.getStringExtra("comm_id")

        val actionBar = supportActionBar
        actionBar!!.title = communityName
        actionBar.setDisplayHomeAsUpEnabled(true)

        Glide.with(this).load(R.drawable.tea).centerCrop().into(communityImage)

        add_temp.setOnClickListener {
            val intent = Intent(this@CommunityActivity, CreateTemplateActivity::class.java)
            startActivity(intent)
            overridePendingTransition(
                R.anim.slide_in_right,
                R.anim.slide_out_left
            )
        }
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
