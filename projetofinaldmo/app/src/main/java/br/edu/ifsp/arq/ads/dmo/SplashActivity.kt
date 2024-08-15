package br.edu.ifsp.arq.ads.dmo

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import br.edu.ifsp.arq.ads.dmo.viewmodel.UserViewModel
import androidx.activity.viewModels
import androidx.lifecycle.Observer

class SplashActivity : AppCompatActivity() {

    private val userViewModel by viewModels<UserViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        var intent: Intent? = null

        userViewModel.isLogged().observe(this, Observer {
            if (it == null) {
                 intent = Intent(this, TelaInicialActivity::class.java)

            }else{
                intent = Intent(this, ListaGruposActivity::class.java)
            }

            val handler = Handler()
            handler.postDelayed(Runnable {
                startActivity(intent)
                finish()
            }, 3000)
        })

    }
}