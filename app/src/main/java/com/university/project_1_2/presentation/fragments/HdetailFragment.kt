package com.university.project_1_2.presentation.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.asLiveData
import com.university.project_1_2.data.dbs.MainDB
import com.university.project_1_2.data.tables.History
import com.university.project_1_2.data.tables.Saved
import com.university.project_1_2.databinding.FragmentHdetailBinding


class HdetailFragment : Fragment() {
    private lateinit var binding: FragmentHdetailBinding
    private var ID = 1
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
    }
    private fun ShowQuote(context: Context){
        val db = MainDB.getDB(context)
        db.getDaoHistory().getHistory().asLiveData().observe(viewLifecycleOwner) { list ->
            list.forEach {
                if(it.id==ID){showData(it)}
            }
        }
    }
    private fun showData(data: History)= with(binding){
        tvAnimeTitle.text = data.anime
        tvCharacter.text = "-${data.character}-"
        tvQuote.text = "''${data.quote}''"
    }
    companion object {
        @JvmStatic
        fun newInstance() = HdetailFragment()
    }
}