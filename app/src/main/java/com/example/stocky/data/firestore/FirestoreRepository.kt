package com.example.stocky.data.firestore

import android.util.Log
import com.example.stocky.model.product.Product
import com.example.stocky.model.sale.Sale
import com.google.firebase.firestore.toObject
import java.text.SimpleDateFormat
import java.util.*

private const val PRODUCTS = "products"
private const val SALES = "sales"

class FirestoreRepository {

    private val db = FirestoreDatabase.getInstance()
    private val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
    private val today = dateFormat.format(Date())

    fun getProducts(callback: (List<Product>) -> Unit) {
        db.collection(PRODUCTS)
            .get()
            .addOnSuccessListener { result ->
                val productList = mutableListOf<Product>()
                for (document in result) {
                    productList.add(document.toObject<Product>())
                }
                callback(productList)
            }
            .addOnFailureListener { e ->
                Log.w("Get Products", "Error getting products", e)
            }
    }

    fun getSales(callback: (List<Sale>) -> Unit) {
        db.collection(SALES)
            .whereEqualTo("date", today)
            .get()
            .addOnSuccessListener { result ->
                val salesList = mutableListOf<Sale>()
                for (document in result) {
                    salesList.add(document.toObject<Sale>())
                }
                callback(salesList)
            }
            .addOnFailureListener { e ->
                Log.w("Get Sales", "Error getting sales", e)
            }
    }

    fun addProduct(data: Product, onSuccess: () -> Unit){
        db.collection(PRODUCTS).document(data.id)
            .set(data)
            .addOnSuccessListener {
                onSuccess()
                Log.d("Add Product", "Product added with ID: ${data.id}")
            }
            .addOnFailureListener { e ->
                Log.w("Get Products", "Error adding product", e)
            }
    }

    fun updateProduct(data: Product, onSuccess: () -> Unit) {
        db.collection(PRODUCTS).document(data.id)
            .set(data)
            .addOnSuccessListener {
                onSuccess()
                Log.d("Update Product", "Product updated with ID: ${data.id}")
            }
            .addOnFailureListener { e ->
                Log.w("Update Product", "Error updating product", e)
            }
    }

    fun addSale(data: Sale, onSuccess: () -> Unit) {
        db.collection(SALES).document(data.id)
            .set(data)
            .addOnSuccessListener {
                onSuccess()
                Log.d("Add Sale", "Sale added with ID: ${data.id}")
            }
            .addOnFailureListener { e ->
                Log.w("Add Sale", "Error adding sale", e)
            }
    }
}