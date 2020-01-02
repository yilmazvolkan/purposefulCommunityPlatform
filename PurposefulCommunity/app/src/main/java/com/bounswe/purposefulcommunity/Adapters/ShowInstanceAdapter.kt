package com.bounswe.purposefulcommunity.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.setPadding
import androidx.recyclerview.widget.RecyclerView
import com.bounswe.purposefulcommunity.Models.ShowInstanceBody
import com.bounswe.purposefulcommunity.Models.UpperInstanceShowBody
import com.bounswe.purposefulcommunity.R
import kotlinx.android.synthetic.main.item_show_inst.view.*

class ShowInstanceAdapter(val context : Context, val userList: ArrayList<UpperInstanceShowBody>, val fieldList: ArrayList<ShowInstanceBody>): RecyclerView.Adapter<ShowInstanceAdapter.ViewHolder>() {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setData(userList[position].createdDate, userList[position].nameField, userList[position].valueField, position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_show_inst, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return userList.size
    }
    /*
    Each item in RecyclerView is called as viewholder.
     */
    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var currentSearchShow : UpperInstanceShowBody? = null
        var currentPosition : Int = 0

        fun setData(createdDate: String, nameField: String, valueField: String, position: Int){

            val newDate = "Created: $createdDate"
            //itemView.txtInstanceDate.text = newDate

            val param = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT)
            param.weight = 3.0f

            val param2 = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT)
            param2.weight = 6.0f

            val tvDynamic0 = TextView(context)
            tvDynamic0.textSize = 12f
            tvDynamic0.setPadding(4)
            tvDynamic0.text = "Related number of results: " + userList.size
            itemView.InstanceOutCard.addView(tvDynamic0)

            val tvDynamic = TextView(context)
            tvDynamic.layoutParams = param2
            tvDynamic.textSize = 14f
            tvDynamic.setPadding(4)
            tvDynamic.text = nameField

            val tvDynamic2 = TextView(context)
            tvDynamic2.layoutParams = param
            tvDynamic2.textSize = 14f
            tvDynamic.setPadding(4)
            tvDynamic2.text = valueField


            itemView.txtInstanceCard.addView(tvDynamic)
            itemView.txtInstanceCard.addView(tvDynamic2)

            if(fieldList.size > 0){
                for (i in 0 until fieldList.size){

                    val linearCH = LinearLayout(context)
                    linearCH.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT)
                    linearCH.orientation = LinearLayout.HORIZONTAL


                    val tvDynamic = TextView(context)
                    tvDynamic.layoutParams = param2
                    tvDynamic.textSize = 14f
                    tvDynamic.setPadding(4)
                    //tvDynamic.textAlignment = TextView.TEXT_ALIGNMENT_CENTER
                    tvDynamic.text = fieldList[i].name + " : " + fieldList[i].parent

                    val tvDynamic2 = TextView(context)
                    tvDynamic2.layoutParams = param
                    tvDynamic2.textSize = 14f
                    tvDynamic.setPadding(4)
                    //tvDynamic.textAlignment = TextView.TEXT_ALIGNMENT_CENTER
                    tvDynamic2.text = fieldList[i].value

                    /*val param3 = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT)
                    param3.weight = 2.0f

                    val tvDynamic3 = TextView(context)
                    tvDynamic3.layoutParams = param3
                    tvDynamic3.textSize = 14f
                    tvDynamic.setPadding(4)
                    //tvDynamic.textAlignment = TextView.TEXT_ALIGNMENT_CENTER
                    tvDynamic3.text = fieldList[i].parent

                     */

                    linearCH.addView(tvDynamic)
                    linearCH.addView(tvDynamic2)
                    //linearCH.addView(tvDynamic3)
                    //itemView.txtInstanceCard.setBackgroundColor(Color.parseColor("#FFDFDC"))

                    itemView.InstanceOutCard.addView(linearCH)
                }
            }

            val tvDynamic3 = TextView(context)
            tvDynamic3.textSize = 12f
            tvDynamic3.setPadding(4)
            tvDynamic3.text = newDate
            itemView.InstanceOutCard.addView(tvDynamic3)

            this.currentSearchShow = UpperInstanceShowBody(createdDate, nameField, valueField)
            this.currentPosition = position
        }
    }
}