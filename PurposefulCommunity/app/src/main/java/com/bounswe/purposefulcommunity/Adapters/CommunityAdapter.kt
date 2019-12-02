package com.bounswe.purposefulcommunity.Adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bounswe.mercatus.API.ApiInterface
import com.bounswe.mercatus.API.RetrofitInstance
import com.bounswe.purposefulcommunity.Fragments.Community.CommunityActivity
import com.bounswe.purposefulcommunity.Fragments.Community.ShowCommActivity
import com.bounswe.purposefulcommunity.Models.CommShowBody
import com.bounswe.purposefulcommunity.Models.GetOneCommBody
import com.bounswe.purposefulcommunity.R
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.item_layout.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.ConnectException

class CommunityAdapter(val context : Context, val userList: ArrayList<CommShowBody>): RecyclerView.Adapter<CommunityAdapter.ViewHolder>() {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setData(userList[position].name, userList[position].size.toString(), userList[position].id, userList[position].isPrivate, position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return userList.size
    }
    /*
    Each item in RecyclerView is called as viewholder.
     */
    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var currentSearchShow : CommShowBody? = null
        var currentPosition : Int = 0
        init {
            itemView.eachItem.setOnClickListener {

                getMyFollowings(currentSearchShow?.id.toString(), currentSearchShow?.name.toString())
            }
        }
        fun setData(name : String, size : String, comm_id : String, isPrivate : Boolean, position: Int){
            itemView.txtName.text = name
            itemView.txtSize.text = size

            Glide.with(context)
                .load(R.drawable.tea)
                .apply(RequestOptions().circleCrop())
                .into(itemView.profileImage)
            this.currentSearchShow = CommShowBody(name, size, comm_id, isPrivate)
            this.currentPosition = position
        }
    }

    private fun getMyFollowings(comm_id: String, comm_name: String){
        val purApp = RetrofitInstance.getRetrofitInstance().create(ApiInterface::class.java)

        val res = context.getSharedPreferences("TOKEN_INFO", Context.MODE_PRIVATE)
        val tokenV = res?.getString("token", "Data Not Found!")

        purApp.getMyFollowing(tokenV!!).enqueue(object :
            Callback<List<GetOneCommBody>> {
            override fun onFailure(call: Call<List<GetOneCommBody>>, t: Throwable) {
                if(t.cause is ConnectException){
                    Toast.makeText(
                        context,
                        "Check your connection!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else{
                    Toast.makeText(
                        context,
                        "Something bad happened!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            override fun onResponse(call: Call<List<GetOneCommBody>>, response: Response<List<GetOneCommBody>>) {
                if (response.code() == 200) {
                    val resp: List<GetOneCommBody>? = response.body()
                    var isJoined = false
                    for(item in resp.orEmpty()){
                        if(comm_id == item.id){
                            val intent = Intent(context, CommunityActivity::class.java)
                            intent.putExtra("comm_id", comm_id)
                            intent.putExtra("comm_name", comm_name)
                            context.startActivity(intent)
                            isJoined = true
                        }
                    }
                    if(!isJoined){
                        val intent = Intent(context, ShowCommActivity::class.java)
                        intent.putExtra("comm_id", comm_id)
                        intent.putExtra("comm_name", comm_name)
                        context.startActivity(intent)
                    }
                }
                else  {
                    Toast.makeText(context, "Get my followings is failed.", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        })
    }
}