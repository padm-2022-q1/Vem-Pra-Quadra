package com.reis.vinicius.vempraquadra.model.repository

import android.app.Application
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.reis.vinicius.vempraquadra.model.entity.Chat
import com.reis.vinicius.vempraquadra.model.firestore.ChatFirestore
import kotlinx.coroutines.tasks.await

class ChatRepository(application: Application): FirestoreRepository<Chat>(application) {
    private val db: FirebaseFirestore = Firebase.firestore
    private val collection = db.collection(Repository.Collections.Chat)

    override suspend fun getAll(): List<Chat> =
        collection.get(getSource()).await().map { it.toObject<ChatFirestore>().toEntity(it.id) }

    override suspend fun getById(id: String): Chat =
        collection.document(id).get(getSource()).await().let { documentSnapshot ->
            documentSnapshot.toObject<ChatFirestore>()?.toEntity(documentSnapshot.id)
                ?: throw Exception("Failed to find item with id $id")
        }

    override suspend fun insert(obj: Chat) {
        collection.add(ChatFirestore.fromEntity(obj)).await()
    }

    override suspend fun update(obj: Chat) {
        collection.document(obj.id).get(getSource()).await()
            .reference.set(ChatFirestore.fromEntity(obj))
    }

    override suspend fun delete(obj: Chat): Boolean =
        collection.document(obj.id).get(getSource()).await().reference.delete().isSuccessful
}