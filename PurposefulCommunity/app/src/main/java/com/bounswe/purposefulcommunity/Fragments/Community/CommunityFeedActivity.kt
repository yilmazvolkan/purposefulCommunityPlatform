package com.bounswe.purposefulcommunity.Fragments.Community

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bounswe.mercatus.API.ApiInterface
import com.bounswe.mercatus.API.RetrofitInstance
import com.bounswe.purposefulcommunity.Adapters.CommunityAdapter
import com.bounswe.purposefulcommunity.Fragments.LoginActivity
import com.bounswe.purposefulcommunity.Models.CommShowBody
import com.bounswe.purposefulcommunity.Models.GetOneCommBody
import com.bounswe.purposefulcommunity.R
import kotlinx.android.synthetic.main.activity_explore.*
import kotlinx.android.synthetic.main.activity_feed_community.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.ConnectException

class CommunityFeedActivity : AppCompatActivity() {
    private var hasHeader: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feed_community)

        val actionBar = supportActionBar
        actionBar!!.title = getString(R.string.my_communities)

        fab.setOnClickListener {
            createCommunity()
        }

        getMyCommunities()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.explore -> {
            val intent = Intent(this@CommunityFeedActivity, ExploreActivity::class.java)
            startActivity(intent)
            overridePendingTransition(
                R.anim.slide_in_right,
                R.anim.slide_out_left
            )
            true
        }
        R.id.action_logout -> {
            val preferences = getSharedPreferences("TOKEN_INFO", Context.MODE_PRIVATE)
            val editor = preferences.edit()
            editor.putString("token", " ")
            editor.apply()
            val intent = Intent(this@CommunityFeedActivity, LoginActivity::class.java)
            startActivity(intent)
            Toast.makeText(this, "Log out success!", Toast.LENGTH_LONG).show()
            finish()
            true
        }
        else -> {
            // If we got here, the user's action was not recognized.
            // Invoke the superclass to handle it.
            super.onOptionsItemSelected(item)
        }
    }
    private fun createCommunity(){
        val intent = Intent(this@CommunityFeedActivity, CreateCommActivity::class.java)
        startActivity(intent)
        overridePendingTransition(
            R.anim.slide_in_right,
            R.anim.slide_out_left
        )
    }
    private fun getMyCommunities(){
        val res = getSharedPreferences("TOKEN_INFO", Context.MODE_PRIVATE)
        val tokenV = res.getString("token", "Data Not Found!")
        val purApp = RetrofitInstance.getRetrofitInstance().create(ApiInterface::class.java)

        purApp.getMyFollowing(tokenV!!).enqueue(object : Callback<List<GetOneCommBody>> {
            override fun onFailure(call: Call<List<GetOneCommBody>>, t: Throwable) {
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
            override fun onResponse(call: Call<List<GetOneCommBody>>, response: Response<List<GetOneCommBody>>) {
                if (response.code() == 200) {
                    val rv = findViewById<RecyclerView>(R.id.recyclerView)
                    rv.layoutManager = LinearLayoutManager(this@CommunityFeedActivity, RecyclerView.VERTICAL, false)

                    val users = ArrayList<CommShowBody>()

                    var adapter = CommunityAdapter(this@CommunityFeedActivity, users)
                    rv.adapter = adapter

                    val res: List<GetOneCommBody>? = response.body()

                    for(i in res.orEmpty()){
                        users.add(CommShowBody(i.name, i.size.toString(), i.id, i.isPrivate, i.description))
                    }
                    if(users.isEmpty()){
                        Toast.makeText(this@CommunityFeedActivity, "No community is found!", Toast.LENGTH_SHORT).show()
                    }
                    runLayoutAnimation()
                    adapter.notifyDataSetChanged()

                } else {
                    Toast.makeText(this@CommunityFeedActivity, "Your communities cannot retrieve!", Toast.LENGTH_SHORT).show()
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
        getMyCommunities()
        super.onResume()
    }
    override fun finish() {
        super.finish()
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
    }
}
