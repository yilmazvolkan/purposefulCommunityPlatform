package com.bounswe.purposefulcommunity.Fragments.Instances

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bounswe.mercatus.API.ApiInterface
import com.bounswe.mercatus.API.RetrofitInstance
import com.bounswe.purposefulcommunity.Adapters.InstanceAdapter
import com.bounswe.purposefulcommunity.Models.AddTempBody
import com.bounswe.purposefulcommunity.Models.CreateInstanceBody
import com.bounswe.purposefulcommunity.Models.EditModel
import com.bounswe.purposefulcommunity.Models.GetFieldsBody
import com.bounswe.purposefulcommunity.R
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_create_instance.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.ConnectException

class CreateInstanceActivity : AppCompatActivity() {

    lateinit var editModelArrayList: ArrayList<EditModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_instance)

        val tempID = intent.getStringExtra("temp_id")
        val tempName = intent.getStringExtra("temp_name")

        val actionBar = supportActionBar
        actionBar!!.title = "Post: $tempName"
        actionBar.setDisplayHomeAsUpEnabled(true)

        if(tempID!!.isNotEmpty()){
            getFields(tempID)
        }
    }

    private fun getFields(id: String){
        val res = getSharedPreferences("TOKEN_INFO", Context.MODE_PRIVATE)
        val tokenV = res.getString("token", "Data Not Found!")
        val purApp = RetrofitInstance.getRetrofitInstance().create(ApiInterface::class.java)

        purApp.getFields(id, tokenV!!).enqueue(object : Callback<List<GetFieldsBody>> {
            override fun onFailure(call: Call<List<GetFieldsBody>>, t: Throwable) {
                if(t.cause is ConnectException){
                    Toast.makeText(
                        this@CreateInstanceActivity,
                        "Check your connection!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else{
                    Toast.makeText(
                        this@CreateInstanceActivity,
                        "Something bad happened!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            override fun onResponse(call: Call<List<GetFieldsBody>>, response: Response<List<GetFieldsBody>>) {
                if (response.code() == 200) {
                    val rv = findViewById<RecyclerView>(R.id.recyclerViewInstanceValues)
                    rv.layoutManager = LinearLayoutManager(this@CreateInstanceActivity, RecyclerView.VERTICAL, false)

                    val res: List<GetFieldsBody>? = response.body()

                    val fields = ArrayList<AddTempBody>()
                    editModelArrayList = populateList(res!!.size)

                    var adapter = InstanceAdapter(this@CreateInstanceActivity, fields, editModelArrayList)
                    rv.adapter = adapter


                    for(i in res.orEmpty()){
                        fields.add(AddTempBody(i.fieldType, i.isRequired, i.name))
                    }
                    if(fields.isEmpty()){
                        Toast.makeText(this@CreateInstanceActivity, "No field is found!", Toast.LENGTH_SHORT).show()
                    }
                    //runLayoutAnimation()
                    adapter.notifyDataSetChanged()

                    fabSaveIns.setOnClickListener {
                        for (i in 0 until editModelArrayList.size) {
                            Toast.makeText(this@CreateInstanceActivity, editModelArrayList[i].getEditTextValue(), Toast.LENGTH_SHORT).show()
                        }
                        createInstance(fields, id)
                    }

                } else {
                    Toast.makeText(this@CreateInstanceActivity, "Your fields cannot retrieve!", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
    private fun populateList(size : Int): ArrayList<EditModel> {

        val list = ArrayList<EditModel>()

        for (i in 1..size) {
            val editModel = EditModel()
            editModel.setEditTextValue(i.toString())
            list.add(editModel)
        }

        return list
    }

    private fun createInstance(fields : ArrayList<AddTempBody>, templateId: String){
        val res = getSharedPreferences("TOKEN_INFO", Context.MODE_PRIVATE)
        val tokenV = res.getString("token", "Data Not Found!")

        val purApp = RetrofitInstance.getRetrofitInstance().create(ApiInterface::class.java)

        val jsonRes = JsonObject()

        for (i in 0 until editModelArrayList.size) {
            jsonRes.addProperty(fields[i].name, editModelArrayList[i].getEditTextValue())
        }
        val instanceBody = CreateInstanceBody(jsonRes, templateId)

        purApp.createInstance(instanceBody, tokenV!!).enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                if(t.cause is ConnectException){
                    Toast.makeText(
                        this@CreateInstanceActivity,
                        "Check your connection!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else{
                    Toast.makeText(
                        this@CreateInstanceActivity,
                        "Something bad happened!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.code() == 200) {
                    Toast.makeText(this@CreateInstanceActivity, "Instance is created successfully!", Toast.LENGTH_SHORT).show()
                    onSupportNavigateUp()
                } else {
                    Toast.makeText(this@CreateInstanceActivity, "Instance create is failed!", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
    override fun onSupportNavigateUp(): Boolean {
        finish()
        onBackPressed()
        return true
    }
    override fun finish() {
        super.finish()
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
    }
}
