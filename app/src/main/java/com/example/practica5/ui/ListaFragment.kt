package com.example.practica5.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.practica5.R
import com.example.practica5.adapter.TareasAdapter
import com.example.practica5.databinding.FragmentListaBinding
import com.example.practica5.model.Tarea
import com.example.practica5.viewModel.AppViewModel

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class ListaFragment : Fragment() {

    private var _binding: FragmentListaBinding? = null
    private val viewModel: AppViewModel by activityViewModels()
    lateinit var  tareasAdapter:TareasAdapter

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentListaBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        iniciaRecyclerView()
        iniciaFiltros()
        iniciaCRUD()

        viewModel.tareasLiveData.observe(viewLifecycleOwner, Observer<List<Tarea>> { lista ->
            //actualizaLista(lista)
            tareasAdapter.setLista(lista)

        })

        binding.fabNuevo.setOnClickListener {
            //creamos acción enviamos argumento nulo porque queremos crear NuevaTarea
            val action = ListaFragmentDirections.actionEditar(null)
            findNavController().navigate(action)
        }

        //para prueba, editamos una tarea aleatoria
        /**binding.btPruebaEdicion.setOnClickListener {
            //cogemos la lista actual de Tareas que tenemos en el ViewModel. No es lo más correcto
            val lista = viewModel.tareasLiveData.value
            //buscamos una tarea aleatoriamente
            val tarea = lista?.get((0..lista.lastIndex).random())
            //se la enviamos a TareaFragment para su edición
            val action = ListaFragmentDirections.actionEditar(tarea)
            findNavController().navigate(action)
        }
        **/

        iniciaFiltros()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun actualizaLista(lista: List<Tarea>?) {
        var listaString = ""
        lista?.forEach() {
            listaString =
                "$listaString ${it.id}-${it.tecnico}-${it.descripcion}-${if (it.pagado) "pagado" else "no pagado"}\n"
        }
        //binding.tvLista.text = listaString
    }

    private fun iniciaFiltros() {
        binding.swSinPagar.setOnCheckedChangeListener() { _, isChecked ->
            //actualiza el LiveData SoloSinPagarLiveData que a su vez modifica tareasLiveData 
            //mediante el Transformation
            viewModel.setSoloSinPagar(isChecked) }

        binding.rgTareas.setOnCheckedChangeListener { _, i ->
            when (resources.getResourceEntryName(i)) {
                "rbTAbierta" -> viewModel.setPorEstado(0)
                "rbTEncurso" -> viewModel.setPorEstado(1)
                "rbTCerrada" -> viewModel.setPorEstado(2)
                "rb4TTodas" -> viewModel.setPorEstado(3)
            }
        }
    }

    private fun iniciaRecyclerView() {
        //creamos el adaptador
        tareasAdapter = TareasAdapter()

        with(binding.rvTareas) {
            //Creamos el layoutManager
            layoutManager = LinearLayoutManager(activity)
            //le asignamos el adaptador
            adapter = tareasAdapter
        }
    }
    private fun iniciaCRUD(){
        binding.fabNuevo.setOnClickListener{
            val action=ListaFragmentDirections.actionEditar(null)
            findNavController().navigate(action)
        }

        tareasAdapter.onTareaClickListener = object : TareasAdapter.OnTareaClickListener {
            //**************Editar  Tarea*************
            override fun onTareaClick(tarea: Tarea?) {
                //creamos acción enviamos argumento la tarea para editarla
                val action = ListaFragmentDirections.actionEditar(tarea)
                findNavController().navigate(action)
            }
            //***********Borrar Tarea************
            override fun onTareaBorrarClick(tarea: Tarea?) {
                //borramos directamente la tarea
                viewModel.delTarea(tarea!!)
            }

        }
    }
}