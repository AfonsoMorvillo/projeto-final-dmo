package br.edu.ifsp.arq.ads.dmo.viewmodel


import android.app.Application
import android.net.Uri
import android.preference.PreferenceManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.edu.ifsp.arq.ads.dmo.model.User
import br.edu.ifsp.arq.ads.dmo.repository.UserRepository

class UserViewModel (application: Application) : AndroidViewModel(application) {

    private val usersRepository = UserRepository(getApplication())

    fun createUser(user: User): LiveData<Boolean> {
        val liveData = MutableLiveData<Boolean>()
        usersRepository.createUser(user) {
            liveData.value = true
        }
        return liveData
    }

    fun updateUser(user: User) = usersRepository.update(user)

    fun login(email: String, password: String) : LiveData<User> = usersRepository.login(email, password)

    fun logout() = PreferenceManager.getDefaultSharedPreferences(getApplication()).let {
        it.edit().remove(USER_ID).apply()
    }

    fun isLogged(): LiveData<User> = PreferenceManager.getDefaultSharedPreferences(getApplication()).let {
        val id = it.getString(USER_ID, null)

        if(id.isNullOrEmpty())
            return MutableLiveData(null)

        return usersRepository.load(id)
    }

    fun resetPassword(email: String) = usersRepository.resetPassword(email)

    fun uploadUserImage(userId: String, photoUri: Uri) = usersRepository.uploadUserImage(userId, photoUri)

    companion object {
        val USER_ID = "USER_ID"
    }
}
