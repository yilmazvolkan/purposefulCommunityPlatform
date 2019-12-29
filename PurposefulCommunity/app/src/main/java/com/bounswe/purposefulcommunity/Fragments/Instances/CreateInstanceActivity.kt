package com.bounswe.purposefulcommunity.Fragments.Instances

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.isDigitsOnly
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

                    for(i in res.orEmpty()){
                        fields.add(AddTempBody(i.fieldType, i.isRequired, i.name))
                    }
                    if(fields.isEmpty()){
                        Toast.makeText(this@CreateInstanceActivity, "No field is found!", Toast.LENGTH_SHORT).show()
                    }
                    editModelArrayList = populateList(fields.size, fields)

                    var adapter = InstanceAdapter(this@CreateInstanceActivity, fields, editModelArrayList)
                    rv.adapter = adapter
                    //runLayoutAnimation()
                    adapter.notifyDataSetChanged()

                    fabSaveIns.setOnClickListener {
                        createInstance(fields, id)
                    }

                } else {
                    Toast.makeText(this@CreateInstanceActivity, "Your fields cannot retrieve!", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
    private fun populateList(size: Int, fields : ArrayList<AddTempBody>): ArrayList<EditModel> {

        val list = ArrayList<EditModel>()

        for (i in 0 until size) {
            val editModel = EditModel()
            if(fields[i].fieldType == "TIME"){
                editModel.setEditTextValue("hh:mm:ss")
            }
            else if(fields[i].fieldType == "DATE"){
                editModel.setEditTextValue("yyyy:MM:dd")
            }
            else if(fields[i].fieldType == "DATE_TIME"){
                editModel.setEditTextValue("yyyy:MM:ddThh:mm:ss")
            }
            else if(fields[i].fieldType == "DECIMAL"){
                editModel.setEditTextValue("Number")
            }
            else if(fields[i].fieldType == "STRING"){
                editModel.setEditTextValue("Text")
            }
            else if(fields[i].fieldType == "BOOLEAN"){
                editModel.setEditTextValue("True or False")
            }
            else if(fields[i].fieldType == "FLOAT"){
                editModel.setEditTextValue("Rational")
            }
            else{
                editModel.setEditTextValue("Value")
            }
            list.add(editModel)
        }
        return list
    }

    private fun createInstance(fields : ArrayList<AddTempBody>, templateId: String){
        val res = getSharedPreferences("TOKEN_INFO", Context.MODE_PRIVATE)
        val tokenV = res.getString("token", "Data Not Found!")

        val purApp = RetrofitInstance.getRetrofitInstance().create(ApiInterface::class.java)

        val jsonRes = JsonObject()

        var isValid = true

        for (i in 0 until editModelArrayList.size) {

            if(fields[i].fieldType == "FLOAT"){
                if(editModelArrayList[i].getEditTextValue()!!.matches("-?\\d+(\\.\\d+)?".toRegex())){
                    jsonRes.addProperty(fields[i].name, editModelArrayList[i].getEditTextValue()!!.toFloat())
                }
                else{
                    isValid = false
                    Toast.makeText(this@CreateInstanceActivity, "Invalid number input!", Toast.LENGTH_SHORT).show()
                }
            }
            else if(fields[i].fieldType == "DECIMAL"){
                if(editModelArrayList[i].getEditTextValue()!!.isDigitsOnly()){
                    jsonRes.addProperty(fields[i].name, editModelArrayList[i].getEditTextValue()!!.toInt())
                }
                else{
                    isValid = false
                    Toast.makeText(this@CreateInstanceActivity, "Invalid number input!", Toast.LENGTH_SHORT).show()
                }
            }
            else if(fields[i].fieldType == "TIME"){
                if(editModelArrayList[i].getEditTextValue()!!.matches("[0-9]{2}:[0-9]{2}:[0-9]{2}".toRegex())){
                    jsonRes.addProperty(fields[i].name, editModelArrayList[i].getEditTextValue())
                }
                else{
                    isValid = false
                    Toast.makeText(this@CreateInstanceActivity, "Invalid time input!", Toast.LENGTH_SHORT).show()
                }
            }
            else if(fields[i].fieldType == "DATE"){
                if(editModelArrayList[i].getEditTextValue()!!.matches("[0-9]{4}-[0-9]{2}-[0-9]{2}".toRegex())){
                    jsonRes.addProperty(fields[i].name, editModelArrayList[i].getEditTextValue())
                }
                else{
                    isValid = false
                    Toast.makeText(this@CreateInstanceActivity, "Invalid date input!", Toast.LENGTH_SHORT).show()
                }
            }
            else if(fields[i].fieldType == "DATE_TIME"){
                if(editModelArrayList[i].getEditTextValue()!!.matches("[0-9]{2}-[0-9]{2}-[0-9]{4}T[0-9]{2}:[0-9]{2}:[0-9]{2}".toRegex())){
                    jsonRes.addProperty(fields[i].name, editModelArrayList[i].getEditTextValue())
                }
                else{
                    isValid = false
                    Toast.makeText(this@CreateInstanceActivity, "Invalid date/time input!", Toast.LENGTH_SHORT).show()
                }
            }
            else if(fields[i].fieldType == "BOOLEAN"){
                if(editModelArrayList[i].getEditTextValue()!!.equals("true", true)
                    || editModelArrayList[i].getEditTextValue()!!.equals("false", true)){

                    jsonRes.addProperty(fields[i].name, editModelArrayList[i].getEditTextValue()!!.toBoolean())
                }
                else{
                    isValid = false
                    Toast.makeText(this@CreateInstanceActivity, "Invalid date/time input!", Toast.LENGTH_SHORT).show()
                }
            }
            else{
                jsonRes.addProperty(fields[i].name, editModelArrayList[i].getEditTextValue())
            }
        }
        if(isValid){
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
