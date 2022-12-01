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
import com.university.project_1_2.data.dbs.MainDB
import com.university.project_1_2.data.tables.History
import com.university.project_1_2.data.tables.Saved
import com.university.project_1_2.databinding.FragmentHdetailBinding


class HdetailFragment : Fragment() {
    private lateinit var binding: FragmentHdetailBinding
    private var ID = 1
    private var curID = 0
    private var curANIME = ""
    private var curCHARACTER = ""
    private var curQUOTE = ""
    private var curTIME = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHdetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ID = arguments?.getInt("hID")!!
        val context = context
        ShowQuote(context!!)
        binding.imSaveQuote.setOnClickListener{
            SaveQuote(context)
            Toast.makeText(context!!,"Saved!", Toast.LENGTH_SHORT).show()
        }
    }
    private fun ShowQuote(context: Context){
        val db = MainDB.getDB(context)
        db.getDaoHistory().getHistory().asLiveData().observe(viewLifecycleOwner) { list ->
            list.forEach {
                if(it.id==ID){showData(it)}
            }
        }
    }
    private fun SaveQuote(context: Context){
        val db = MainDB.getDB(context)
        val saved = Saved(
            curID,
            curANIME,
            curCHARACTER,
            curQUOTE,
            curTIME
        )
        var threadInsert5 = Thread {
            db.getDaoSaved().insertSaved(saved)
        }
        threadInsert5.start()
        threadInsert5.join()
    }
    private fun showData(data: History)= with(binding){
        curID = data.id!!
        curANIME = data.anime
        curCHARACTER = data.character
        curQUOTE = data.quote
        curTIME = data.time
        tvAnimeTitle.text = data.anime
        tvCharacter.text = "-${data.character}-"
        tvQuote.text = "''${data.quote}''"
    }
    companion object {
        @JvmStatic
        fun newInstance() = HdetailFragment()
    }
}