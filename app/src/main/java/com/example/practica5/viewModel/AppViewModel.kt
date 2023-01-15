package com.example.practica5.viewModel

import android.app.Application
import androidx.lifecycle.*
import com.example.practica5.model.Tarea
import com.example.practica5.repository.Repository

class AppViewModel(application: Application) : AndroidViewModel(application) {
    private  val repositorio: Repository
    //liveData de lista de tareas
    val tareasLiveData : LiveData<ArrayList<Tarea>>
    //creamos el LiveData de tipo Booleano. Repesenta nuestro filtro
    //private val soloSinPagarLiveData= MutableLiveData<Boolean>(false)
    private val porEstadoLiveData = MutableLiveData<Int>()

    val SOLO_SIN_PAGAR="SOLO_SIN_PAGAR"
    val ESTADO="ESTADO"
    private val filtrosLiveData by lazy{
        val mutableMap = mutableMapOf<String, Any?>(
            SOLO_SIN_PAGAR to false,
            ESTADO to 3
        )
        MutableLiveData(mutableMap)
    }
    init {
        //inicia repositorio
        Repository(getApplication<Application>().applicationContext)
        repositorio=Repository
        //tareasLiveData=Transformations.switchMap(soloSinPagarLiveData)
        //{soloSinPagar->Repository.getTareasFiltroSinPagar(soloSinPagar)}
        tareasLiveData=Transformations.switchMap(filtrosLiveData)
        { mapFiltro ->
            val aplicarSinPagar = mapFiltro!![SOLO_SIN_PAGAR] as Boolean
            val estado = mapFiltro[ESTADO] as Int
            //Devuelve el resultado del when
            when {//trae toda la lista de tareas
                (!aplicarSinPagar && (estado == 3)) -> repositorio.getAllTareas()
                //Sólo filtra por ESTADO
                (!aplicarSinPagar && (estado != 3)) -> repositorio.getTareasFiltroEstado(estado)
                //Sólo filtra SINPAGAR
                (aplicarSinPagar && (estado == 3)) -> repositorio.getTareasFiltroSinPagar(
                    aplicarSinPagar
                )//Filtra por ambos
                else -> repositorio.getTareasFiltroSinPagarEstado(aplicarSinPagar, estado)
            }
        }
    }
    fun addTarea(tarea: Tarea) = repositorio.addTarea(tarea)
    fun delTarea(tarea: Tarea) = repositorio.delTarea(tarea)

    //fun setSoloSinPagar(soloSinPagar:Boolean){soloSinPagarLiveData.value=soloSinPagar}

    fun setPorEstado(estado:Int){porEstadoLiveData.value = estado}

    fun setSoloSinPagar(soloSinPagar: Boolean) {
        //recuperamos el map
        val mapa = filtrosLiveData.value
        //modificamos el filtro
        mapa!![SOLO_SIN_PAGAR] = soloSinPagar
        //activamos el LiveData
        filtrosLiveData.value = mapa
    }
    /**
     * Modifica el Map filtrosLiveData el elemento "ESTADO"
     * que activará el Transformations de TareasLiveData lo
     *llamamos cuando cambia el RadioButton
     */
    fun setEstado(estado: Int) {
        //recuperamos el map
        val mapa = filtrosLiveData.value
        //modificamos el filtro
        mapa!![ESTADO] = estado
        //activamos el LiveData
        filtrosLiveData.value = mapa
    }


}
