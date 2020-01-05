package com.bounswe.purposefulcommunity.Adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bounswe.purposefulcommunity.Fragments.Instances.ShowTemplateActivity
import com.bounswe.purposefulcommunity.Models.ShowTempBody
import com.bounswe.purposefulcommunity.R
import kotlinx.android.synthetic.main.item_temp.view.*

class TemplatesAdapter(val context : Context, val userList: ArrayList<ShowTempBody>): RecyclerView.Adapter<TemplatesAdapter.ViewHolder>() {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setData(userList[position].id, userList[position].name, userList[position].createdDate, userList[position].commID, userList[position].commName, position)
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
            itemView.eachTempItem.setOnClickListener {
                val intent = Intent(context, ShowTemplateActivity::class.java)
                intent.putExtra("temp_id", currentSearchShow?.id.toString())
                intent.putExtra("temp_name", currentSearchShow?.name.toString())
                intent.putExtra("comm_id", currentSearchShow?.commID.toString())
                intent.putExtra("comm_name", currentSearchShow?.commName.toString())
                context.startActivity(intent)
            }
        }
        fun setData(id : String, name : String, createdDate : String,  commID: String, commName: String, position: Int){
            itemView.txtTempName.text = name
            val newDate = "Created: $createdDate"
            itemView.txtCreatedDate.text = newDate

            this.currentSearchShow = ShowTempBody(id, createdDate, name, commID, commName)
            this.currentPosition = position
        }
    }
}