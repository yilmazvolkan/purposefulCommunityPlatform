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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bounswe.mercatus.API.ApiInterface
import com.bounswe.mercatus.API.RetrofitInstance
import com.bounswe.purposefulcommunity.Adapters.ShowInstanceAdapter
import com.bounswe.purposefulcommunity.Fragments.Instances.ShowTemplatesActivity
import com.bounswe.purposefulcommunity.Fragments.Templates.TemplateActivity
import com.bounswe.purposefulcommunity.Models.GetInstanceLDBody
import com.bounswe.purposefulcommunity.Models.GetOneCommBody
import com.bounswe.purposefulcommunity.Models.ShowInstanceBody
import com.bounswe.purposefulcommunity.Models.UpperInstanceShowBody
import com.bounswe.purposefulcommunity.R
import com.bumptech.glide.Glide
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_community.*
import kotlinx.android.synthetic.main.activity_show_comm.communityImage
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.net.ConnectException

class CommunityActivity : AppCompatActivity() {

    private var communityID: String = ""
    private var isFABOpen = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_community)

        val communityName = intent.getStringExtra("comm_name")
        communityID = intent.getStringExtra("comm_id")!!

        val actionBar = supportActionBar
        actionBar!!.title = communityName
        actionBar!!
        actionBar.setDisplayHomeAsUpEnabled(true)

        Glide.with(this).load(R.drawable.tea).centerCrop().into(communityImage)

        getOneCommunity(communityID)

        addCSD.setOnClickListener {
            val intent = Intent(this@CommunityActivity, ShowTemplatesActivity::class.java)
            intent.putExtra("comm_temp_id", communityID)
            intent.putExtra("comm_temp_name", communityName)
            startActivity(intent)
            overridePendingTransition(
                R.anim.slide_in_right,
                R.anim.slide_out_left
            )
        }

        add_temp.setOnClickListener {
            val intent = Intent(this@CommunityActivity, TemplateActivity::class.java)
            intent.putExtra("comm_temp_id", communityID)
            intent.putExtra("comm_temp_name", communityName)
            startActivity(intent)
            overridePendingTransition(
                R.anim.slide_in_right,
                R.anim.slide_out_left
            )
        }
        /*fabAddComm.setOnClickListener {
            animateFAB()
        }
        fabAddPost.setOnClickListener {
            val intent = Intent(this@CommunityActivity, ShowTemplatesActivity::class.java)
            intent.putExtra("comm_temp_id", communityID)
            intent.putExtra("comm_temp_name", communityName)
            startActivity(intent)
            overridePendingTransition(
                R.anim.slide_in_right,
                R.anim.slide_out_left
            )
        }
        fabAddType.setOnClickListener {
            val intent = Intent(this@CommunityActivity, TemplateActivity::class.java)
            intent.putExtra("comm_temp_id", communityID)
            intent.putExtra("comm_temp_name", communityName)
            startActivity(intent)
            overridePendingTransition(
                R.anim.slide_in_right,
                R.anim.slide_out_left
            )
        }

         */


        getInstances(communityID)

        add_temp.isClickable = false
    }
    /*private fun animateFAB(){
        if(isFABOpen){
            val fabAnimation = AnimationUtils.loadAnimation(applicationContext, R.anim.fab_rotate_anticlock)
            val fabAnimation2 = AnimationUtils.loadAnimation(applicationContext, R.anim.fab_close)
            fabAddComm.startAnimation(fabAnimation)
            fabAddType.startAnimation(fabAnimation2)
            fabAddPost.startAnimation(fabAnimation2)
            fabAddType.isClickable = false
            fabAddPost.isClickable = false
            isFABOpen = false
        }
        else{
            val fabAnimation = AnimationUtils.loadAnimation(applicationContext, R.anim.fab_rotate_clock)
            val fabAnimation2 = AnimationUtils.loadAnimation(applicationContext, R.anim.fab_open)
            fabAddComm.startAnimation(fabAnimation)
            fabAddType.startAnimation(fabAnimation2)
            fabAddPost.startAnimation(fabAnimation2)
            fabAddType.isClickable = true
            fabAddPost.isClickable = true
            isFABOpen = true
        }
    }

     */
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
                    pickImageFromGallery()
                }
            }
            else{
                //system OS is < Marshmallow
                pickImageFromGallery()
            }
            true
        }
        R.id.search -> {
            val communityName = intent.getStringExtra("comm_name")
            val intent = Intent(this@CommunityActivity, ShowTemplatesActivity::class.java)
            intent.putExtra("comm_temp_id", communityID)
            intent.putExtra("comm_temp_name", communityName)
            startActivity(intent)
            overridePendingTransition(
                R.anim.slide_in_right,
                R.anim.slide_out_left
            )
            true
        }
        R.id.action_leave -> {
            val communityName = intent.getStringExtra("comm_name")
            unfollowCommunity(communityID, communityName!!)
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
    private fun getOneCommunity(communityID: String){
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

    private fun uploadImage(communityID: String, image: String){
        val res = getSharedPreferences("TOKEN_INFO", Context.MODE_PRIVATE)
        val tokenV = res.getString("token", "Data Not Found!")

        val purApp = RetrofitInstance.getRetrofitInstance().create(ApiInterface::class.java)
        //val path = Context.EXTER
        val file = File(image)
        val requestFile: RequestBody = RequestBody.create(
            MediaType.parse
            ("multipart/form-data"), file)

        val body: MultipartBody.Part = MultipartBody.Part.createFormData(
            "image", file.name.trim(), requestFile)


        purApp.uploadImage(communityID, body, tokenV!!).enqueue(object : Callback<ResponseBody> {
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
                        t.message,
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

            uploadImage(communityID, data!!.dataString!!)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    /*private fun getPathFromURI(uri: Uri) {
        var path: String = uri.path // uri = any content Uri

        val databaseUri: Uri
        val selection: String?
        val selectionArgs: Array<String>?
        if (path.contains("/document/image:")) { // files selected from "Documents"
            databaseUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            selection = "_id=?"
            selectionArgs = arrayOf(DocumentsContract.getDocumentId(uri).split(":")[1])
        } else { // files selected from all other sources, especially on Samsung devices
            databaseUri = uri
            selection = null
            selectionArgs = null
        }
        try {
            val projection = arrayOf(
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.ORIENTATION,
                MediaStore.Images.Media.DATE_TAKEN
            ) // some example data you can query
            val cursor = contentResolver.query(
                databaseUri,
                projection, selection, selectionArgs, null
            )
            if (cursor.moveToFirst()) {
                val columnIndex = cursor.getColumnIndex(projection[0])
                imagePath = cursor.getString(columnIndex)
                // Log.e("path", imagePath);
                imagesPathList.add(imagePath)
            }
            cursor.close()
        } catch (e: Exception) {
            Log.e(TAG, e.message, e)
        }
    }*/

    private fun getInstances(communityID: String){
        val res = getSharedPreferences("TOKEN_INFO", Context.MODE_PRIVATE)
        val tokenV = res.getString("token", "Data Not Found!")
        val purApp = RetrofitInstance.getRetrofitInstance().create(ApiInterface::class.java)

        purApp.getAllInstances(communityID, tokenV!!).enqueue(object : Callback<List<GetInstanceLDBody>> {
            override fun onFailure(call: Call<List<GetInstanceLDBody>>, t: Throwable) {
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
            override fun onResponse(call: Call<List<GetInstanceLDBody>>, response: Response<List<GetInstanceLDBody>>) {
                if (response.code() == 200) {
                    val rv = findViewById<RecyclerView>(R.id.recyclerViewCommunityInstances)
                    rv.layoutManager = LinearLayoutManager(this@CommunityActivity, RecyclerView.VERTICAL, false)

                    val instances = ArrayList<UpperInstanceShowBody>()


                    val resp: List<GetInstanceLDBody>? = response.body()

                    for(i in resp.orEmpty()){
                        val myListTypes: JsonObject = i.instanceFields
                        val templates: JsonObject = i.template.templatesNameId

                        val innerFields = ArrayList<ShowInstanceBody>()
                        val fieldList =  ArrayList<ShowInstanceBody>()

                        for (key in myListTypes.keySet()) {
                            if(key != "@context" && templates.keySet().contains(key)){
                                for (key2 in myListTypes.get(key).asJsonObject.keySet()) {
                                    innerFields.add(ShowInstanceBody(key2, myListTypes.get(key).asJsonObject.get(key2).toString().replace("\"", ""), key))
                                }
                            }
                            else if(key != "@context"){
                                fieldList.add(ShowInstanceBody(key, myListTypes.get(key).toString().replace("\"", ""), "Self"))
                                //users.add(UpperInstanceShowBody(i.createdDate, key, myListTypes.get(key).toString().replace("\"", "")))
                            }
                        }
                        if(!i.name.isNullOrEmpty()){
                            instances.add(UpperInstanceShowBody(i.name ,i.createdDate, fieldList, innerFields))
                        }
                        else{
                            instances.add(UpperInstanceShowBody("Name" ,i.createdDate, fieldList, innerFields))
                        }
                    }
                    if(instances.isEmpty()){
                        Toast.makeText(this@CommunityActivity, "No instance is found!", Toast.LENGTH_SHORT).show()
                    }
                    var adapter = ShowInstanceAdapter(this@CommunityActivity, instances)
                    rv.adapter = adapter
                    adapter.notifyDataSetChanged()


                } else {
                    Toast.makeText(this@CommunityActivity, "Instances cannot retrieve!", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
    private fun unfollowCommunity(communityID: String, communityName: String){
        val res = getSharedPreferences("TOKEN_INFO", Context.MODE_PRIVATE)
        val tokenV = res.getString("token", "Data Not Found!")
        val purApp = RetrofitInstance.getRetrofitInstance().create(ApiInterface::class.java)

        purApp.unfollowCommunity(communityID, tokenV!!).enqueue(object : Callback<ResponseBody> {
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
                        t.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.code() == 200) {
                    Toast.makeText(this@CommunityActivity, "Unfollow successful!", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@CommunityActivity, CommunityFeedActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(
                        R.anim.slide_in_right,
                        R.anim.slide_out_left
                    )
                    finish()
                }
                else if(response.code() == 500){
                    Toast.makeText(this@CommunityActivity, "You have already unfollowed the community!", Toast.LENGTH_SHORT).show()
                }
                else {
                    Toast.makeText(this@CommunityActivity, "Join failed!", Toast.LENGTH_SHORT).show()
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
