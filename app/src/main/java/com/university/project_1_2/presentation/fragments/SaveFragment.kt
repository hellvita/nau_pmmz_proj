package com.university.project_1_2.presentation.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.AnyThread
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.university.project_1_2.R
import com.university.project_1_2.data.dbs.MainDB
import com.university.project_1_2.databinding.FragmentSaveBinding
import com.university.project_1_2.domain.MainViewModel
import com.university.project_1_2.presentation.adapters.QuoteModel
import com.university.project_1_2.presentation.adapters.QuotesListAdapter
import kotlin.concurrent.thread


class SaveFragment : Fragment(), QuotesListAdapter.Listener {
    private lateinit var binding: FragmentSaveBinding
    private lateinit var adapter: QuotesListAdapter
    private val model: MainViewModel by activityViewModels()
    private var noSavedQuotes = true
    private var ID = 1
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSaveBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val context = context
        initRecView()
        getSavedQuotes(context!!)
        ShowSavedQuotes()
    }
    private fun ShowSavedQuotes()= with(binding){
        model.QuotesList.observe(viewLifecycleOwner){
            adapter.submitList(it.subList(0, it.size))
        }
    }
    private fun checkQs(noQs:Boolean)= with(binding){
        if(noQs) {
            tvNoSave.isVisible = true
        }
        else {
            tvNoSave.isGone = true
        }
    }
    private fun getSavedQuotes(context: Context)= with(binding){
        val db = MainDB.getDB(context)
        var list1 = ArrayList<QuoteModel>()
            db.getDaoSaved().getAllSavedQs().asLiveData().observe(viewLifecycleOwner){list->
                list.forEach {
                    val item1 = QuoteModel(
                        it.id!!, it.anime, it.character, it.quote, it.time
                    )
                    list1.add(item1)
                }
                model.QuotesList.value=list1
                if(list.isEmpty()){noSavedQuotes=true}
                else {noSavedQuotes=false}
                checkQs(noSavedQuotes)
            }
    }
    private fun initRecView()=with(binding){
        recViewSave.layoutManager = LinearLayoutManager(activity)
        adapter = QuotesListAdapter(this@SaveFragment)
        recViewSave.adapter = adapter
    }
    private fun openSdetailFragment(item: QuoteModel){
        ID = item.ID
        val controller = findNavController()
        val bundle = Bundle()
        bundle.putInt("sID", ID)
        controller.navigate(R.id.sdetailFragment, bundle)
    }
    override fun onClick(item: QuoteModel) {
        openSdetailFragment(item)
    }
    companion object {
        @JvmStatic
        fun newInstance() = SaveFragment()
    }

}