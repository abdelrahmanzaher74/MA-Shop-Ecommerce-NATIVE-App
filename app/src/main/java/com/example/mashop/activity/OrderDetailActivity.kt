package com.example.mashop.activity

import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat // تم الإضافة: للاستخدام الصحيح للألوان
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mashop.Adapter.OrdersAdapter
import com.example.mashop.Helper.OrderManager
import com.example.mashop.Model.Order
import com.example.mashop.R
import com.example.mashop.databinding.ActivityOrderDetailBinding
import com.example.mashop.Adapter.OrderItemsAdapter

class OrderDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOrderDetailBinding
    private var order: Order? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backBtn.setOnClickListener { finish() }

        // 1. استقبال ID الطلب
        val orderId = intent.getStringExtra("ORDER_ID")

        if (orderId != null) {
            // 2. البحث عن الطلب في OrderManager
            order = OrderManager.getOrderById(orderId)

            if (order != null) {
                displayOrderDetails(order!!)
                setupOrderTrackingTimeline(order!!.status)
            } else {
                Toast.makeText(this, "Order not found.", Toast.LENGTH_SHORT).show()
                finish()
            }
        } else {
            finish()
        }
    }

    private fun displayOrderDetails(order: Order) {
        binding.tvDetailOrderId.text = "#${order.id}"
        binding.tvDetailOrderDate.text = "Date: ${order.date}"
        binding.tvShippingAddress.text = order.shippingAddress
        binding.tvTotalSummary.text = "Total: $${order.total}"

        // 🟢 التصحيح: استخدام OrderItemsAdapter
        binding.rvOrderItems.layoutManager = LinearLayoutManager(this)
        binding.rvOrderItems.adapter = OrderItemsAdapter(order.items)
    }

    private fun setupOrderTrackingTimeline(currentStatus: String) {
        // المراحل الثابتة
        val steps = listOf("Order Placed", "Processing", "Shipped", "Delivered")
        val container = binding.timelineContainer

        // بناء الـ Timeline
        steps.forEachIndexed { index, step ->
            val isCompleted = isStepCompleted(step, currentStatus)

            val statusView = TextView(this).apply {
                text = if (isCompleted) "✅ $step" else "⚪ $step"
                textSize = 16f
                setPadding(0, 10, 0, 10)

                // 🟢 التعديل: استخدام ContextCompat.getColor ولون افتراضي (بدلاً من R.color.purple)
                val statusColor = if (isCompleted) ContextCompat.getColor(context, R.color.purple_500) else ContextCompat.getColor(context, android.R.color.darker_gray)
                setTextColor(statusColor)
            }
            container.addView(statusView)
        }
    }

    private fun isStepCompleted(step: String, currentStatus: String): Boolean {
        val allSteps = listOf("Order Placed", "Processing", "Shipped", "Delivered")
        val currentStatusIndex = allSteps.indexOf(currentStatus)
        val stepIndex = allSteps.indexOf(step)

        return stepIndex <= currentStatusIndex
    }
}