package br.edu.ifsp.arq.ads.dmo.repository

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.edu.ifsp.arq.ads.dmo.model.Postagem
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

class PostagemRepository (application: Application) {

    private val firestore = FirebaseFirestore.getInstance()

    fun insert(postagem: Postagem?) {
        firestore.collection("postagem").add(postagem!!)
            .addOnSuccessListener { unused: DocumentReference? ->
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

        firestore.collection("postagem").whereEqualTo("grupoId", groupoId).get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (doc in task.result) {
                        val post = doc.toObject(Postagem::class.java)
                        post.id = doc.id
                        postagens.add(post)
                    }
                }
                liveData.setValue(postagens)
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
                        liveData.value = post
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