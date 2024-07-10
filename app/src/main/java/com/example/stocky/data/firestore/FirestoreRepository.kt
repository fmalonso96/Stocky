package com.example.stocky.data.firestore

import android.util.Log
import com.example.stocky.model.product.Product
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.toObject

private const val PRODUCTS = "products"

class FirestoreRepository {

    private val db = FirestoreDatabase.getInstance()

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
                Log.w("Get Products", "Error adding document", e)
            }
    }

    /*Tanto add como update son identicas, pero quiero tener una funcion diferente para cada uno.
    Solo son identicas porque Firestore usa el .set para añadir si no existe o update si existe.*/

    fun addProduct(data: Product, onSuccess: () -> Unit) {
        db.collection(PRODUCTS).document(data.id)
            .set(data)
            .addOnSuccessListener {
                Log.d("Add Product", "DocumentSnapshot added with ID: ${data.id}")
                onSuccess()
            }
            .addOnFailureListener { e ->
                Log.w("Add Product", "Error adding document", e)
            }
    }

    fun updateProduct(data: Product, onSuccess: () -> Unit) {
        db.collection(PRODUCTS).document(data.id)
            .set(data)
            .addOnSuccessListener {
                Log.d("Update Product", "DocumentSnapshot updated with ID: ${data.id}")
                onSuccess()
            }
            .addOnFailureListener { e ->
                Log.w("Update Product", "Error updating document", e)
            }
    }
}