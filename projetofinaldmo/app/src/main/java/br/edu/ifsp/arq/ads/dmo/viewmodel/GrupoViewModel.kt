package br.edu.ifsp.arq.ads.dmo.viewmodel;

import android.app.Application
import android.net.Uri
import android.widget.ImageView
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import br.edu.ifsp.arq.ads.dmo.model.Grupo
import br.edu.ifsp.arq.ads.dmo.repository.GrupoRepository

class GrupoViewModel  (application: Application) : AndroidViewModel(application) {

    private val grupoReposiory = GrupoRepository(getApplication())

    fun createGrupo(grupo: Grupo) = grupoReposiory.insert(grupo)

    fun updateGrupo(grupo: Grupo) = grupoReposiory.update(grupo)

    fun getAllGroups(userId: String?): LiveData<List<Grupo>> {
        return grupoReposiory.getAllGroups(userId)
    }

    fun deleteGrupo(grupo: Grupo) {
        grupoReposiory.delete(grupo)
    }

    fun uploadGrupoImage(grupoId: String, photoUri: Uri) = grupoReposiory.uploadGrupoImagem(grupoId, photoUri)

    fun loadGrupoImage(grupoId: String, imageView: ImageView) = grupoReposiory.loadGrupo(grupoId, imageView)

    fun getGrupo(grupoId: String) = grupoReposiory.getGrupo(grupoId)

}
