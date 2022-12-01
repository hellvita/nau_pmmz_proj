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
import com.university.project_1_2.data.tables.Generated
import com.university.project_1_2.data.tables.History
import com.university.project_1_2.data.tables.Saved
import com.university.project_1_2.databinding.FragmentGdetailBinding

class GdetailFragment : Fragment() {
    private lateinit var binding: FragmentGdetailBinding
    private var thereIsData = false
    private var thereIsList = false
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
        binding = FragmentGdetailBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        thereIsData = arguments?.getBoolean("thereIsData")!!
        thereIsList = arguments?.getBoolean("thereIsList")!!
        arguments?.putBoolean("thereIsList", false)
        ID = arguments?.getInt("ID")!!
        val context = context
        ReadDataFromDB(context!!, thereIsData, thereIsList)
        binding.imSaveQuote.setOnClickListener{
            SaveQuote(context)
            Toast.makeText(context!!,"Saved!",Toast.LENGTH_SHORT).show()
        }
    }
    private fun ShowQuoteByID(context: Context, db: MainDB){
        db.getDaoGenerated().getAllItem().asLiveData().observe(viewLifecycleOwner) { list ->
            list.forEach {
                if(it.id==ID){showData(it)}
            }
        }
    }
    private fun ShowLastQuote(context: Context, db: MainDB){
        db.getDaoGenerated().getLastItem().asLiveData().observe(viewLifecycleOwner) { list ->
            list.forEach {showData(it)}
        }
    }
    private fun ReadDataFromDB(context: Context, needDataID:Boolean, useList:Boolean)= with(binding){
        val db = MainDB.getDB(context)
        if(needDataID){ ShowQuoteByID(context,db)}
        else {ShowLastQuote(context,db) }
    }
    private fun showData(data:Generated)= with(binding){
        curID = data.id!!
        curANIME = data.anime
        curCHARACTER = data.character
        curQUOTE = data.quote
        curTIME = data.time
        tvAnimeTitle.text = curANIME
        tvCharacter.text = "-$curCHARACTER-"
        tvQuote.text = "''$curQUOTE''"
        SaveToHistory(curID,curANIME,curCHARACTER,curQUOTE,curTIME)
    }
    private fun SaveToHistory(id:Int,anime:String,character:String,quote:String,time:String){
        val context = context
        val db = MainDB.getDB(context!!)
        val history = History(
            id,anime,character,quote,time
        )
        var threadInsert2 = Thread {
            db.getDaoHistory().insertHistory(history)
        }
        threadInsert2.start()
        threadInsert2.join()
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
    companion object {
        @JvmStatic
        fun newInstance() = GdetailFragment()
    }
}