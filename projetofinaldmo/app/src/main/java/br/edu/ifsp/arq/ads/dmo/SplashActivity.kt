package br.edu.ifsp.arq.ads.dmo

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val intent = Intent(this, TelaInicialActivity::class.java)
        val handler = Handler()
        handler.postDelayed(Runnable {
            startActivity(intent)
            finish()
        }, 3000)
    }
}