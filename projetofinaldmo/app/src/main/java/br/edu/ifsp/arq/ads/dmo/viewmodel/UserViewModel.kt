package br.edu.ifsp.arq.ads.dmo.viewmodel

import android.app.Application
import android.preference.PreferenceManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.edu.ifsp.arq.ads.dmo.model.User
import br.edu.ifsp.arq.ads.dmo.repository.UsersRepository

class UserViewModel (application: Application) : AndroidViewModel(application) {

    private val usersRepository = UsersRepository(getApplication())

    fun createUser(user: User) = usersRepository.insert(user)

    fun updateUser(user: User) = usersRepository.update(user)

    fun login(email: String, password: String) : MutableLiveData<User> {
        return MutableLiveData(
            usersRepository.login(email, password).also { user ->
                PreferenceManager.getDefaultSharedPreferences(getApplication()).let {
                    if (user != null)
                        it.edit().putString(USER_ID, user.id).apply()
                }
            }
        )
    }

    fun logout() = PreferenceManager.getDefaultSharedPreferences(getApplication()).let {
        it.edit().remove(USER_ID).apply()
    }

    fun isLogged(): LiveData<User> = PreferenceManager.getDefaultSharedPreferences(getApplication()).let {
        return usersRepository.loadUserById(it.getString(USER_ID, ""))
    }

    companion object {
        val USER_ID = "USER_ID"
    }
}
