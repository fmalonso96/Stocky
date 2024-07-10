package com.example.stocky.presentation.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stocky.data.firestore.FirestoreRepository
import com.example.stocky.model.product.Product
import kotlinx.coroutines.launch

class ProductsViewModel : ViewModel() {

    private val firestoreRepository = FirestoreRepository()

    private val _products = MutableLiveData<List<Product>>()
    val products: LiveData<List<Product>> = _products

    var currentProduct by mutableStateOf(Product())

    init {
        viewModelScope.launch {
            loadProducts()
        }
    }

    private fun loadProducts() {
        firestoreRepository.getProducts { _products.postValue(it) }
    }

    fun addProduct(data: Product) {
        viewModelScope.launch {
            firestoreRepository.addProduct(data) { loadProducts() }
        }
    }

    fun updateProduct(data: Product) {
        viewModelScope.launch {
            firestoreRepository.updateProduct(data) { loadProducts() }
        }
    }
}