package com.example.mashop.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mashop.Adapter.OrdersAdapter
import com.example.mashop.Helper.OrderManager // استيراد OrderManager
import com.example.mashop.databinding.ActivityMyOrdersBinding // استخدام View Binding

class MyOrdersActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMyOrdersBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // تهيئة View Binding
        binding = ActivityMyOrdersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setVariable()
        setupOrdersRecyclerView()
    }

    private fun setVariable() {
        // زر الرجوع
        binding.backBtn.setOnClickListener { startActivity(Intent(this, MainActivity::class.java)) }
    }

    private fun setupOrdersRecyclerView() {
        // **المنطق الأساسي:** جلب طلبات المستخدم الحالي فقط
        val userOrders = OrderManager.getMyOrders()

        // تهيئة الـ RecyclerView
        binding.rvOrders.layoutManager = LinearLayoutManager(this)
        binding.rvOrders.adapter = OrdersAdapter(userOrders)

        // منطق عرض/إخفاء رسالة "لا يوجد طلبات"
        with(binding) {
            emptyTxt.visibility = if (userOrders.isEmpty()) View.VISIBLE else View.GONE
            rvOrders.visibility = if (userOrders.isEmpty()) View.GONE else View.VISIBLE
        }
    }
}