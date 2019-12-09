package com.bounswe.purposefulcommunity.Fragments.Community

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bounswe.mercatus.API.ApiInterface
import com.bounswe.mercatus.API.RetrofitInstance
import com.bounswe.purposefulcommunity.Fragments.Instances.ShowTemplatesActivity
import com.bounswe.purposefulcommunity.Fragments.Templates.CreateTemplateActivity
import com.bounswe.purposefulcommunity.Models.GetOneCommBody
import com.bounswe.purposefulcommunity.R
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_community.*
import kotlinx.android.synthetic.main.activity_show_comm.communityImage
import okhttp3.MultipartBody
import okhttp3.ResponseBody
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


        addCSD.setOnClickListener {
            val intent = Intent(this@CommunityActivity, ShowTemplatesActivity::class.java)
            intent.putExtra("comm_temp_id", communityID)
            startActivity(intent)
            overridePendingTransition(
                R.anim.slide_in_right,
                R.anim.slide_out_left
            )
        }

        add_temp.setOnClickListener {
            val intent = Intent(this@CommunityActivity, CreateTemplateActivity::class.java)
            intent.putExtra("comm_temp_id", communityID)
            intent.putExtra("comm_temp_name", communityName)
            startActivity(intent)
            overridePendingTransition(
                R.anim.slide_in_right,
                R.anim.slide_out_left
            )
        }
        add_temp.isClickable = false
    }
    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_upload -> {
            //check runtime permission
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) ==
                    PackageManager.PERMISSION_DENIED){
                    //permission denied
                    val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE);
                    //show popup to request runtime permission
                    requestPermissions(permissions, PERMISSION_CODE);
                }
                else{
                    //permission already granted
                    pickImageFromGallery();
                }
            }
            else{
                //system OS is < Marshmallow
                pickImageFromGallery();
            }
            true
        }
        R.id.action_leave -> {
            //TODO add unfollow community here
            true
        }
        else -> {
            // If we got here, the user's action was not recognized.
            // Invoke the superclass to handle it.
            super.onOptionsItemSelected(item)
        }
    }

    //handle requested permission result
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode){
            PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED){
                    //permission from popup granted
                    pickImageFromGallery()
                }
                else{
                    //permission from popup denied
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.options_menu, menu)
        return true
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
                                add_temp.isClickable = true
                            }
                        }
                    }
                    if(response.body()!!.creatorUser.id.isNotEmpty()){
                        if(userID == response.body()!!.creatorUser.id){
                            addTempText.visibility = View.VISIBLE
                            add_temp.isClickable = true
                        }
                    }
                } else {
                    Toast.makeText(this@CommunityActivity, "Community cannot retrieve!", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun uploadImage(communityID: String, image: MultipartBody.Part){
        val res = getSharedPreferences("TOKEN_INFO", Context.MODE_PRIVATE)
        val tokenV = res.getString("token", "Data Not Found!")
        val userID = res.getString("user_id", "Data Not Found!")

        val purApp = RetrofitInstance.getRetrofitInstance().create(ApiInterface::class.java)

        purApp.uploadImage(communityID, image, tokenV!!).enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
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
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.code() == 200) {
                } else {
                    Toast.makeText(this@CommunityActivity, "Community cannot retrieve!", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
    private fun pickImageFromGallery() {
        //Intent to pick image
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    companion object {
        //image pick code
        private val IMAGE_PICK_CODE = 1000
        //Permission code
        private val PERMISSION_CODE = 1001
    }

    //handle result of picked image
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE){
            communityImage.setImageURI(data?.data)

            //uploadImage("adqssa", data!!.data!!)
        }
        super.onActivityResult(requestCode, resultCode, data)
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
