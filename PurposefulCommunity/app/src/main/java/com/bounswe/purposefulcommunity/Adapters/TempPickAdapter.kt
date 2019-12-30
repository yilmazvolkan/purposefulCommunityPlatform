package com.bounswe.purposefulcommunity.Adapters

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bounswe.purposefulcommunity.Fragments.Templates.TemplateActivity
import com.bounswe.purposefulcommunity.Models.ShowTempBody
import com.bounswe.purposefulcommunity.R
import kotlinx.android.synthetic.main.item_pick_temp.*
import kotlinx.android.synthetic.main.item_temp.view.*

class TempPickAdapter(val context : Context, val userList: ArrayList<ShowTempBody>): RecyclerView.Adapter<TempPickAdapter.ViewHolder>() {

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
            itemView.eachTempItem.setOnClickListener {
                val color = ContextCompat.getColor(context, R.color.colorPrimary)
                val dialogBuilder = AlertDialog.Builder(context)
                    .setTitle("Give Name")
                    .setCancelable(true)
                    .create()
                val editView = LayoutInflater.from(context).inflate(R.layout.item_pick_temp, null)
                dialogBuilder.setView(editView)
                dialogBuilder.setButton(AlertDialog.BUTTON_POSITIVE, "Add", DialogInterface.OnClickListener{
                        dialog, _ ->
                    val name = dialogBuilder.editTemplateName.text
                    if(name.toString().isEmpty()){
                        Toast.makeText(context, "Name cannot be empty!", Toast.LENGTH_SHORT).show()
                    }
                    else{
                        val intent = Intent(context, TemplateActivity::class.java)
                        intent.putExtra("comm_temp_id", " ")
                        intent.putExtra("comm_temp_name", " ")
                        intent.putExtra("type_new_name", name.toString())
                        intent.putExtra("type_name", currentSearchShow!!.name)
                        intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
                        context.startActivity(intent)
                        dialog.dismiss()
                    }
                })
                dialogBuilder.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", DialogInterface.OnClickListener {
                        dialog, _ ->
                    dialog.dismiss()

                })
                dialogBuilder.show()
                dialogBuilder.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(color)
                dialogBuilder.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(color)
            }
        }
        fun setData(id : String, name : String, createdDate : String,  position: Int){
            itemView.txtTempName.text = name
            val newDate = "Created: $createdDate"
            itemView.txtCreatedDate.text = newDate

            this.currentSearchShow = ShowTempBody(id, createdDate, name)
            this.currentPosition = position
        }
    }
}