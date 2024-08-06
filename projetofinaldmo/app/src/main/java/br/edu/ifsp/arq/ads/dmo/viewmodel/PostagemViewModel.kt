package br.edu.ifsp.arq.ads.dmo.viewmodel;

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import br.edu.ifsp.arq.ads.dmo.model.Grupo
import br.edu.ifsp.arq.ads.dmo.model.Postagem
import br.edu.ifsp.arq.ads.dmo.repository.GrupoRepository
import br.edu.ifsp.arq.ads.dmo.repository.PostagemRepository

class PostagemViewModel  (application: Application) : AndroidViewModel(application) {

    private val postagemRepository = PostagemRepository(getApplication())

    fun createPostagem(postagem: Postagem) = postagemRepository.insert(postagem)

    fun updatePostagem(postagem: Postagem) = postagemRepository.update(postagem)

    fun getAllPosts(groupId: String?): LiveData<List<Postagem>> {
        return postagemRepository.getAllPostagens(groupId)
    }

    fun deletePostagem(postagem: Postagem) {
        postagemRepository.delete(postagem)
    }

}
