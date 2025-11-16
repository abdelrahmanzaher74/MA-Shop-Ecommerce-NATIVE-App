package com.example.mashop.Helper

import com.example.mashop.Model.ItemsModel
import com.example.mashop.Model.Order
import java.text.SimpleDateFormat
import java.util.*

object OrderManager {

    // **1. تخزين الطلبات:** قائمة ثابتة تخزن جميع الطلبات (محاكاة لقاعدة البيانات)
    private val allOrders = mutableListOf<Order>()

    // **2. معرف المستخدم الحالي:** (يجب استبداله بمنطق تسجيل الدخول الفعلي)
    // هذا يضمن أن يتم ربط الطلب بالمستخدم الصحيح.
    fun getCurrentUserId(): String {
        // TODO: مستقبلاً، استبدل هذا لجلب معرف المستخدم الحقيقي المسجل دخوله
        // مؤقتاً: نستخدم معرف ثابت للاختبار
        return "user_123"
    }

    /**
     * وظيفة إنشاء طلب جديد (تُستدعى من CheckoutActivity)
     */
    fun createNewOrder(
        items: List<ItemsModel>,
        total: Double,
        address: String,
        payment: String
    ): Order{
        val currentUserId = getCurrentUserId()
        val newOrderId = System.currentTimeMillis().toString() // ID فريد للطلب

        val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        val dateString = dateFormat.format(Date())

        val newOrder = Order(
            id = newOrderId,
            userId = currentUserId,
            date = dateString,
            total = total,
            status = "Processing", // الحالة الأولية للطلب
            items = items,
            shippingAddress = address,
            paymentMethod = payment
        )

        allOrders.add(newOrder)
        return newOrder
    }

    /**
     * وظيفة جلب طلبات المستخدم الحالي فقط (تُستدعى من MyOrdersActivity)
     */
    fun getMyOrders(): List<Order> {
        val currentUserId = getCurrentUserId()
        // تصفية القائمة لعرض الطلبات التي تطابق معرف المستخدم الحالي فقط
        return allOrders
            .filter { it.userId == currentUserId }
            .sortedByDescending { it.id } // عرض الأحدث أولاً
    }
    fun getOrderById(orderId: String): Order? {
        return allOrders.find { it.id == orderId }
    }
}