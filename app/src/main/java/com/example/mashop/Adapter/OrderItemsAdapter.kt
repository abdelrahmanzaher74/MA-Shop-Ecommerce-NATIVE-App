package com.example.mashop.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mashop.Model.ItemsModel // 🎯 نموذج المنتجات الخاص بك
import com.example.mashop.R

class OrderItemsAdapter(private val itemsList: List<ItemsModel>) :
    RecyclerView.Adapter<OrderItemsAdapter.ItemViewHolder>() {

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // يجب أن تتوافق هذه IDs مع تصميم item_order_item_card.xml
        val tvItemName: TextView = itemView.findViewById(R.id.tvItemName)
        val tvItemQuantity: TextView = itemView.findViewById(R.id.tvItemQuantity)
        val tvItemPrice: TextView = itemView.findViewById(R.id.tvItemPrice)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_order_item_card, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = itemsList[position]

        // ItemsModel يحتوي على price و numberInCart
        val itemTotal = item.price * item.numberInCart

        holder.tvItemName.text = item.title
        holder.tvItemQuantity.text = "Qty: ${item.numberInCart}"

        holder.tvItemPrice.text = "$${String.format("%.2f", itemTotal)}"
    }

    override fun getItemCount() = itemsList.size
}