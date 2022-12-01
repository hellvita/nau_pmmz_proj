package com.university.project_1_2.presentation.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.university.project_1_2.R
import com.university.project_1_2.data.dbs.MainDB
import com.university.project_1_2.data.tables.History
import com.university.project_1_2.databinding.FragmentGlistRandBinding
import com.university.project_1_2.domain.MainViewModel
import com.university.project_1_2.presentation.adapters.QuoteModel
import com.university.project_1_2.presentation.adapters.QuotesListAdapter


class GlistRandFragment : Fragment(), QuotesListAdapter.Listener {
    private lateinit var binding: FragmentGlistRandBinding
    private lateinit var adapter: QuotesListAdapter
    private val model: MainViewModel by activityViewModels()
    private val thereIsData = true
    private val thereIsList = true
    private var ID = 1
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGlistRandBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val context = context
        initRecView()
        getSavedQuotes(context!!)
        ReadDataFromDB()
    }
    private fun ReadDataFromDB()= with(binding){
        model.QuotesList.observe(viewLifecycleOwner){
            adapter.submitList(it.subList(0, it.size))
        }
    }
    private fun getSavedQuotes(context: Context)= with(binding){
        val db = MainDB.getDB(context)
        var list1 = ArrayList<QuoteModel>()
        db.getDaoGenerated().getLastTenItems().asLiveData().observe(viewLifecycleOwner){list->
            list.forEach {
                val item1 = QuoteModel(
                    it.id!!, it.anime, it.character, it.quote, it.time
                )
                list1.add(item1)
            }
            model.QuotesList.value=list1
        }
    }
    private fun initRecView()=with(binding){
        recViewGlist.layoutManager = LinearLayoutManager(activity)
        adapter = QuotesListAdapter(this@GlistRandFragment)
        recViewGlist.adapter = adapter
    }
    private fun openGdetailFragment(item: QuoteModel){
        ID = item.ID
        SaveToHistory(item.ID,item.Anime,item.Character,item.Quote, item.Time)
        val controller = findNavController()
        val bundle = Bundle()
        bundle.putBoolean("thereIsData", thereIsData)
        bundle.putBoolean("thereIsList", thereIsList)
        bundle.putInt("ID", ID)
        controller.navigate(R.id.gdetailFragment, bundle)
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
    override fun onClick(item: QuoteModel) {
        openGdetailFragment(item)
    }
    companion object {
        @JvmStatic
        fun newInstance() = GlistRandFragment()
    }
}