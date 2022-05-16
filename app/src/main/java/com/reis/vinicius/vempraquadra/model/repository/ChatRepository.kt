package com.reis.vinicius.vempraquadra.model.repository

import android.app.Application
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.reis.vinicius.vempraquadra.model.dto.ChatWithMessages
import com.reis.vinicius.vempraquadra.model.entity.Chat
import com.reis.vinicius.vempraquadra.model.entity.Message
import com.reis.vinicius.vempraquadra.model.firestore.ChatFirestore
import com.reis.vinicius.vempraquadra.model.firestore.MatchFirestore
import com.reis.vinicius.vempraquadra.model.firestore.MessageFirestore
import kotlinx.coroutines.tasks.await

class ChatRepository(application: Application): FirestoreRepository<Chat>(application) {
    private val db: FirebaseFirestore = Firebase.firestore
    private val chatCollection = db.collection(Repository.Collections.Chat)
    private val messageCollection = db.collection(Repository.Collections.ChatMessage)
    private val matchRepository = RepositoryFactory(application)
        .create(Repository.Type.Match) as MatchRepository

    override suspend fun getAll(): List<Chat> {
        val matches = db.collection(Repository.Collections.Match)
            .get(getSource()).await()
            .map { it.toObject(MatchFirestore::class.java).toEntity(it.id) }

        return chatCollection.whereIn(ChatFirestore.Fields.matchId, matches.map { it.id })
            .get(getSource()).await()
            .map { query ->
                query.toObject(ChatFirestore::class.java).let { chat ->
                    chat.toEntity(
                        query.id,
                        matches.firstOrNull { it.id == chat.matchId },
                        getLastMessage(query.id)
                    )
                }
            }
    }

    override suspend fun getById(id: String): Chat =
        chatCollection.document(id).get(getSource()).await().let { documentSnapshot ->
            val chatFirestore = documentSnapshot.toObject(ChatFirestore::class.java)
                ?: throw Exception("Failed to parse chat with id $id")
            val match = matchRepository.getById(chatFirestore.matchId ?: "")

            chatFirestore.toEntity(documentSnapshot.id, match, getLastMessage(documentSnapshot.id))
        }

    override suspend fun insert(obj: Chat) {}

    override suspend fun update(obj: Chat) {}

    override suspend fun delete(obj: Chat): Boolean =
        chatCollection.document(obj.id).get(getSource()).await().reference.delete().isSuccessful

    suspend fun getAllByUser(userId: String): List<Chat> {
        val matches = db.collection(Repository.Collections.Match)
            .whereArrayContains(MatchFirestore.Fields.usersIds, userId).get(getSource()).await()
            .map { it.toObject(MatchFirestore::class.java).toEntity(it.id) }

        return chatCollection.whereIn(ChatFirestore.Fields.matchId, matches.map { it.id })
            .get(getSource()).await()
            .map { query ->
                query.toObject(ChatFirestore::class.java).let { chat ->
                    chat.toEntity(
                        query.id,
                        matches.firstOrNull { it.id == chat.matchId },
                        getLastMessage(query.id)
                    )
                }
            }
    }

    suspend fun getWithMessagesById(id: String): ChatWithMessages =
        chatCollection.document(id).get(getSource()).await().let { documentSnapshot ->
            val chatFirestore = documentSnapshot.toObject(ChatFirestore::class.java)
                ?: throw Exception("Failed to parse chat with id $id")
            val match = matchRepository.getById(chatFirestore.matchId ?: "")
            val messages = getMessages(documentSnapshot.id)
            val chat = chatFirestore.toEntity(documentSnapshot.id, match, messages.lastOrNull())

            ChatWithMessages(chat, messages)
        }

    private suspend fun getMessages(chatId: String): List<Message> =
        messageCollection
            .whereEqualTo(MessageFirestore.Fields.chatId, chatId)
            .orderBy(MessageFirestore.Fields.sentIn, Query.Direction.ASCENDING)
            .get(getSource()).await()
            .map { it.toObject(MessageFirestore::class.java).toEntity(it.id) }

    private suspend fun getLastMessage(chatId: String): Message? =
        messageCollection
            .whereEqualTo(MessageFirestore.Fields.chatId, chatId)
            .orderBy(MessageFirestore.Fields.sentIn, Query.Direction.DESCENDING)
            .limit(1).get(getSource()).await()
            .map { it.toObject(MessageFirestore::class.java).toEntity(it.id) }.firstOrNull()

}