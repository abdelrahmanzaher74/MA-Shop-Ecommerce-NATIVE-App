package com.example.mashop.Adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.mashop.Model.Order
import com.example.mashop.R // تأكد من استيراد R
import com.example.mashop.activity.OrderDetailActivity // سنفترض أن هذه هي صفحة التفاصيل الجديدة

class OrdersAdapter(private val ordersList: List<Order>) :
    RecyclerView.Adapter<OrdersAdapter.OrderViewHolder>() {

    class OrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvOrderId: TextView = itemView.findViewById(R.id.tvOrderId)
        val tvOrderDate: TextView = itemView.findViewById(R.id.tvOrderDate)
        val tvOrderStatus: TextView = itemView.findViewById(R.id.tvOrderStatus)
        val tvOrderTotal: TextView = itemView.findViewById(R.id.tvOrderTotal)
        val btnViewDetails: TextView = itemView.findViewById(R.id.btnViewDetails)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_order_card, parent, false)
        return OrderViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val order = ordersList[position]
        holder.tvOrderId.text = "#${order.id.substring(order.id.length - 4)}" // عرض آخر 4 أرقام
        holder.tvOrderDate.text = order.date
        holder.tvOrderTotal.text = "$${order.total}"
        holder.tvOrderStatus.text = order.status

        // **منطق حالة الطلب:** يضمن ظهور الألوان الصحيحة
        when (order.status) {
            "Delivered" -> {
                holder.tvOrderStatus.setBackgroundResource(R.drawable.status_background_green)
            }
            "In Transit" -> {
                holder.tvOrderStatus.setBackgroundResource(R.drawable.status_background_purple)
            }
            "Processing" -> {
                // يمكنك استخدام لون مختلف للقيد المعالجة
                holder.tvOrderStatus.setBackgroundResource(R.drawable.status_background_gray)
                holder.tvOrderStatus.setTextColor(ContextCompat.getColor(holder.itemView.context, android.R.color.black))
            }
            "Cancelled" -> {
                holder.tvOrderStatus.setBackgroundResource(R.drawable.status_background_red)
            }
            else -> {
                holder.tvOrderStatus.setBackgroundResource(R.drawable.status_background_gray)
            }
        }

        // **منطق النقر:** لفتح صفحة التفاصيل والتتبع
        holder.btnViewDetails.setOnClickListener {
            val intent = Intent(holder.itemView.context, OrderDetailActivity::class.java).apply {
                // يجب تمرير رقم الطلب لتتمكن صفحة التفاصيل من جلب بياناته
                putExtra("ORDER_ID", order.id)
            }
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount() = ordersList.size
}