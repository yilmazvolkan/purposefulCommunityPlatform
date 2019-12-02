package com.bounswe.purposefulcommunity.Fragments.Community

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bounswe.mercatus.API.ApiInterface
import com.bounswe.mercatus.API.RetrofitInstance
import com.bounswe.purposefulcommunity.R
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_show_comm.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.ConnectException

class ShowCommActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_comm)

        val communityName = intent.getStringExtra("comm_name")
        val communityID = intent.getStringExtra("comm_id")
        val communitySize = intent.getStringExtra("comm_size")
        val communityPrivate = intent.getBooleanExtra("comm_private", false)
        val communityAbout = intent.getStringExtra("comm_about")

        val actionBar = supportActionBar
        actionBar!!.title = communityName
        actionBar.setDisplayHomeAsUpEnabled(true)

        Glide.with(this).load(R.drawable.tea).centerCrop().into(communityImage)

        setCommunityFeatures(communitySize!!, communityPrivate, communityAbout!!)

        btn_join.setOnClickListener {
            followCommunity(communityID!!, communityName!!)
        }

    }
    private fun followCommunity(communityID: String, communityName: String){
        val res = getSharedPreferences("TOKEN_INFO", Context.MODE_PRIVATE)
        val tokenV = res.getString("token", "Data Not Found!")
        val purApp = RetrofitInstance.getRetrofitInstance().create(ApiInterface::class.java)

        purApp.followCommunity(communityID, tokenV!!).enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                if(t.cause is ConnectException){
                    Toast.makeText(
                        this@ShowCommActivity,
                        "Check your connection!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else{
                    Toast.makeText(
                        this@ShowCommActivity,
                        t.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.code() == 200) {
                    Toast.makeText(this@ShowCommActivity, "Join successful!", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@ShowCommActivity, CommunityActivity::class.java)
                    intent.putExtra("comm_id", communityID)
                    intent.putExtra("comm_name", communityName)
                    startActivity(intent)
                    overridePendingTransition(
                        R.anim.slide_in_right,
                        R.anim.slide_out_left
                    )
                    finish()
                }
                else if(response.code() == 500){
                    Toast.makeText(this@ShowCommActivity, "You have already joined the community!", Toast.LENGTH_SHORT).show()
                }
                else {
                    Toast.makeText(this@ShowCommActivity, "Join failed!", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
    private fun setCommunityFeatures(communitySize: String, communityPrivate: Boolean, communityAbout: String){

        if(communityPrivate){
            textPrivate.text = "Private"
        }
        else{
            textPrivate.text = "Public"
        }
        textSize.text = communitySize
        textAbout.text = communityAbout
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
