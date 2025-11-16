package com.example.mashop.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mashop.Helper.ManagmentCart
import com.example.mashop.Helper.OrderManager // تأكد من استيراد OrderManager
import com.example.mashop.databinding.ActivityCheckoutBinding

class CheckoutActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCheckoutBinding
    private lateinit var managmentCart: ManagmentCart
    private var finalTotal: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheckoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        managmentCart = ManagmentCart(this)

        // 1. استقبال الإجمالي من CartActivity
        finalTotal = intent.getDoubleExtra("TOTAL_AMOUNT", 0.0)

        setVariable()
        updateSummary()
    }

    private fun setVariable() {
        binding.backBtn.setOnClickListener { finish() }

        binding.placeOrderBtn.setOnClickListener {
            handlePlaceOrder()
        }
    }

    private fun updateSummary() {
        // عرض الإجمالي النهائي
        binding.tvFinalTotal.text = "Final Total: $${finalTotal}"
    }

    private fun handlePlaceOrder() {
        val address = binding.etAddress.text.toString().trim()

        // التحقق من صحة الإدخال
        if (address.isEmpty() || managmentCart.getListCart().isEmpty() || finalTotal <= 0) {
            Toast.makeText(this, "Please enter a valid address and ensure your cart is not empty.", Toast.LENGTH_LONG).show()
            return
        }

        // تحديد طريقة الدفع المختارة
        val selectedPaymentId = binding.rgPaymentMethod.checkedRadioButtonId
        val paymentMethod = when (selectedPaymentId) {
            binding.rbCreditCard.id -> "Credit Card"
            else -> "Cash on Delivery"
        }

        // 1. إنشاء الطلب محلياً
        val items = managmentCart.getListCart()
        val newOrder = OrderManager.createNewOrder(
            items = items,
            total = finalTotal,
            address = address,
            payment = paymentMethod
        )

        // 2. مسح السلة بعد النجاح

        // 3. الانتقال إلى صفحة النجاح (OrderSuccessActivity)
        val intent = Intent(this, OrderSuccessActivity::class.java).apply {
            putExtra("ORDER_ID", newOrder.id)
        }
        startActivity(intent)
        finishAffinity() // إغلاق الأنشطة السابقة (السلة والدفع)
    }
}