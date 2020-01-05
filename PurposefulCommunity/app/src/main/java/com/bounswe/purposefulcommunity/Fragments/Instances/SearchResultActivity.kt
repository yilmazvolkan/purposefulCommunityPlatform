package com.bounswe.purposefulcommunity.Fragments.Instances

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bounswe.mercatus.API.ApiInterface
import com.bounswe.mercatus.API.RetrofitInstance
import com.bounswe.purposefulcommunity.Adapters.ShowInstanceAdapter
import com.bounswe.purposefulcommunity.Models.GetInstanceLDBody
import com.bounswe.purposefulcommunity.Models.ShowInstanceBody
import com.bounswe.purposefulcommunity.Models.UpperInstanceShowBody
import com.bounswe.purposefulcommunity.R
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.ConnectException

class SearchResultActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_result)
        searchResult()

        val actionBar = supportActionBar
        actionBar!!.title = getString(R.string.search)
        actionBar.setDisplayHomeAsUpEnabled(true)
    }
    private fun searchResult(){
        val res = getSharedPreferences("TOKEN_INFO", Context.MODE_PRIVATE)
        val tokenV = res.getString("token", "Data Not Found!")
        val purApp = RetrofitInstance.getRetrofitInstance().create(ApiInterface::class.java)
        val tempID = intent.getStringExtra("temp_id")
        val jsonRes = intent.getStringExtra("json_res")

        var obj2 = JsonParser().parse(jsonRes).asJsonObject

        purApp.searchInstance(true, tempID!!, tokenV!!, obj2).enqueue(object : Callback<List<GetInstanceLDBody>> {
            override fun onFailure(call: Call<List<GetInstanceLDBody>>, t: Throwable) {
                if(t.cause is ConnectException){
                    Toast.makeText(
                        this@SearchResultActivity,
                        "Check your connection!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else{
                    Toast.makeText(
                        this@SearchResultActivity,
                        "Something bad happened!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            override fun onResponse(call: Call<List<GetInstanceLDBody>>, response: Response<List<GetInstanceLDBody>>) {
                if (response.code() == 200) {
                    val rv = findViewById<RecyclerView>(R.id.recyclerViewSearchResult)
                    rv.layoutManager = LinearLayoutManager(this@SearchResultActivity, RecyclerView.VERTICAL, false)

                    val users = ArrayList<UpperInstanceShowBody>()
                    val innerFields = ArrayList<ShowInstanceBody>()

                    val resp: List<GetInstanceLDBody>? = response.body()
                    for(i in resp.orEmpty()){
                        val myListTypes: JsonObject = i.instanceFields
                        val templates: JsonObject = i.template.templatesNameId

                        for (key in myListTypes.keySet()) {
                            if(key != "@context" && templates.keySet().contains(key)){
                                for (key2 in myListTypes.get(key).asJsonObject.keySet()) {
                                    innerFields.add(ShowInstanceBody(key2, myListTypes.get(key).asJsonObject.get(key2).toString().replace("\"", ""), key))
                                }
                            }
                            else if(key != "@context"){
                                users.add(UpperInstanceShowBody(i.createdDate, key, myListTypes.get(key).toString().replace("\"", "")))
                            }
                        }
                    }
                    if(users.isEmpty()){
                        Toast.makeText(this@SearchResultActivity, "No instance is found!", Toast.LENGTH_SHORT).show()
                    }
                    var adapter = ShowInstanceAdapter(this@SearchResultActivity, users, innerFields)
                    rv.adapter = adapter
                    adapter.notifyDataSetChanged()
                } else {
                    Toast.makeText(this@SearchResultActivity, "Search failed!", Toast.LENGTH_SHORT).show()
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
