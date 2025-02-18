package com.example.testemoble

import android.content.Context
import android.os.Bundle// Contém os dados da instância anterior da Activity.
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity // Classe base para atividades compatíveis com versões antigas do Android.
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {


    private lateinit var textViewTempo :TextView
    private lateinit var botaoIniciartext :Button


   private  var descansando = false
   private  var pomodo_concluido = 0
    private var job: Job? = null

    private fun vibrar(context: Context, tempo: Long) {
        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

        // Verifica se a versão do Android é igual ou superior ao Android Oreo
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
// Para versões do Android Oreo e superiores
            val vibrationEffect = VibrationEffect.createOneShot(tempo,
                VibrationEffect.DEFAULT_AMPLITUDE)
            vibrator.vibrate(vibrationEffect)
            Log.d("VIBRACAO", "Usando VibrationEffect (API 26+)")
        } else {
// Para versões anteriores ao Android Oreo
            vibrator.vibrate(tempo)
            Log.d("VIBRACAO", "Usando vibrate() antigo (API < 26)")
        }


    }
    fun exemploVibracao() {
        vibrar(this, 500)

    }


    fun IniciarPomodoro(){
        var duracao = 0
        botaoIniciartext.isEnabled = false // pra ele nao ficar clicando toda hora
        job?.cancel()
        job = CoroutineScope(Dispatchers.Main).launch {


            while(duracao<1500){
                duracao++
                val minutos =  duracao / 60
                val segundos =  duracao % 60
                textViewTempo.text= "Tempo : $minutos:$segundos"
                delay(1000)

            }
            pomodo_concluido++
            exemploVibracao()

            if(pomodo_concluido % 4 == 0 ) {
                IniciarDescansoLongo()
            } else{
                IniciarDescansoCurto()
            }
        }
    }

    fun IniciarDescansoCurto(){
        descansando=true
        var duracaoCurta = 0
        job?.cancel()
        job = CoroutineScope(Dispatchers.Main).launch {
            while(duracaoCurta<300){
                duracaoCurta++
                val minutos = (300 - duracaoCurta) / 60
                val segundos = (300 - duracaoCurta) % 60
                textViewTempo.text = "Tempo: %02d:%02d".format(minutos, segundos) +" restantes"
                delay(1000)

            }
            exemploVibracao()
            botaoIniciartext.isEnabled= true // deixar o botao clicavel
            textViewTempo.text="Pronto para outro pomodoro"
            descansando=false

        }

    }

    fun IniciarDescansoLongo(){
        descansando=true
        var duracaoLonga = 0
        job?.cancel()
        job = CoroutineScope(Dispatchers.Main).launch {
            while(duracaoLonga<900){
                duracaoLonga++
                val minutos = (300 - duracaoLonga) / 60
                val segundos = (300 - duracaoLonga) % 60
                textViewTempo.text = "Tempo: %02d:%02d".format(minutos, segundos) +" restantes"
                delay(1000)

            }
            exemploVibracao()
            botaoIniciartext.isEnabled= true // deixar o botao clicavel
            textViewTempo.text="Pronto para outro pomodoro"
            descansando=false
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        botaoIniciartext = findViewById<Button>(R.id.botaoIniciartext)
        textViewTempo = findViewById<TextView>(R.id.textViewTempo)





        botaoIniciartext.setOnClickListener {
            if(!descansando) {

                IniciarPomodoro()
            }
        }










    }
}