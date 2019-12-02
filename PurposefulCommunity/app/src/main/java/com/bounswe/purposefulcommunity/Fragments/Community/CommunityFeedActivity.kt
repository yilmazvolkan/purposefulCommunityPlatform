package com.bounswe.purposefulcommunity.Fragments.Community

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bounswe.mercatus.API.ApiInterface
import com.bounswe.mercatus.API.RetrofitInstance
import com.bounswe.purposefulcommunity.Adapters.CommunityAdapter
import com.bounswe.purposefulcommunity.Models.CommShowBody
import com.bounswe.purposefulcommunity.Models.CommunityBody
import com.bounswe.purposefulcommunity.R
import kotlinx.android.synthetic.main.activity_feed_community.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.ConnectException

class CommunityFeedActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feed_community)

        val actionBar = supportActionBar
        actionBar!!.title = getString(R.string.communities)

        fab.setOnClickListener {
            createCommunity()
        }

        getCommunities()
    }
    private fun createCommunity(){
        val intent = Intent(this@CommunityFeedActivity, CreateCommActivity::class.java)
        startActivity(intent)
        overridePendingTransition(
            R.anim.slide_in_right,
            R.anim.slide_out_left
        )
    }
    private fun getCommunities(){
        val res = getSharedPreferences("TOKEN_INFO", Context.MODE_PRIVATE)
        val tokenV = res.getString("token", "Data Not Found!")
        val purApp = RetrofitInstance.getRetrofitInstance().create(ApiInterface::class.java)

        purApp.getComList(tokenV!!).enqueue(object : Callback<List<CommunityBody>> {
            override fun onFailure(call: Call<List<CommunityBody>>, t: Throwable) {
                if(t.cause is ConnectException){
                    Toast.makeText(
                        this@CommunityFeedActivity,
                        "Check your connection!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else{
                    Toast.makeText(
                        this@CommunityFeedActivity,
                        "Something bad happened!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            override fun onResponse(call: Call<List<CommunityBody>>, response: Response<List<CommunityBody>>) {
                if (response.code() == 200) {
                    val array: MutableList<String> = ArrayList()
                    for (i in response.body()!!){
                        array.add(i.name)
                    }
                    val rv = findViewById<RecyclerView>(R.id.recyclerView)
                    rv.layoutManager = LinearLayoutManager(this@CommunityFeedActivity, RecyclerView.VERTICAL, false)

                    val users = ArrayList<CommShowBody>()

                    var adapter = CommunityAdapter(this@CommunityFeedActivity, users)
                    rv.adapter = adapter

                    val res: List<CommunityBody>? = response.body()

                    for(i in res.orEmpty()){
                        users.add(CommShowBody(i.name, i.size.toString(), i.id, i.isPrivate))
                    }
                    if(users.isEmpty()){
                        Toast.makeText(this@CommunityFeedActivity, "No users found!", Toast.LENGTH_SHORT).show()
                    }
                    adapter.notifyDataSetChanged()

                } else {
                    Toast.makeText(this@CommunityFeedActivity, "Com get failed.!", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
    override fun finish() {
        super.finish()
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
    }
}
