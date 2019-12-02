package com.bounswe.purposefulcommunity.Fragments.Community

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bounswe.mercatus.API.ApiInterface
import com.bounswe.mercatus.API.RetrofitInstance
import com.bounswe.purposefulcommunity.Models.GetOneCommBody
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

        val actionBar = supportActionBar
        actionBar!!.title = communityName
        actionBar.setDisplayHomeAsUpEnabled(true)

        Glide.with(this).load(R.drawable.tea).centerCrop().into(communityImage)

        getCommunityFeatures(communityID)

        btn_join.setOnClickListener {
            followCommunity(communityID)
        }

    }
    private fun followCommunity(communityID: String){
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
    private fun getCommunityFeatures(communityID: String){
        val res = getSharedPreferences("TOKEN_INFO", Context.MODE_PRIVATE)
        val tokenV = res.getString("token", "Data Not Found!")
        val purApp = RetrofitInstance.getRetrofitInstance().create(ApiInterface::class.java)

        purApp.getOneComm(communityID, tokenV!!).enqueue(object : Callback<GetOneCommBody> {
            override fun onFailure(call: Call<GetOneCommBody>, t: Throwable) {
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
                        "Something bad happened!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            override fun onResponse(call: Call<GetOneCommBody>, response: Response<GetOneCommBody>) {
                if (response.code() == 200) {

                    if(response.body()!!.isPrivate){
                        textPrivate.text = "Private"
                    }
                    else{
                        textPrivate.text = "Public"
                    }
                    textSize.text = response.body()!!.size.toString()
                    textAbout.text = response.body()!!.description
                }
                else {
                    Toast.makeText(this@ShowCommActivity, "Communities cannot retrieve!", Toast.LENGTH_SHORT).show()
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
