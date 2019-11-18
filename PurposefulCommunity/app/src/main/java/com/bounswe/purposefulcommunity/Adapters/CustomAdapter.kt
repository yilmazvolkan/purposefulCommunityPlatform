package com.bounswe.purposefulcommunity.Adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bounswe.purposefulcommunity.Fragments.ShowCommActivity
import com.bounswe.purposefulcommunity.Models.CommShowBody
import com.bounswe.purposefulcommunity.R
import kotlinx.android.synthetic.main.item_layout.view.*

class CustomAdapter(val context : Context, val userList: ArrayList<CommShowBody>): RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

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
                //Toast.makeText(context, currentSearchShow?.pk.toString(), Toast.LENGTH_SHORT).show()
                /*
                Pass pk value to show profile activity and start activity
                 */
                val intent = Intent(context, ShowCommActivity::class.java)
                intent.putExtra("comm_id", currentSearchShow?.id.toString())
                context.startActivity(intent)
            }
        }
        fun setData(name : String, size : String, comm_id : String, isPrivate : Boolean, position: Int){
            itemView.txtName.text = name
            itemView.txtSize.text = size

            this.currentSearchShow = CommShowBody(name, size, comm_id, isPrivate)
            this.currentPosition = position
        }
    }
}