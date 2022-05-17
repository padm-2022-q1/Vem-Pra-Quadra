package com.reis.vinicius.vempraquadra.model.repository

import android.app.Application
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.reis.vinicius.vempraquadra.model.firestore.UserDataFirestore
import com.reis.vinicius.vempraquadra.model.entity.UserData
import kotlinx.coroutines.tasks.await

class UserDataRepository(application: Application): FirestoreRepository<UserData>(application) {
    private val db: FirebaseFirestore = Firebase.firestore
    private val collection = db.collection(Repository.Collections.UserData)

    override suspend fun getAll(): List<UserData> =
        collection.get(getSource()).await()
            .map { it.toObject(UserDataFirestore::class.java).toEntity(it.id) }

    override suspend fun getById(id: String): UserData =
        collection.whereEqualTo(FieldPath.documentId(), id).get(getSource()).await()
            .first().let { it.toObject(UserDataFirestore::class.java).toEntity(it.id) }

    override suspend fun insert(obj: UserData){
        collection.document(obj.id).set(UserDataFirestore.fromEntity(obj))
    }

    override suspend fun update(obj: UserData) {
        collection.whereEqualTo(FieldPath.documentId(), obj.id)
            .get(getSource()).await().let{ querySnapshot ->
                if (querySnapshot.isEmpty)
                    throw Exception("Failed to remove element: Id ${obj.id} not found")

                querySnapshot.first().reference.set(UserDataFirestore.fromEntity(obj))
            }
    }

    override suspend fun delete(obj: UserData) {
        collection.whereEqualTo(FieldPath.documentId(), obj.id)
            .get(getSource()).await().let { querySnapshot ->
                if (querySnapshot.isEmpty)
                    throw Exception("Failed to remove Match: Id ${obj.id} not found")

                querySnapshot.first().reference.delete()
            }
    }
}