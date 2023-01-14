package com.example.practica5.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.practica5.model.Tarea
import com.example.practica5.repository.Repository

class AppViewModel(application: Application) : AndroidViewModel(application) {
    private  val repositorio: Repository
    //liveData de lista de tareas
    val tareasLiveData : LiveData<ArrayList<Tarea>>
    //inicio ViewModel
    init {
        //inicia repositorio
        Repository(getApplication<Application>().applicationContext)
        repositorio=Repository
        tareasLiveData = repositorio.getAllTareas()
    }
    fun addTarea(tarea: Tarea) = repositorio.addTarea(tarea)
    fun delTarea(tarea: Tarea) = repositorio.delTarea(tarea)
}
