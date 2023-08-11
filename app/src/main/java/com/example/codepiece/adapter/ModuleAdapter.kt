package com.example.codepiece.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.codepiece.R
import com.example.codepiece.data.ModuleItem

class ModuleAdapter(private val context: Context, private val moduleItems: List<ModuleItem>) :
    RecyclerView.Adapter<ModuleAdapter.ModuleViewHolder>() {

    private var mOnClickListener: OnClickListener? = null

    inner class ModuleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.item_image)
        val nameTextView: TextView = itemView.findViewById(R.id.item_name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ModuleViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.custom_module, parent, false)
        return ModuleViewHolder(view)
    }

    override fun onBindViewHolder(holder: ModuleViewHolder, position: Int) {
        val currentItem = moduleItems[position]
        holder.nameTextView.text = currentItem.name
        Glide.with(context).load(currentItem.imageResId).into(holder.imageView)
        holder.itemView.setOnClickListener {
            if (mOnClickListener != null) {
                mOnClickListener!!.onClick(position, currentItem)
            }
        }
    }

    override fun getItemCount(): Int {
        return moduleItems.size
    }

    fun setOnclickListener(onClickListener: OnClickListener) {
        this.mOnClickListener = onClickListener
    }

    interface OnClickListener {
        fun onClick(position: Int, moduleItem: ModuleItem)
    }
}
