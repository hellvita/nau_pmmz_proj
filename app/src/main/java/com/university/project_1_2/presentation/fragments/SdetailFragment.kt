package com.university.project_1_2.presentation.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import com.university.project_1_2.R
import com.university.project_1_2.data.dbs.MainDB
import com.university.project_1_2.data.tables.Saved
import com.university.project_1_2.databinding.FragmentSdetailBinding

class SdetailFragment : Fragment() {
    private lateinit var binding: FragmentSdetailBinding
    private var ID = 1
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSdetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)= with(binding){
        super.onViewCreated(view, savedInstanceState)
        ID = arguments?.getInt("sID")!!
        val context = context
        ShowQuote(context!!)
        imDeleteQuote.setOnClickListener{
            Toast.makeText(context!!, "Deleted!", Toast.LENGTH_SHORT).show()
            deleteQuote(context)
        }
    }
    private fun ShowQuote(context: Context){
        val db = MainDB.getDB(context)
        db.getDaoSaved().getAllSavedQs().asLiveData().observe(viewLifecycleOwner) { list ->
            list.forEach {
                if(it.id==ID){showData(it)}
            }
        }
    }
    private fun showData(data: Saved)= with(binding){
        tvAnimeTitle.text = data.anime
        tvCharacter.text = "-${data.character}-"
        tvQuote.text = "''${data.quote}''"
    }
    private fun deleteQuote(context: Context){
        val db = MainDB.getDB(context)
            db.getDaoSaved().getAllSavedQs().asLiveData().observe(viewLifecycleOwner){list->
                list.forEach { if (it.id==ID){
                    var thread = Thread{db.getDaoSaved().deleteQsByID(ID)}
                    thread.start()
                    thread.join()
                } }
            }
    }
    companion object {
        @JvmStatic
        fun newInstance() = SdetailFragment()
    }
}