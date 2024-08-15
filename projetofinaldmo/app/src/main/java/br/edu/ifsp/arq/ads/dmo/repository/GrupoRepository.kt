package br.edu.ifsp.arq.ads.dmo.repository

import android.app.Application
import android.net.Uri
import android.preference.PreferenceManager
import android.util.Log
import android.widget.ImageView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.edu.ifsp.arq.ads.dmo.R
import br.edu.ifsp.arq.ads.dmo.model.Grupo
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.app
import com.google.firebase.storage.ktx.storage


class GrupoRepository (application: Application) {

    private val firestore = FirebaseFirestore.getInstance()

    private val glide = Glide.with(application)

    private val storage = Firebase.storage(Firebase.app)

    private val preference = PreferenceManager.getDefaultSharedPreferences(application)


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

        firestore.collection("grupo").whereArrayContains("memberIds", userId).get()
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

    fun uploadGrupoImagem(grupoId: String, imageUri: Uri) : LiveData<String> {

        val liveData = MutableLiveData<String>()

        val imageRef = storage.reference.child("grupos/${grupoId}.jpg")

        imageRef.putFile(imageUri).addOnSuccessListener { taskSnapshot->
            imageRef.downloadUrl.addOnSuccessListener {
                liveData.value =  it.toString();
            }
        }

        return liveData
    }

    fun loadGrupo(grupoId: String, imageView: ImageView) = storage.reference.child("grupos/$grupoId/profile.jpg")
        .downloadUrl.addOnSuccessListener {
            glide.load(it)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(R.drawable.menu_vazio)
                .placeholder(R.drawable.menu_vazio)
                .into(imageView)
        }


    fun getGrupo(grupoId: String): MutableLiveData<Grupo> {
        val liveData = MutableLiveData<Grupo>()

        if (grupoId.isNullOrEmpty()) {
            liveData.value = null
            return liveData
        }

        firestore.collection("grupo").document(grupoId).get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val grupo = document.toObject(Grupo::class.java)
                    grupo?.id = document.id
                    liveData.value = grupo
                } else {
                    liveData.value = null
                }
            }
            .addOnFailureListener { exception ->
                liveData.value = null
            }

        return liveData
    }

}
