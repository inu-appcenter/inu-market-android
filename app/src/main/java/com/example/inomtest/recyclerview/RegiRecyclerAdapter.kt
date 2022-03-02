package com.example.inomtest.recyclerview

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.inomtest.R

class RegiRecyclerAdapter(private val items: ArrayList<Uri>, val context: Context) :
    RecyclerView.Adapter<RegiRecyclerAdapter.ViewHolder>() {

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        Glide.with(context).load(item)
            .override(500, 500)
            .into(holder.image)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val inflatedView = LayoutInflater.from(parent.context).inflate(R.layout.item_upload_image, parent, false)
        return ViewHolder(inflatedView)
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        private var view: View = v
        var image = v.findViewById<ImageView>(R.id.upload_imageView)

        fun bind(listener: View.OnClickListener, item: String) {
            view.setOnClickListener(listener)
        }
    }
}