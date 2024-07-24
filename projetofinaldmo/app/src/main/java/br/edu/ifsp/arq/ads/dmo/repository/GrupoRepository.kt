package br.edu.ifsp.arq.ads.dmo.repository

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.edu.ifsp.arq.ads.dmo.model.Grupo
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore


class GrupoRepository (application: Application) {

    private val firestore = FirebaseFirestore.getInstance()

    fun insert(grupo: Grupo?) {
        firestore.collection("grupo").add(grupo!!)
            .addOnSuccessListener { unused: DocumentReference? ->
                Log.d(
                    this.toString(),
                    "Grupo cadastrado com sucesso."
                )
            }
    }

    fun update(grupo: Grupo) {
        firestore.collection("grupo").document(grupo.id).set(grupo)
            .addOnSuccessListener { unused: Void? ->
                Log.d(
                    this.toString(),
                    "Grupo atualizado com sucesso."
                )
            }
    }

    fun delete(grupo: Grupo) {
        firestore.collection("grupo").document(grupo.id).delete()
            .addOnSuccessListener { unused: Void? ->
                Log.d(
                    this.toString(),
                    "Grupo atualizado com sucesso."
                )
            }
    }

    fun getAllGroups(userId: String?): LiveData<List<Grupo>> {
        val liveData = MutableLiveData<List<Grupo>>()
        val groups: MutableList<Grupo> = ArrayList()

        if (userId == null) {
            liveData.value = groups
            return liveData
        }

        firestore.collection("groups").whereArrayContains("memberIds", userId).get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (doc in task.result) {
                        val group = doc.toObject(Grupo::class.java)
                        group.id = doc.id
                        groups.add(group)
                    }
                }
                liveData.setValue(groups)
            }

        return liveData
    }

}