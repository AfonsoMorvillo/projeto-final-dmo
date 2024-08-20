package br.edu.ifsp.arq.ads.dmo.repository

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.edu.ifsp.arq.ads.dmo.model.Grupo
import br.edu.ifsp.arq.ads.dmo.model.Postagem
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Collections

class PostagemRepository (application: Application) {

    private val firestore = FirebaseFirestore.getInstance()

    fun insert(postagem: Postagem?) {
        firestore.collection("postagem").add(postagem!!)
            .addOnSuccessListener { unused: DocumentReference? ->

                val grupoRef = firestore.collection("grupo").document(postagem.grupoId)

                firestore.runTransaction { transaction ->
                    val grupoSnapshot = transaction.get(grupoRef)
                    val grupo = grupoSnapshot.toObject(Grupo::class.java)

                    if (grupo != null) {
                        grupo.quantidadeAtual = grupo.quantidadeAtual?.plus(postagem.quantidade!!)
                        transaction.set(grupoRef, grupo)
                    }
                }

                Log.d(
                    this.toString(),
                    "Postagem publicada com sucesso."
                )
            }


    }

    fun update(postagem: Postagem) {
        firestore.collection("postagem").document(postagem.id).set(postagem)
            .addOnSuccessListener { unused: Void? ->
                Log.d(
                    this.toString(),
                    "postagem atualizada com sucesso."
                )
            }
    }

    fun delete(postagem: Postagem) {
        firestore.collection("postagem").document(postagem.id).delete()
            .addOnSuccessListener { unused: Void? ->
                Log.d(
                    this.toString(),
                    "Postagem excluida."
                )
            }
    }

    fun getAllPostagens(groupoId: String?): LiveData<List<Postagem>> {
        val liveData = MutableLiveData<List<Postagem>>()
        val postagens: MutableList<Postagem> = ArrayList()

        if (groupoId == null) {
            liveData.value = postagens
            return liveData
        }

        // Primeiro, obtenha todas as postagens para o grupo especificado
        firestore.collection("postagem")
            .whereEqualTo("grupoId", groupoId)
            .get()
            .addOnCompleteListener { postTask ->
                if (postTask.isSuccessful) {
                    val posts = postTask.result
                    if (posts != null) {
                        val userIds = mutableSetOf<String>()

                        for (doc in posts) {
                            val post = doc.toObject(Postagem::class.java)
                            post.id = doc.id
                            postagens.add(post)
                            userIds.add(post.userId)
                        }

                        if (userIds.isNotEmpty()){



                        firestore.collection("user")
                            .whereIn(FieldPath.documentId(), userIds.toList())
                            .get()
                            .addOnCompleteListener { userTask ->
                                if (userTask.isSuccessful) {
                                    val userDocuments = userTask.result
                                    val userMap = mutableMapOf<String, String>()

                                    for (userDoc in userDocuments!!) {
                                        val userName = userDoc.getString("name")
                                        val userId = userDoc.id
                                        userMap[userId] = userName ?: ""
                                    }

                                    // Associe os nomes dos usuários às postagens
                                    for (post in postagens) {
                                        post.nomeUsuario = userMap[post.userId] ?: ""
                                    }

                                    // Ordene as postagens por data
                                    postagens.sortByDescending { it.data }
                                    liveData.value = postagens
                                } else {
                                    // Caso a busca dos usuários falhe, ainda envie as postagens
                                    liveData.value = postagens
                                }
                            }
                        }
                    }
                } else {
                    liveData.value = postagens
                }
            }

        return liveData
    }


    fun getPost(postId: String?): LiveData<Postagem> {
        val liveData = MutableLiveData<Postagem>()

        if (postId.isNullOrEmpty()) {
            liveData.value = null
            return liveData
        }

        firestore.collection("postagem").document(postId).get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val document = task.result
                    if (document != null && document.exists()) {
                        val post = document.toObject(Postagem::class.java)
                        post?.id = document.id


                        firestore.collection("user").document(post?.userId ?: "")
                            .get()
                            .addOnCompleteListener { userTask ->
                                if (userTask.isSuccessful) {
                                    val user = userTask.result
                                    if (user != null && user.exists()) {
                                        val userName = user.getString("name")
                                        post?.nomeUsuario = userName ?: ""
                                        liveData.value = post
                                    } else {
                                        liveData.value = post
                                    }
                                } else {
                                    liveData.value = post
                                }
                            }
                    } else {
                        liveData.value = null
                    }
                } else {
                    liveData.value = null
                }
            }

        return liveData
    }




}