package com.example.practica5.model.temp

import android.app.Application
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.practica5.model.Tarea
import kotlin.random.Random

object ModelTempTareas {
    //lista de tareas
    private val tareas = ArrayList<Tarea>()
    //LiveData para observar en la vista los cambios en la lista
    private val tareasLiveData = MutableLiveData<ArrayList<Tarea>>(tareas)
    //el context que suele ser necesario en acceso a datos
    private lateinit var application: Application

    operator fun invoke(context: Context){
        this.application= context.applicationContext as Application
        iniciaPruebaTareas()
    }

    fun getAllTareas(): LiveData<ArrayList<Tarea>> {
        tareasLiveData.value= tareas
        return tareasLiveData
    }

    fun addTarea(tarea: Tarea) {
        val pos = tareas.indexOf(tarea)
        if (pos < 0) {//si no existe
            tareas.add(tarea)
        } else {
            //si existe se sustituye
            tareas.set(pos, tarea)
        }
        //actualiza el LiveData
        tareasLiveData.value = tareas
    }
    fun iniciaPruebaTareas() {
        val tecnicos = listOf(
            "Pepe Gotero",
            "Sacarino Pómez",
            "Mortadelo Fernández",
            "Filemón López",
            "Zipi Climent",
            "Zape Gómez"
        )
        lateinit var tarea: Tarea
        (1..10).forEach {
            tarea = Tarea(
                (0..4).random(),
                (0..2).random(),
                Random.nextBoolean(),
                (0..2).random(),
                (0..30).random(),
                (0..5).random().toFloat(),
                tecnicos.random(),
                "tarea $it realizada por el técnico \n"
            )
            tareas.add(tarea)
        }
        //actualizamos el LiveData
        tareasLiveData.value = tareas
    }

    suspend fun delTarea(tarea: Tarea) {
        //Thread.sleep(10000)
        tareas.remove(tarea)
        tareasLiveData.postValue(tareas)
    }

    fun getTareasFiltroSinPagar(soloSinPagar:Boolean): LiveData<ArrayList<Tarea>> {
        //devuelve el LiveData con la  lista filtrada o entera
        tareasLiveData.value=if(soloSinPagar)
            tareas.filter { !it.pagado } as ArrayList<Tarea>
        else
            tareas
        return tareasLiveData
    }

    fun getTareasFiltroEstado(estado: Int): LiveData<ArrayList<Tarea>>{
        when(estado){
            3 -> tareasLiveData.value = tareas
            else ->{
                tareasLiveData.value = tareas.filter { it.estado ==estado} as ArrayList<Tarea>
            }
        }
        return tareasLiveData
    }

    fun getTareasFiltroSinPagarEstado(soloSinPagar:Boolean, estado:Int): LiveData<ArrayList<Tarea>> {
        //devuelve el LiveData con la  lista filtrada
        tareasLiveData.value=tareas.filter { !it.pagado && it.estado==estado } as ArrayList<Tarea>
        return tareasLiveData
    }

}