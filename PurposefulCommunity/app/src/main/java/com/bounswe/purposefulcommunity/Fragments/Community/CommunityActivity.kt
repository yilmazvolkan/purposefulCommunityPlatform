package com.bounswe.purposefulcommunity.Fragments.Community

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bounswe.mercatus.API.ApiInterface
import com.bounswe.mercatus.API.RetrofitInstance
import com.bounswe.purposefulcommunity.Fragments.Templates.CreateTemplateActivity
import com.bounswe.purposefulcommunity.Models.GetOneCommBody
import com.bounswe.purposefulcommunity.R
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_community.*
import kotlinx.android.synthetic.main.activity_show_comm.communityImage
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.ConnectException

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

        getOneCommunities(communityID)

        add_temp.setOnClickListener {
            val intent = Intent(this@CommunityActivity, CreateTemplateActivity::class.java)
            startActivity(intent)
            overridePendingTransition(
                R.anim.slide_in_right,
                R.anim.slide_out_left
            )
        }
    }

    private fun getOneCommunities(communityID: String){
        val res = getSharedPreferences("TOKEN_INFO", Context.MODE_PRIVATE)
        val tokenV = res.getString("token", "Data Not Found!")
        val userID = res.getString("user_id", "Data Not Found!")

        val purApp = RetrofitInstance.getRetrofitInstance().create(ApiInterface::class.java)

        purApp.getOneComm(communityID, tokenV!!).enqueue(object : Callback<GetOneCommBody> {
            override fun onFailure(call: Call<GetOneCommBody>, t: Throwable) {
                if(t.cause is ConnectException){
                    Toast.makeText(
                        this@CommunityActivity,
                        "Check your connection!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else{
                    Toast.makeText(
                        this@CommunityActivity,
                        "Something bad happened!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            override fun onResponse(call: Call<GetOneCommBody>, response: Response<GetOneCommBody>) {
                if (response.code() == 200) {
                    if(response.body()!!.builders.isNotEmpty()){
                        for(item in response.body()!!.builders){
                            if(userID == item.id){
                                addTempText.visibility = View.VISIBLE
                            }
                        }
                    }
                    if(response.body()!!.creatorUser.id.isNotEmpty()){
                        if(userID == response.body()!!.creatorUser.id){
                            addTempText.visibility = View.VISIBLE
                        }
                    }
                } else {
                    Toast.makeText(this@CommunityActivity, "Community cannot retrieve!", Toast.LENGTH_SHORT).show()
                }
            }
        })
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
