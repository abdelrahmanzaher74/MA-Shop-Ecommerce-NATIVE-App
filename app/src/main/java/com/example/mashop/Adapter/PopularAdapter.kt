package com.example.mashop.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.request.RequestOptions
import com.example.mashop.Model.ItemsModel
import com.example.mashop.activity.DetailActivity
import com.example.mashop.databinding.ViewholderRecommendedBinding
class PopularAdapter(val items:MutableList<ItemsModel>): RecyclerView.Adapter<PopularAdapter.Viewholder> () {
    private  var context: Context? = null
    class Viewholder (val binding: ViewholderRecommendedBinding): RecyclerView.ViewHolder(binding.root)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularAdapter.Viewholder {
        context = parent.context
        val binding = ViewholderRecommendedBinding.inflate(LayoutInflater.from(context),parent,false)
        return Viewholder(binding)
    }
    override fun onBindViewHolder(holder: PopularAdapter.Viewholder, position: Int) {
        holder.binding.titletxt.text = items[position].title
        holder.binding.pricetxt.text = "$" + items[position].price.toString()
        holder.binding.ratingtxt.text = items[position].rating.toString()

        val requestOptions = RequestOptions().transform(CenterCrop())
        Glide.with(holder.itemView.context)
            .load(items[position].picUrl[0])
            .apply(requestOptions)
            .into(holder.binding.pic)

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, DetailActivity::class.java)
            intent.putExtra("object",items[position])
            holder.itemView.context.startActivity(intent)
        }
    }
    override fun getItemCount(): Int = items.size
}