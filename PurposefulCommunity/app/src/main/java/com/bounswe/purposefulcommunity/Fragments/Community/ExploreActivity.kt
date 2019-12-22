package com.bounswe.purposefulcommunity.Fragments.Community

import android.content.Context
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
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
import kotlinx.android.synthetic.main.activity_explore.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.ConnectException

class ExploreActivity : AppCompatActivity() {
    private var hasHeader: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_explore)

        val actionBar = supportActionBar
        actionBar!!.title = getString(R.string.explore)
        actionBar.setDisplayHomeAsUpEnabled(true)

        getCommunities()
    }

    private fun getCommunities(){
        val res = getSharedPreferences("TOKEN_INFO", Context.MODE_PRIVATE)
        val tokenV = res.getString("token", "Data Not Found!")
        val purApp = RetrofitInstance.getRetrofitInstance().create(ApiInterface::class.java)

        purApp.getComList(tokenV!!).enqueue(object : Callback<List<CommunityBody>> {
            override fun onFailure(call: Call<List<CommunityBody>>, t: Throwable) {
                if(t.cause is ConnectException){
                    Toast.makeText(
                        this@ExploreActivity,
                        "Check your connection!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else{
                    Toast.makeText(
                        this@ExploreActivity,
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
                    rv.layoutManager = LinearLayoutManager(this@ExploreActivity, RecyclerView.VERTICAL, false)

                    val users = ArrayList<CommShowBody>()

                    var adapter = CommunityAdapter(this@ExploreActivity, users)
                    rv.adapter = adapter

                    val res: List<CommunityBody>? = response.body()

                    for(i in res.orEmpty()){
                        users.add(CommShowBody(i.name, i.size.toString(), i.id, i.isPrivate, i.description))
                    }
                    if(users.isEmpty()){
                        Toast.makeText(this@ExploreActivity, "No community is found!", Toast.LENGTH_SHORT).show()
                    }
                    runLayoutAnimation()
                    adapter.notifyDataSetChanged()

                } else {
                    Toast.makeText(this@ExploreActivity, "Communities cannot retrieve!", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
    private fun runLayoutAnimation() = recyclerView.apply {
        layoutAnimation = AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_fall_down)
        adapter?.notifyDataSetChanged()
        scheduleLayoutAnimation()

        if (hasHeader) {
            layoutAnimationListener = object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation?) {
                    layoutManager?.findViewByPosition(0)?.clearAnimation()
                }
                override fun onAnimationEnd(animation: Animation?) = Unit
                override fun onAnimationRepeat(animation: Animation?) = Unit
            }
        }
    }
    override fun onResume() {
        getCommunities()
        super.onResume()
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
