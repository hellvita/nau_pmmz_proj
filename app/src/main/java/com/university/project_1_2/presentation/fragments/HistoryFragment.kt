package com.university.project_1_2.presentation.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.university.project_1_2.R
import com.university.project_1_2.data.dbs.MainDB
import com.university.project_1_2.databinding.FragmentHistoryBinding
import com.university.project_1_2.domain.MainViewModel
import com.university.project_1_2.presentation.adapters.HistoryAdapter
import com.university.project_1_2.presentation.adapters.QuoteModel
import com.university.project_1_2.presentation.adapters.QuotesListAdapter


class HistoryFragment : Fragment(), HistoryAdapter.Listener {
    private lateinit var binding: FragmentHistoryBinding
    private var noHistory = true
    private lateinit var adapter: HistoryAdapter
    private val model: MainViewModel by activityViewModels()
    private var ID = 1
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val context = context
        initRecView()
        getSavedQuotes(context!!)
        ShowSavedQuotes()
    }
    private fun getSavedQuotes(context: Context)= with(binding){
        val db = MainDB.getDB(context)
        var list1 = ArrayList<QuoteModel>()
        db.getDaoHistory().getHistory().asLiveData().observe(viewLifecycleOwner){list->
            list.forEach {
                val item1 = QuoteModel(
                    it.id!!, it.anime, it.character, it.quote, it.time
                )
                list1.add(item1)
            }
            model.QuotesList.value=list1
            if(list.isEmpty()){noHistory=true}
            else {noHistory=false}
            checkQs(noHistory)
        }
    }
    private fun ShowSavedQuotes()= with(binding){
        model.QuotesList.observe(viewLifecycleOwner){
            adapter.submitList(it.subList(0, it.size))
        }
    }
    private fun checkQs(noQs:Boolean)= with(binding){
        if(noQs) {
            tvNoHistory.isVisible = true
        }
        else {
            tvNoHistory.isGone = true
        }
    }
    private fun initRecView()=with(binding){
        recViewHistory.layoutManager = LinearLayoutManager(activity)
        adapter = HistoryAdapter(this@HistoryFragment)
        recViewHistory.adapter = adapter
    }
    private fun openHdetailFragment(item: QuoteModel){
        ID = item.ID
        val controller = findNavController()
        val bundle = Bundle()
        bundle.putInt("hID", ID)
        controller.navigate(R.id.hdetailFragment, bundle)
    }
    override fun onClick(item: QuoteModel) {
        openHdetailFragment(item)
    }
    companion object {
        @JvmStatic
        fun newInstance() = HistoryFragment()
    }

}