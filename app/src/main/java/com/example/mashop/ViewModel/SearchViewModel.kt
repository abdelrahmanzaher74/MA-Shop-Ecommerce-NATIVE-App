package com.example.mashop.ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mashop.Model.ItemsModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class SearchViewModel : ViewModel() {

    // تحديد العقدة الرئيسية بناءً على اسم الموديل، افترضنا أنها "items"
    private val databaseRef = FirebaseDatabase.getInstance().getReference("Items")

    private val _searchResults = MutableLiveData<List<ItemsModel>>()
    val searchResults: LiveData<List<ItemsModel>> = _searchResults

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private var searchJob: Job? = null // لتتبع مهمة البحث وإلغائها (Debouncing)

    fun searchProducts(query: String) {
        searchJob?.cancel() // إلغاء أي بحث سابق

        val trimmedQuery = query.trim()

        if (trimmedQuery.isBlank() || trimmedQuery.length < 2) {
            // إفراغ النتائج إذا كان البحث فارغاً أو قصيراً
            _searchResults.value = emptyList()
            _isLoading.value = false
            return
        }

        searchJob = viewModelScope.launch {
            _isLoading.value = true
            delay(350) // Debouncing: انتظر 350 مللي ثانية

            if (isActive) {
                performFirebaseSearch(trimmedQuery)
            }
        }
    }

    private fun performFirebaseSearch(query: String) {

        // تجهيز الاستعلام: يتم تحويل الحرف الأول إلى حالة كبيرة (UpperCase) للمطابقة الدقيقة مع Firebase
        val capitalizedQuery = query.replaceFirstChar {
            if (it.isLowerCase()) it.titlecase() else it.toString()
        }

        // استخدام خاصية 'title' في استعلام البحث
        databaseRef
            .orderByChild("title")
            .startAt(capitalizedQuery)
            .endAt(capitalizedQuery + "\uf8ff") // البحث حسب البادئة
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val products = snapshot.children.mapNotNull {
                        it.getValue(ItemsModel::class.java)
                    }
                    _searchResults.postValue(products)
                    _isLoading.postValue(false)
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("SearchViewModel", "Firebase Search Error: ${error.message}")
                    _searchResults.postValue(emptyList())
                    _isLoading.postValue(false)
                }
            })
    }
}