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
    private val soloSinPagarLiveData= MutableLiveData<Boolean>(false)
    //inicio ViewModel
    init {
        //inicia repositorio
        Repository(getApplication<Application>().applicationContext)
        repositorio=Repository
        tareasLiveData=Transformations.switchMap(soloSinPagarLiveData)
        {soloSinPagar->Repository.getTareasFiltroSinPagar(soloSinPagar)}
    }
    fun addTarea(tarea: Tarea) = repositorio.addTarea(tarea)
    fun delTarea(tarea: Tarea) = repositorio.delTarea(tarea)

    fun setSoloSinPagar(soloSinPagar:Boolean){soloSinPagarLiveData.value=soloSinPagar}



}
