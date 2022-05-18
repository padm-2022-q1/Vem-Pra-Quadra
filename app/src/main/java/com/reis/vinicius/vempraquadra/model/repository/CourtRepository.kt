package com.reis.vinicius.vempraquadra.model.repository

import android.app.Application
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.reis.vinicius.vempraquadra.model.entity.Court
import com.reis.vinicius.vempraquadra.model.firestore.CourtFirestore
import kotlinx.coroutines.tasks.await

class CourtRepository(application: Application): FirestoreRepository<Court>(application) {
    private val db: FirebaseFirestore = Firebase.firestore
    private val collection = db.collection(Repository.Collections.Court)

    companion object {
        private const val nextIdDoc = "courtId"
    }

    override suspend fun getAll(): List<Court> =
        collection.whereGreaterThan(CourtFirestore.Fields.id, 0).get(getSource()).await()
            .toObjects(CourtFirestore::class.java).map { it.toEntity() }

    override suspend fun getById(id: String): Court =
        collection.whereEqualTo(CourtFirestore.Fields.id, id).get(getSource()).await()
            .toObjects(CourtFirestore::class.java).first().toEntity()

    override suspend fun insert(obj: Court) {
        collection.add(CourtFirestore(
            id = nextId(),
            name = obj.name,
            address = obj.address
        )).await()
    }

    override suspend fun update(obj: Court) {
        collection.whereEqualTo(CourtFirestore.Fields.id, obj.id)
            .get(getSource()).await().let{ querySnapshot ->
                if (querySnapshot.isEmpty)
                    throw Exception("Failed to remove element: Id ${obj.id} not found")

                querySnapshot.first().reference.set(CourtFirestore.fromEntity(obj))
            }
    }

    override suspend fun delete(obj: Court) {
        collection.whereEqualTo(CourtFirestore.Fields.id, obj.id)
            .get(getSource()).await().let { querySnapshot ->
                if (querySnapshot.isEmpty)
                    throw Exception("Failed to remove element: Id ${obj.id} not found")

                querySnapshot.first().reference.delete()
            }
    }

    private suspend fun nextId(): Long =
        collection.document(nextIdDoc).get(getSource()).await().let { documentSnapshot ->
            if (documentSnapshot.exists()) {
                val oldValue = documentSnapshot.toObject(LongId::class.java)?.value ?: 0
                LongId(oldValue + 1)
            } else {
                LongId(1)
            }.let { newTaskId ->
                documentSnapshot.reference.set(newTaskId)
                newTaskId.value ?: throw Exception("Nww id should not be null")
            }
        }
}