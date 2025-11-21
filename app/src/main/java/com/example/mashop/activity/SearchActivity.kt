package com.example.mashop.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.mashop.Adapter.PopularAdapter
import com.example.mashop.ViewModel.SearchViewModel
import com.example.mashop.databinding.ActivitySearchBinding

class SearchActivity : AppCompatActivity() {

    private lateinit var viewModel: SearchViewModel
    private lateinit var binding: ActivitySearchBinding
    private lateinit var popularAdapter: PopularAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // تهيئة View Binding
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 1. تهيئة الـ ViewModel
        viewModel = ViewModelProvider(this).get(SearchViewModel::class.java)

        // 2. إعداد الـ RecyclerView
        setupRecyclerView()

        // 3. إعداد شريط البحث
        setupSearchView()

        // 4. مراقبة البيانات
        observeData()
    }

    private fun setupRecyclerView() {
        popularAdapter = PopularAdapter(mutableListOf())
        binding.searchResultsRecyclerView.layoutManager = GridLayoutManager(this, 2)
        binding.searchResultsRecyclerView.adapter = popularAdapter
    }

    private fun setupSearchView() {
        // تفعيل شريط البحث تلقائياً عند الدخول للصفحة
        binding.searchView.requestFocus()

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { viewModel.searchProducts(it) }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.searchProducts(newText ?: "")
                return true
            }
        })
    }

    private fun observeData() {
        // مراقبة نتائج البحث
        viewModel.searchResults.observe(this) { products ->
            popularAdapter.updateList(products)
        }

        // مراقبة حالة التحميل
        viewModel.isLoading.observe(this) { isLoading ->
            binding.searchProgressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }
}