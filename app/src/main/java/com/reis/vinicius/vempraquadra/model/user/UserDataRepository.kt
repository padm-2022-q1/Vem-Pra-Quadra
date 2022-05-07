package com.reis.vinicius.vempraquadra.model.user

import android.app.Application
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.reis.vcom.reis.vinicius.vempraquadra.model.courtinicius.vempraquadra.model.FirebaseRepository
import kotlinx.coroutines.tasks.await

class UserDataRepository(application: Application): FirebaseRepository<UserData>(application) {
    private val db: FirebaseFirestore = Firebase.firestore
    private val collection = db.collection(UserDataFirestore.CollectionName)

    override suspend fun getAll(): List<UserData> =
        collection.get(getSource()).await()
            .toObjects(UserDataFirestore::class.java).map { it.toEntity() }

    override suspend fun getById(id: String): UserData =
        collection.whereEqualTo(UserDataFirestore.Fields.id, id).get(getSource()).await()
            .toObjects(UserDataFirestore::class.java).first().toEntity()

    override suspend fun insert(obj: UserData): String = UserDataFirestore(
        id = "",
        name = obj.name,
        userName = obj.userName,
        gender = obj.gender,
        birth = obj.birth
    ).let {
        collection.add(it)
        it.id ?: throw Exception("Failed to add match")
    }

    override suspend fun update(obj: UserData) {
        collection.whereEqualTo(UserDataFirestore.Fields.id, obj.id)
            .get(getSource()).await().let{ querySnapshot ->
                if (querySnapshot.isEmpty)
                    throw Exception("Failed to remove element: Id ${obj.id} not found")

                querySnapshot.first().reference.set(UserDataFirestore.fromEntity(obj))
            }
    }

    override suspend fun delete(obj: UserData) {
        collection.whereEqualTo(UserDataFirestore.Fields.id, obj.id)
            .get(getSource()).await().let { querySnapshot ->
                if (querySnapshot.isEmpty)
                    throw Exception("Failed to remove Match: Id ${obj.id} not found")

                querySnapshot.first().reference.delete()
            }
    }

    override suspend fun deleteMany(objects: List<UserData>?) {
        val results = if (objects == null) collection
        else collection.whereIn(UserDataFirestore.Fields.id, objects.map { it.id })

        results.get(getSource()).await().forEach { queryDocumentSnapshot ->
            queryDocumentSnapshot.reference.delete()
        }
    }
}