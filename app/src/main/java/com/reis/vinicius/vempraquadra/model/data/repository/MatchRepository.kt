package com.reis.vinicius.vempraquadra.model.data.repository

import android.app.Application
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.reis.vinicius.vempraquadra.model.data.entity.Match
import com.reis.vinicius.vempraquadra.model.data.firestore.MatchFirestore
import kotlinx.coroutines.tasks.await

class MatchRepository(application: Application): FirebaseRepository<Match>(application) {
    private val db: FirebaseFirestore = Firebase.firestore
    private val collection = db.collection(MatchFirestore.CollectionName)

    override suspend fun getAll(): List<Match> =
        collection.get(getSource()).await()
            .toObjects(MatchFirestore::class.java).map { it.toEntity() }

    override suspend fun getById(id: String): Match =
        collection.whereEqualTo(MatchFirestore.Fields.id, id).get(getSource()).await()
            .toObjects(MatchFirestore::class.java).first().toEntity()

    override suspend fun insert(obj: Match): String = MatchFirestore(
        id = "",
        name = obj.name,
        date = obj.date,
        courtId = obj.court?.id,
        chatId = obj.chat?.id
    ).let {
        collection.add(it)
        it.id ?: throw Exception("Failed to add match")
    }

    override suspend fun update(obj: Match) {
        collection.whereEqualTo(MatchFirestore.Fields.id, obj.id)
            .get(getSource()).await().let{ querySnapshot ->
                if (querySnapshot.isEmpty)
                    throw Exception("Failed to remove element: Id ${obj.id} not found")

                querySnapshot.first().reference.set(MatchFirestore.fromEntity(obj))
            }
    }

    override suspend fun delete(obj: Match) {
        collection.whereEqualTo(MatchFirestore.Fields.id, obj.id)
            .get(getSource()).await().let { querySnapshot ->
                if (querySnapshot.isEmpty)
                    throw Exception("Failed to remove Match: Id ${obj.id} not found")

                querySnapshot.first().reference.delete()
            }
    }

    override suspend fun deleteMany(objects: List<Match>?) {
        val results = if (objects == null) collection
        else collection.whereIn(MatchFirestore.Fields.id, objects.map { it.id })

        results.get(getSource()).await().forEach { queryDocumentSnapshot ->
            queryDocumentSnapshot.reference.delete()
        }
    }
}