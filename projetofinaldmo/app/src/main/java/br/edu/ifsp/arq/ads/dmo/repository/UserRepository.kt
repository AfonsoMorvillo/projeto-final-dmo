package br.edu.ifsp.arq.ads.dmo.repository

import android.app.Application
import android.net.Uri
import android.preference.PreferenceManager
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.edu.ifsp.arq.ads.dmo.model.User
import br.edu.ifsp.arq.ads.dmo.viewmodel.UserViewModel
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.app
import com.google.firebase.storage.ktx.storage
import org.json.JSONObject

class UserRepository(application: Application) {

    private val firestore = FirebaseFirestore.getInstance()

    private val queue = Volley.newRequestQueue(application)

    private val preference = PreferenceManager.getDefaultSharedPreferences(application)

    private val storage = Firebase.storage(Firebase.app)

    fun login(email: String, password: String): LiveData<User> {
        println("executando login")
        val liveData = MutableLiveData<User>(null)

        val params = JSONObject().also {
            it.put("email", email)
            it.put("password", password)
            it.put("returnSecureToken", true)
        }

        val request = JsonObjectRequest(
            Request.Method.POST, BASE_URL + SIGNIN + KEY, params, Response.Listener { response ->
                val localId = response.getString("localId")
                val idToken = response.getString("idToken")

                firestore.collection("user")
                    .document(localId).get().addOnSuccessListener {
                        val user = it.toObject(User::class.java)
                        user?.id = localId
                        user?.password = idToken

                        liveData.value = user

                        println("id do login")
                        println(localId)

                        preference.edit().putString(UserViewModel.USER_ID, localId).apply()

                        firestore.collection("user")
                            .document(localId).set(user!!)
                    }
            }, Response.ErrorListener { error ->
                val user = User()
                user?.id = "ERRO"

                liveData.value = user
                Log.e(this.toString(), error.message ?: "Error")
            }
        )

        queue.add(request)

        return liveData
    }

    fun createUser(user: User, onSuccess: () -> Unit) {

        val params = JSONObject().also {
            it.put("email", user.email)
            it.put("password", user.password)
            it.put("returnSecureToken", true)
        }

        val request = JsonObjectRequest(Request.Method.POST,
            BASE_URL + SIGNUP + KEY,
            params,
            Response.Listener { response ->
                user.id = response.getString("localId")
                user.password = response.getString("idToken")
                println(user.id)

                firestore.collection("user")
                    .document(user.id).set(user).addOnSuccessListener {
                        Log.d(this.toString(), "Usuário ${user.email} cadastrado com sucesso.")
                        preference.edit().putString(UserViewModel.USER_ID, user.id).apply()
                        onSuccess() // Notifica que a criação foi bem-sucedida
                    }
            },
            Response.ErrorListener { error ->
                Log.e(this.toString(), error.message ?: "Error")
            }
        )

        queue.add(request)
    }


    fun load(userId: String): LiveData<User> {
        val liveData = MutableLiveData<User>()

        val userRef = firestore.collection("user").document(userId)

        userRef.get().addOnSuccessListener {
            val user = it.toObject(User::class.java)
            user?.id = it.id

            liveData.value = user
        }

        return liveData
    }


    fun update(user: User): Boolean {

        var updated = false

        val userRef = firestore.collection("user").document(user.id)

        userRef.set(user).addOnSuccessListener { updated = true }

        return updated
    }

    fun resetPassword(email: String) {

        val params = JSONObject().also {
            it.put("email", email)
            it.put("requestType", "PASSWORD_RESET")
        }

        val request = JsonObjectRequest(Request.Method.POST,
            BASE_URL + PASSWORD_RESET + KEY,
            params,
            Response.Listener { response ->
                Log.d(this.toString(), response.keys().toString())
            },
            Response.ErrorListener { error ->
                Log.e(this.toString(), error.message ?: "Error")
            }
        )

        queue.add(request)

    }

    fun uploadUserImage(userId: String, imageUri: Uri): LiveData<String> {

        val liveData = MutableLiveData<String>()

        val imageRef = storage.reference.child("users/${userId}.jpg")

        imageRef.putFile(imageUri).addOnSuccessListener { taskSnapshot ->
            imageRef.downloadUrl.addOnSuccessListener {
                liveData.value = it.toString();
            }
        }

        return liveData
    }

    companion object {

        const val BASE_URL = "https://identitytoolkit.googleapis.com/v1/"

        const val SIGNUP = "accounts:signUp"

        const val SIGNIN = "accounts:signInWithPassword"

        const val PASSWORD_RESET = "accounts:sendOobCode"

        const val KEY =
            "?key=AIzaSyBIaCZuNhqNI9mXH91fLtgy5800hdjTBzs" // pegar nas Configurações do Projeto no Firebase - valor parecido com AIzaSyBxFjit4FD8NN5Mx8hTFQQxeVA1Pv2OUag

    }


}