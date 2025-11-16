package com.example.mashop.activity


import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.mashop.databinding.ActivityOrderSuccessBinding

class OrderSuccessActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOrderSuccessBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderSuccessBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // استقبال رقم الطلب من CheckoutActivity
        val orderId = intent.getStringExtra("ORDER_ID")

        if (orderId != null) {
            binding.tvOrderId.text = "Your Order ID: #$orderId"
        }

        setListeners()
    }

    private fun setListeners() {
        // العودة إلى الشاشة الرئيسية (MainActivity)
        binding.btnContinueShopping.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            // مسح كل الأنشطة السابقة باستثناء الرئيسية
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }

        // الانتقال لصفحة عرض الطلبات (التي سننشئها في الخطوة التالية)
        binding.btnViewOrders.setOnClickListener {
            val intent = Intent(this, MyOrdersActivity::class.java)
            startActivity(intent)
            finish()
        }
    }


}