package raya.raya.raya

import java.util.*

class Partida(dificultad: Int) {
    //CAMPOS DE CLASE
    //--------------------------------------------------------------------------------------------
//Con esta instrucción almacenamos la dificultad del juego
    val dificultad:Int = dificultad
    var jugador=1
    //Este es el array para las casillas. Si no están ocupadas seguirán en 0, si lo están se pondrá 1
    private var casillas =arrayOf(0,0,0,0,0,0,0,0,0)
    //Array para tener en cuenta las combinaciones ganadoras
    val COMBINACIONES = arrayOf(
            arrayOf(0, 1, 2),
            arrayOf(3, 4, 5),
            arrayOf(6, 7, 8),
            arrayOf(0, 3, 6),
            arrayOf(1, 4, 7),
            arrayOf(2, 5, 8),
            arrayOf(0, 4, 8),
            arrayOf(2, 4, 6))
    /*
    SABER SI HAY PROYECTO DE TRES EN RAYA
    ----------------------------------------------------------------------------------------------
     */
    fun dosEnRaya (jugador_en_turno:Int):Int{
    var casilla:Int=-1
        //Para saber cuantas fichas seguidas lleva un jugador en una de las combinaciones
    var cuantas_lleva:Int=0
        for(i in COMBINACIONES){
            for(pos in i){
                /*
                Esta combinación hace que si la casilla está marcada por el jugador que está jugando
                en ese momento, se sume la variable cuantas lleva, para ver las posibles victorias*/
                if(casillas[pos]==jugador_en_turno){
                    cuantas_lleva++
                }
                //Esta condición evalua si la casilla está vacía y la almacena en la variable "casilla"
                if(casillas[pos]==0){
                    casilla=pos
                }
            }//Fin del segundo bucle
            if(cuantas_lleva==2 && casilla!=-1){
                return casilla
            }
            //Se resetean las variables para iniciar nuevamente la comprovación con la siguiente combinación
            casilla=-1
            cuantas_lleva=0
        }//Fin del primer bucle
        return -1
    }//Fin de método dosEnRaya

    /*
    METODO INTELIGENCIA ARTIFICIAL PARA GENERAR CASILLA ALEATORIA PARA DIBUJAR
    ----------------------------------------------------------------------------------------------
     */
    public fun ia():Int{
        var casilla_azar:Int
        casilla_azar=dosEnRaya(2)
        /*PARA EL NIVEL FÁCIL
        Si existe una casilla marcada como clave para la máquina se pasará esa casilla,
         no un número al azar*/
        if(casilla_azar!=-1){
            return casilla_azar
        }
        /*
        PARA NIVEL NORMAL
        Se pasa una 1 por parámetro al método dosEnRaya para evitar que la máquina, aparte de querer
        hacer tres en raya, se preocupe de evitar que yo gane
         */
        if(dificultad>0){
            casilla_azar=dosEnRaya(1)
            if(casilla_azar!=-1){
                return casilla_azar
            }
        }
        /*
        PARA NIVEL DIFÍCIL
        La máquina marcará las esquinas, ya que son claves para ganar en este juego
 */
        if(dificultad==2){
            //Esquina arriba izquierda
            if(casillas[0]==0){
                return 0
            }
            //Esquina arriba derecha
            if(casillas[2]==0){
                return 2
            }
            //Esquina abajo izquierda
            if(casillas[6]==0){
                return 6
            }
            //Esquina abajo derecha
            if(casillas[8]==0){
                return 8
            }
        }//Fin del if principal

        //Número generado de 0 a 8
        casilla_azar = Random().nextInt(9)
        return casilla_azar
    }
    /*
    CAMBIAR DE TURNO
    -----------------------------------------------------------------------------------------------
     */
    fun turno():Int{
        var empate=true
        var ult_movimiento = true
        //Con este bucle podemos recorrer las combinaciones posibles y comprobar si hay ganador
        //var total=0
        for(i in COMBINACIONES){
            for(pos in i){
                    if(casillas[pos]==0){
                    empate=false
                }//Fin if
               // println("Valor en posicion $total = "+casillas[pos])
                if(casillas[pos]!=jugador){
                    ult_movimiento=false
                }
            }//Fin segundo for
            //println("-----------------------------------------------------------")
           // total++
            if(ult_movimiento){
                return jugador
            }
            ult_movimiento=true
        }//Fin primer for

        //Si no se encuentra ningún 0 en el array es que estarán todas las casillas ocupadas
        if(empate){
            return 3
        }
        jugador++
        if(jugador>2){
            jugador=1
        }


        //Se retorna 0 se no ocurre nada
        return 0
    }//Fin del método

    /*
    COMPROBAR CASILLA VACIA
    -----------------------------------------------------------------------------------------------
     */
    public fun comprueba_casilla(casilla:Int):Boolean{
        if(casillas[casilla]!=0){
            return false
        }else{
            casillas[casilla]=jugador
        }
        return true
    }




}//Fin de clase