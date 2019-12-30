package com.bounswe.purposefulcommunity.Fragments.Templates

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bounswe.mercatus.API.ApiInterface
import com.bounswe.mercatus.API.RetrofitInstance
import com.bounswe.purposefulcommunity.Adapters.TempPickAdapter
import com.bounswe.purposefulcommunity.Models.GetTempBody
import com.bounswe.purposefulcommunity.Models.ShowTempBody
import com.bounswe.purposefulcommunity.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.ConnectException

class SelectTempActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_temp)

        val actionBar = supportActionBar
        actionBar!!.title = getString(R.string.pick_type)
        actionBar.setDisplayHomeAsUpEnabled(true)

        val communityID = intent.getStringExtra("comm_id")
        getTemplates(communityID)
    }
    private fun getTemplates(communityID: String){
        val res = getSharedPreferences("TOKEN_INFO", Context.MODE_PRIVATE)
        val tokenV = res.getString("token", "Data Not Found!")
        val purApp = RetrofitInstance.getRetrofitInstance().create(ApiInterface::class.java)

        purApp.getTemplates(communityID, tokenV!!).enqueue(object : Callback<List<GetTempBody>> {
            override fun onFailure(call: Call<List<GetTempBody>>, t: Throwable) {
                if(t.cause is ConnectException){
                    Toast.makeText(
                        this@SelectTempActivity,
                        "Check your connection!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else{
                    Toast.makeText(
                        this@SelectTempActivity,
                        "Something bad happened!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            override fun onResponse(call: Call<List<GetTempBody>>, response: Response<List<GetTempBody>>) {
                if (response.code() == 200) {
                    val array: MutableList<String> = ArrayList()
                    for (i in response.body()!!){
                        array.add(i.name)
                    }
                    val rv = findViewById<RecyclerView>(R.id.recyclerViewPickTemplates)
                    rv.layoutManager = LinearLayoutManager(this@SelectTempActivity, RecyclerView.VERTICAL, false)

                    val users = ArrayList<ShowTempBody>()

                    var adapter = TempPickAdapter(this@SelectTempActivity, users)
                    rv.adapter = adapter


                    val res: List<GetTempBody>? = response.body()

                    for(i in res.orEmpty()){
                        users.add(ShowTempBody(i.id, i.createdDate, i.name))
                    }
                    if(users.isEmpty()){
                        Toast.makeText(this@SelectTempActivity, "No template is found!", Toast.LENGTH_SHORT).show()
                    }
                    adapter.notifyDataSetChanged()

                } else {
                    Toast.makeText(this@SelectTempActivity, "Templates cannot retrieve!", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
