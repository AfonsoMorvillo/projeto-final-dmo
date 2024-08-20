    package br.edu.ifsp.arq.ads.dmo

    import android.app.Activity
    import android.content.Intent
    import android.os.Bundle
    import android.widget.EditText
    import com.google.android.material.button.MaterialButton
    import br.edu.ifsp.arq.ads.dmo.viewmodel.GrupoViewModel
    import androidx.activity.viewModels
    import androidx.appcompat.app.AppCompatActivity
    import androidx.lifecycle.Observer
    import br.edu.ifsp.arq.ads.dmo.model.User
    import br.edu.ifsp.arq.ads.dmo.viewmodel.UserViewModel

    class JuntarGrupoActivity : AppCompatActivity() {

        private val grupoViewModel by viewModels<GrupoViewModel>()
        private val userViewModel by viewModels<UserViewModel>()

        lateinit var edtGrupo: EditText
        lateinit var btnJuntarse: MaterialButton
        lateinit var user: User

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_juntar_grupo)
            loadUserLogged()

            edtGrupo = findViewById(R.id.edtGrupo)
            btnJuntarse = findViewById(R.id.btn_juntar)

            btnJuntarse.setOnClickListener {
                if (user != null) {
                    grupoViewModel.juntarSeGrupo(edtGrupo.text.toString(), user.id).observe(this, Observer { success ->
                        if (success) {
                            setResult(Activity.RESULT_OK)
                        } else {
                            setResult(Activity.RESULT_CANCELED)
                        }
                        finish()
                    })
                }
            }
        }

        private fun loadUserLogged() {
            userViewModel.isLogged().observe(this,  Observer {
                if (it == null) {
                    startActivity(
                        Intent(
                            this,
                            LoginActivity::class.java
                        )
                    )
                    finish()
                } else {
                    this@JuntarGrupoActivity.user = it
                }
            })
        }
    }
