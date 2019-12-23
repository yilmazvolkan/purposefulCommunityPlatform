package com.bounswe.purposefulcommunity.Fragments.Instances

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bounswe.mercatus.API.ApiInterface
import com.bounswe.mercatus.API.RetrofitInstance
import com.bounswe.purposefulcommunity.Adapters.FieldsAdapter
import com.bounswe.purposefulcommunity.Models.AddTempBody
import com.bounswe.purposefulcommunity.Models.GetFieldsBody
import com.bounswe.purposefulcommunity.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.ConnectException

class CreateInstanceActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_instance)

        val tempID = intent.getStringExtra("temp_id")
        val tempName = intent.getStringExtra("temp_name")

        val actionBar = supportActionBar
        actionBar!!.title = "Post: $tempName"
        actionBar.setDisplayHomeAsUpEnabled(true)

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
                    val rv = findViewById<RecyclerView>(R.id.recyclerViewFields)
                    rv.layoutManager = LinearLayoutManager(this@CreateInstanceActivity, RecyclerView.VERTICAL, false)

                    val fields = ArrayList<AddTempBody>()

                    var adapter = FieldsAdapter(this@CreateInstanceActivity, fields)
                    rv.adapter = adapter

                    val res: List<GetFieldsBody>? = response.body()

                    for(i in res.orEmpty()){
                        fields.add(AddTempBody(i.fieldType, i.isRequired, i.name))
                    }
                    if(fields.isEmpty()){
                        Toast.makeText(this@CreateInstanceActivity, "No field is found!", Toast.LENGTH_SHORT).show()
                    }
                    //runLayoutAnimation()
                    adapter.notifyDataSetChanged()

                } else {
                    Toast.makeText(this@CreateInstanceActivity, "Your fields cannot retrieve!", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
}
