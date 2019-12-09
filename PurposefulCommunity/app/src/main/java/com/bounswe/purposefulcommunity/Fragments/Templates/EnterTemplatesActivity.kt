package com.bounswe.purposefulcommunity.Fragments.Templates

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bounswe.mercatus.API.ApiInterface
import com.bounswe.mercatus.API.RetrofitInstance
import com.bounswe.purposefulcommunity.Models.AddTempBody
import com.bounswe.purposefulcommunity.Models.CreateTemplateBody
import com.bounswe.purposefulcommunity.R
import kotlinx.android.synthetic.main.activity_enter_templates.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.ConnectException

class EnterTemplatesActivity : AppCompatActivity() {

    private var types = arrayOf("Text", "Boolean", "Decimal", "Rational", "Duration", "Date-Time", "Date", "Time")
    private var w3ctypes = arrayOf("STRING", "BOOLEAN", "DECIMAL", "FLOAT", "DURATION", "DATE_TIME", "DATE", "TIME")
    private val results = ArrayList<AddTempBody>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_enter_templates)

        val actionBar = supportActionBar
        actionBar!!.title = "Create Template"
        actionBar.setDisplayHomeAsUpEnabled(true)

        val tempName = intent.getStringExtra("temp_name")
        val tempSize = intent.getStringExtra("temp_size")
        val communityID = intent.getStringExtra("comm_temp_id")

        val aa = ArrayAdapter(this, android.R.layout.simple_spinner_item, types)

        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        templateCreator(tempSize!!.toInt(), aa)

        create_send.setOnClickListener {
            //if(templateIsValid()){
                templateSetter(tempSize.toInt())
            //TODO  ID null geliyor
                createTemplate(communityID!!, tempName!!)
            //}
        }
    }
    private fun templateSetter(size : Int){
        for(i in 1..size){
            if(i == 1){
                results.add(AddTempBody(w3ctypes[type1.selectedItemPosition], editPropName1.text.toString()))
            }
            else if(i == 2){
                results.add(AddTempBody(w3ctypes[type2.selectedItemPosition], editPropName2.text.toString()))
            }
            else if(i == 3){
                results.add(AddTempBody(w3ctypes[type3.selectedItemPosition], editPropName3.text.toString()))
            }
            else if(i == 4){
                results.add(AddTempBody(w3ctypes[type4.selectedItemPosition], editPropName4.text.toString()))
            }
            else if(i == 5){
                results.add(AddTempBody(w3ctypes[type5.selectedItemPosition], editPropName5.text.toString()))
            }
            else if(i == 6){
                results.add(AddTempBody(w3ctypes[type6.selectedItemPosition], editPropName6.text.toString()))
            }
            else if(i == 7){
                results.add(AddTempBody(w3ctypes[type7.selectedItemPosition], editPropName7.text.toString()))
            }
        }
    }
    private fun templateCreator(size : Int, aa: ArrayAdapter<String>){
        for(i in 1..size){
            if(i == 1){
                item1.visibility = View.VISIBLE
                type1.adapter = aa
            }
            else if(i == 2){
                item2.visibility = View.VISIBLE
                type2.adapter = aa
            }
            else if(i == 3){
                item3.visibility = View.VISIBLE
                type3.adapter = aa
            }
            else if(i == 4){
                item4.visibility = View.VISIBLE
                type4.adapter = aa
            }
            else if(i == 5){
                item5.visibility = View.VISIBLE
                type5.adapter = aa
            }
            else if(i == 6){
                item6.visibility = View.VISIBLE
                type6.adapter = aa
            }
            else if(i == 7){
                item7.visibility = View.VISIBLE
                type7.adapter = aa
            }
        }
    }
    private fun createTemplate(communityId: String, name: String){
        val res = getSharedPreferences("TOKEN_INFO", Context.MODE_PRIVATE)
        val tokenV = res.getString("token", "Data Not Found!")
        val userID = res.getString("user_id", "Data Not Found!")

        val purApp = RetrofitInstance.getRetrofitInstance().create(ApiInterface::class.java)

        val tempBody = CreateTemplateBody(communityId, results, name, userID!!)

        purApp.createTemp(tempBody, tokenV!!).enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                if(t.cause is ConnectException){
                    Toast.makeText(
                        this@EnterTemplatesActivity,
                        "Check your connection!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else{
                    Toast.makeText(
                        this@EnterTemplatesActivity,
                        "Something bad happened!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.code() == 200) {
                    Toast.makeText(this@EnterTemplatesActivity, "Template is created successfully!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@EnterTemplatesActivity, "Template create is failed!", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
    private fun templateIsValid():Boolean{
        var isValid = true

        if (editPropName1.text.toString().isEmpty()){
            layPropName1.isErrorEnabled = true
            layPropName1.error = "Name 1 cannot be empty!"
            isValid = false
        }else{
            layPropName1.isErrorEnabled = false
        }
        if (editPropName2.text.toString().isEmpty()){
            layPropName2.isErrorEnabled = true
            layPropName2.error = "Name 2 cannot be empty!"
            isValid = false
        }else{
            layPropName2.isErrorEnabled = false
        }
        if (editPropName3.text.toString().isEmpty()){
            layPropName3.isErrorEnabled = true
            layPropName3.error = "Name 3 cannot be empty!"
            isValid = false
        }else{
            layPropName3.isErrorEnabled = false
        }
        if (editPropName4.text.toString().isEmpty()){
            layPropName4.isErrorEnabled = true
            layPropName4.error = "Name 4 cannot be empty!"
            isValid = false
        }else{
            layPropName4.isErrorEnabled = false
        }
        if (editPropName5.text.toString().isEmpty()){
            layPropName5.isErrorEnabled = true
            layPropName5.error = "Name 5 cannot be empty!"
            isValid = false
        }else{
            layPropName5.isErrorEnabled = false
        }
        if (editPropName6.text.toString().isEmpty()){
            layPropName6.isErrorEnabled = true
            layPropName6.error = "Name 6 cannot be empty!"
            isValid = false
        }else{
            layPropName6.isErrorEnabled = false
        }
        if (editPropName7.text.toString().isEmpty()){
            layPropName7.isErrorEnabled = true
            layPropName7.error = "Name 7 cannot be empty!"
            isValid = false
        }else{
            layPropName7.isErrorEnabled = false
        }
        return isValid
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
