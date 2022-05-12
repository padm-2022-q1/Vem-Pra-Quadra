package com.reis.vinicius.vempraquadra.model.match

import android.app.Application
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.reis.vcom.reis.vinicius.vempraquadra.model.courtinicius.vempraquadra.model.FirebaseRepository
import com.reis.vinicius.vempraquadra.model.RepositoryFactory
import com.reis.vinicius.vempraquadra.model.chat.ChatFirestore
import com.reis.vinicius.vempraquadra.model.court.Court
import com.reis.vinicius.vempraquadra.model.court.CourtFirestore
import kotlinx.coroutines.tasks.await

class MatchRepository(application: Application): FirebaseRepository<Match>(application) {
    private val db: FirebaseFirestore = Firebase.firestore
    private val collection = db.collection(RepositoryFactory.Collections.Match)

    override suspend fun getAll(): List<Match> =
        collection.get(getSource()).await()
            .map { it.toObject<MatchFirestore>().toEntity(it.id) }

    override suspend fun getById(id: String): Match =
        collection.document(id).get(getSource()).await().let { documentSnapshot ->
            documentSnapshot.toObject<MatchFirestore>()?.toEntity(documentSnapshot.id)
                ?: throw Exception("Failed to get match with id $id")
        }

    override suspend fun insert(obj: Match) {
        val newMatch = collection.document()
        val newChat = db.collection(RepositoryFactory.Collections.Chat).document()

        db.runTransaction { transaction ->
            transaction
                .set(newMatch, MatchFirestore.fromEntity(obj))
                .set(newChat, ChatFirestore(newMatch.id))
        }.await()
    }

    override suspend fun update(obj: Match) {
        collection.document(obj.id).get(getSource()).await()
            .reference.set(MatchFirestore.fromEntity(obj))
    }

    override suspend fun delete(obj: Match): Boolean =
        collection.document(obj.id).get(getSource()).await().reference.delete().isSuccessful
}