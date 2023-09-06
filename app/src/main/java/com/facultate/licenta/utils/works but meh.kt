package com.facultate.licenta.utils

import com.facultate.licenta.model.Order

class works {
    //save orders to user
//     suspend fun saveOrder(newOrder: Order, email: String) {
//        val orderData = mapOrderToFirebaseData(newOrder)
//
//        val userDocument = firestore.collection("Orders").document(email)
//
//        firestore.runTransaction { transaction ->
//            val document = transaction.get(userDocument)
//
//
//
//            if (document.exists()) {
//                val ordersList =
//                    document.get("orders") as? MutableList<HashMap<String, Any>> ?: mutableListOf()
//                ordersList.add(orderData as HashMap<String, Any>)
//
//                transaction.update(userDocument, "orders", ordersList)
//            } else {
//
//                transaction.set(
//                    document.reference,
//                    hashMapOf(
//                        "orders" to listOf(orderData as HashMap<String, Any>),
//                        "email" to email
//                    )
//                )
//            }
//        }.addOnSuccessListener {
//            // Transaction succeeded
//            println("added")
//        }.addOnFailureListener { e ->
//            // Transaction failed
//            println("$e")
//
//        }
//    }

}