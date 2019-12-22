package com.bounswe.purposefulcommunity.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bounswe.purposefulcommunity.Models.ShowTempBody
import com.bounswe.purposefulcommunity.R
import kotlinx.android.synthetic.main.item_layout.view.eachItem
import kotlinx.android.synthetic.main.item_temp.view.*

class TemplatesAdapter(val context : Context, val userList: ArrayList<ShowTempBody>): RecyclerView.Adapter<TemplatesAdapter.ViewHolder>() {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setData(userList[position].id, userList[position].name, userList[position].createdDate, position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_temp, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return userList.size
    }
    /*
    Each item in RecyclerView is called as viewholder.
     */
    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var currentSearchShow : ShowTempBody? = null
        var currentPosition : Int = 0
        init {
            itemView.eachItem.setOnClickListener {

            }
        }
        fun setData(id : String, name : String, createdDate : String,  position: Int){
            itemView.txtTempName.text = name
            val newDate = "Date: " + createdDate
            itemView.txtCreatedDate.text = newDate

            this.currentSearchShow = ShowTempBody(id, createdDate, name)
            this.currentPosition = position
        }
    }
}