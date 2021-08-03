package raya.raya.raya

import android.app.Activity
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.RadioGroup
import android.widget.Toast
import com.example.myapplication.R
import com.google.android.gms.ads.*


class MainActivity : Activity() {
    //Variables para el banner
    private val TAG = "MainActivity"
    private lateinit var mAdview: AdView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Esto es para meter el banner en el layout principal
        MobileAds.initialize(this)
        bannerAd()
    }
/*
INCIO MÉTODO BOTONES
---------------------------------------------------------------------------------------
 */
    fun aJugar(vista: View){
    //Para reproducir el sonido de la casilla 1 o dos jugadores
     var mediaPlayer:MediaPlayer = MediaPlayer.create(this, R.raw.empieza_juego);
    mediaPlayer.start()


        var imagen: ImageView
        //Con este bucle ponemos todas las casillas en blanco
        for(identificadores in CASILLAS ){
            imagen = findViewById(identificadores)
            imagen.setImageResource(R.drawable.enblanco)
        }

        jugadores=1
        //Si la vista recibe el Boton 2, entonces se ejecutará el juego contra una persona
        if(vista.getId()== R.id.dos_jugadores){
            jugadores=2
        }


        /*Lo que hago con estas dos variables siguientes y el método es obtener el botón pulsado en
        el radio y establecer la dificultad
         */
        var  configDificultad: RadioGroup = findViewById(R.id.configD)
        var id_seleccionado:Int= configDificultad.checkedRadioButtonId
        var dificultad =0
        if(id_seleccionado== R.id.normal){
            dificultad=1
        }else if(id_seleccionado== R.id.imposible){
            dificultad=2
        }
        //En la clase partida se controlará el juego que se establece con los parámetros elegidos en este método
        partida = Partida(dificultad)
        //Con el siguiente código voy a inhabilitar los botones
        var boton:Button=findViewById(R.id.unJugador)
        boton.isEnabled=false
        var radioGroup:RadioGroup=findViewById(R.id.configD)
        radioGroup.alpha=0F
        var boton2:Button=findViewById(R.id.dos_jugadores)
        boton2.isEnabled=false



    }//Fin del método
/*
INICIO MÉTODO CASILLAS
--------------------------------------------------------------------------------------
 */
    fun toque(mi_casilla: View){
    if(partida==null){
        return
    }
    /*Aquí declaro una variable para almacenar la posición del array que coincide con la vista
      que ejecuta el método
     */
        var casilla = 0
        for(id_casillas in CASILLAS){
            if(id_casillas==mi_casilla.id) {
                break
            }
            casilla++
        }//Fin de bucle for

    /*Este Toast es para el mensaje de alerta
    val mensaje: Toast = Toast.makeText(this,"Has pulsado la casilla $casilla",Toast.LENGTH_LONG)
    mensaje.setGravity(Gravity.CENTER,0,0)
    mensaje.show()
    */
    if(partida!!.comprueba_casilla(casilla)==false){
        return
    }
    marca(casilla)
    //Para crear el sonido cuando el humano pone una ficha
    var sonido_fichas:MediaPlayer= MediaPlayer.create(this, R.raw.poner_ficha)
    sonido_fichas.start()
    var resultado:Int=partida!!.turno()
    if(resultado>0){
        termina(resultado)
        return
    }
    //Este if permite que si elegimos dos jugadores, solo se ejecute la parte en la que elige la persona
    if (jugadores==1){
    //Ahora la máquina va a elegir dónde poner la cruz
    casilla= partida!!.ia()//La doble admiración es para decir que no será nulo
    //Para evitar que se pueda pulsar donde la máquina ha elegido
    while (partida!!.comprueba_casilla(casilla)!=true){
        casilla = partida!!.ia()
    }

    marca(casilla)
    resultado=partida!!.turno()
    if(resultado>0){
        termina(resultado)
    }
    }
    }//Fin de método toque

    /*
    DECLARAR GANADOR Y CERRAR JUEGO
    ----------------------------------------------------------------------------------------------
     */
    private fun termina(resultado: Int){
  //Este if es para poner el mensaje por pantalla de ganar, empatar o perder
        if(resultado==1){
            var ganador = MediaPlayer.create(this, R.raw.ganador)
            ganador.start()
            val toast: Toast = Toast.makeText(
                this,
                getText(R.string.circulos_ganan),
                Toast.LENGTH_LONG
            )
            toast.setGravity(Gravity.CENTER, 0, 0)
            toast.show()
        }else if(resultado==2){
            var perdedor = MediaPlayer.create(this, R.raw.perdedor)
            perdedor.start()
            val toast: Toast = Toast.makeText(
                this,
                getText(R.string.aspas_ganan),
                Toast.LENGTH_LONG
            )
            toast.setGravity(Gravity.CENTER, 0, 0)
            toast.show()
        }else{
            var empate = MediaPlayer.create(this, R.raw.empate)
            empate.start()
            val toast: Toast = Toast.makeText(this, getText(R.string.empate), Toast.LENGTH_LONG)
            toast.setGravity(Gravity.CENTER, 0, 0)
            toast.show()
        }//Fin el if principal

        //Para cerrar el juego se pone en null
        partida=null
        //Para volver a habilitar los botones
        var boton:Button=findViewById(R.id.unJugador)
        boton.isEnabled=true
        var radioGroup:RadioGroup=findViewById(R.id.configD)
        radioGroup.alpha=1F
        var boton2:Button=findViewById(R.id.dos_jugadores)
        boton2.isEnabled=true
    }//Fin del método termina

    /*
        METODO PARA MARCAR CASILLAS
        ------------------------------------------------------------------------------------------
     */
    private fun marca(casilla: Int){
        //Mediante la variable imágen estoy obteniendo el ID seleccionado
        var imagen:ImageView = findViewById(CASILLAS[casilla])
        /*Ahora cambio la imágen de la casilla por un aspa o un círculo
        Dependiendo de si el jugador es 1 o 2, se le asignará un círculo o un aspa
         */
        if(partida?.jugador==1){
            imagen.setImageResource(R.drawable.circulo)
        }else{
            imagen.setImageResource(R.drawable.aspa)
        }
    }
//CAMPO DE CLASE
//-------------------------------------------------------------------------------------------------
    private var partida: Partida?=null
    private var jugadores: Int=0
    //Iniciamos el array casillas que identifica cada casilla y la almacena en el array.
    private var CASILLAS= arrayOf(
            R.id.a1, R.id.a2, R.id.a3,
            R.id.b1, R.id.b2, R.id.b3,
            R.id.c1, R.id.c2, R.id.c3
    )




/*
    METODO PARA BANNER
    ----------------------------------------------------------------------------------------------------
*/
    private fun bannerAd(){
        mAdview = findViewById(R.id.adView)
        val adRequest= AdRequest.Builder().build()
        mAdview.loadAd(adRequest)
        mAdview.adListener = object: AdListener(){
            override fun onAdLoaded() {
                Log.d(TAG, "Ad loaded")
            }

            override fun onAdFailedToLoad(p0: LoadAdError?){
                Log.d(TAG, "Ad failed to load")
            }

            override fun onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
                Log.d(TAG,"Ad opened")
            }
            override fun onAdClicked() {
                // Code to be executed when the user clicks on an ad.
                Log.d(TAG,"Ad clicked")
            }
            override fun onAdLeftApplication() {
                // Code to be executed when the user has left the app.
                Log.d(TAG,"User left app on ad")
            }

            override fun onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
                Log.d(TAG,"Ad closed")
            }


        }
    }



}//Fin de clase