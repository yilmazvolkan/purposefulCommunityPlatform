package com.bounswe.purposefulcommunity.Fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bounswe.mercatus.API.ApiInterface
import com.bounswe.mercatus.API.RetrofitInstance
import com.bounswe.purposefulcommunity.Models.CommunityBody
import com.bounswe.purposefulcommunity.Models.CreateCommBody
import com.bounswe.purposefulcommunity.R
import kotlinx.android.synthetic.main.activity_create_comm.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.ConnectException

class CreateCommActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_comm)

        val actionBar = supportActionBar
        actionBar!!.title = getString(R.string.title_create_community)

        btn_create.setOnClickListener {
            val name = editComName.text.toString()
            val description = editDesc.text.toString()
            val size = editSize.text.toString().toInt()
            val isPrivate = switch1.isChecked //Comm is private when it is true

            if (isValidForm(name, description, size)) {
                createCommunity(name, description, size, isPrivate)
            }
        }
        join_comm.setOnClickListener {
            val intent = Intent(this@CreateCommActivity, CommunityActivity::class.java)
            startActivity(intent)
            overridePendingTransition(
                R.anim.slide_in_right,
                R.anim.slide_out_left
            )
        }
    }
    private fun createCommunity(name: String, desc: String, size: Int, isPrivate: Boolean){
        val purApp = RetrofitInstance.getRetrofitInstance().create(ApiInterface::class.java)
        val communityInfo = CreateCommBody(desc,isPrivate, name,size)

        val res = getSharedPreferences("TOKEN_INFO", Context.MODE_PRIVATE)
        val tokenV = res.getString("token", "Data Not Found!")

        purApp.createComm(communityInfo, tokenV!!).enqueue(object : Callback<CommunityBody> {
            override fun onFailure(call: Call<CommunityBody>, t: Throwable) {
                if(t.cause is ConnectException){
                    Toast.makeText(
                        this@CreateCommActivity,
                        "Check your connection!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else{
                    Toast.makeText(
                        this@CreateCommActivity,
                        "Something bad happened!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            override fun onResponse(call: Call<CommunityBody>, response: Response<CommunityBody>) {
                if (response.code() == 200) {
                    Toast.makeText(this@CreateCommActivity, "Community created!", Toast.LENGTH_SHORT)
                        .show()

                }
                else  {
                    Toast.makeText(this@CreateCommActivity, "Create failed", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        })
    }
    private fun isValidForm(name: String, description: String, size: Int):Boolean{

        var isValid = true

        if (name.isEmpty()){
            layComName.isErrorEnabled = true
            layComName.error = "Name cannot be empty!"
            isValid = false
        }else{
            layComName.isErrorEnabled = false
        }
        if (description.isEmpty()){
            layDesc.isErrorEnabled = true
            layDesc.error = "Description cannot be empty!"
            isValid = false
        }else{
            layDesc.isErrorEnabled = false
        }
        if(size < 1){
            laySize.isErrorEnabled = true
            laySize.error = "Size must be more than 1."
            isValid = false
        }
        else{
            laySize.isErrorEnabled = false
        }
        return isValid
    }
    override fun finish() {
        super.finish()
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
    }
}
