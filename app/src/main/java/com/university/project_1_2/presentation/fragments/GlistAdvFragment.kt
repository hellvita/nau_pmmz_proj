package com.university.project_1_2.presentation.fragments

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.university.project_1_2.R
import com.university.project_1_2.databinding.FragmentGlistAdvBinding
import com.university.project_1_2.presentation.adapters.QuoteModel
import com.university.project_1_2.presentation.adapters.QuotesListAdapter
import com.university.project_1_2.data.dbs.MainDB
import com.university.project_1_2.data.tables.Generated
import com.university.project_1_2.data.tables.History
import com.university.project_1_2.domain.MainViewModel
import org.json.JSONArray
import org.json.JSONObject
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class GlistAdvFragment : Fragment(), QuotesListAdapter.Listener {
    val URL_BYANIME = "https://animechan.vercel.app/api/random/anime?title="
    val URL_BY10ANIME = "https://animechan.vercel.app/api/quotes/anime?title="
    val URL_BYCHARACTER = "https://animechan.vercel.app/api/random/character?name="
    val URL_BY10CHARACTER = "https://animechan.vercel.app/api/quotes/character?name="
    private lateinit var binding: FragmentGlistAdvBinding
    private lateinit var adapter: QuotesListAdapter
    private var TEN = false
    private var isNewAdv = false
    private var KEY_WORD:String = ""
    private val KEY_ANIME:Int = 1
    private val KEY_CHARACTER:Int = 2
    private var CUR_KEY_WORD:Int = 1
    private val thereIsData = true
    private var thereIsList = true
    private var ID = 1
    private val model: MainViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGlistAdvBinding.inflate(inflater, container, false)
        return binding.root
    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecView()
        TEN = arguments?.getBoolean("isTen")!!
        if(!TEN)thereIsList=false
        isNewAdv = arguments?.getBoolean("isNewAdv", false)!!
        setKeyType()
        val context = context
        binding.ibGenerate.setOnClickListener{
            KEY_WORD = binding.etGenerateBy.text.toString()
            if (CUR_KEY_WORD==KEY_ANIME){
                if(TEN) requestQuoteData(context!!,URL_BY10ANIME+"$KEY_WORD")
                else requestQuoteData(context!!,URL_BYANIME+"$KEY_WORD")
            }
            else if (CUR_KEY_WORD==KEY_CHARACTER){
                if(TEN)requestQuoteData(context!!,URL_BY10CHARACTER+"$KEY_WORD")
                else requestQuoteData(context!!,URL_BYCHARACTER+"$KEY_WORD")
            }
            getSavedQuotes(context!!,TEN)
            arguments?.putBoolean("isNewAdv", false)
            ReadDataFromDB()
        }
        if (!isNewAdv) ReadDataFromDB()
    }
    private fun initRecView()=with(binding){
        recViewGlist.layoutManager = LinearLayoutManager(activity)
        adapter = QuotesListAdapter(this@GlistAdvFragment)
        recViewGlist.adapter = adapter
    }

    private fun setKeyType()= with(binding){
        binding.bSetKewValue.setOnClickListener {
            if(CUR_KEY_WORD==KEY_ANIME){
                bSetKewValue.text = getString(R.string.key_word_character)
                CUR_KEY_WORD = KEY_CHARACTER
            }
            else if(CUR_KEY_WORD==KEY_CHARACTER){
                bSetKewValue.text = getString(R.string.key_word_anime)
                CUR_KEY_WORD = KEY_ANIME
            }
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun requestQuoteData(context: Context, url:String){
        val queue = Volley.newRequestQueue(context)
        val stringRequest = StringRequest(
            Request.Method.GET,url,
            { responce->
                if(TEN){
                    var arrayObj = JSONArray(responce)
                    for(i in 0 until arrayObj.length() ){
                        val formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm")
                        val time = LocalDateTime.now().format(formatter)
                        val quote = arrayObj[i] as JSONObject
                        val db = MainDB.getDB(context)
                        val generated = Generated(
                            null,
                            quote.getString("anime"),
                            quote.getString("character"),
                            quote.getString("quote"),
                            time
                        )
                        var threadInsert3 = Thread {
                            db.getDaoGenerated().insertItem(generated)
                        }
                        threadInsert3.start()
                        threadInsert3.join()
                    }

                }
                else {
                    var arrayObj = JSONObject(responce)
                    val formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm")
                    val time = LocalDateTime.now().format(formatter)
                    val db = MainDB.getDB(context)
                    val generated = Generated(
                        null,
                        arrayObj.getString("anime"),
                        arrayObj.getString("character"),
                        arrayObj.getString("quote"),
                        time
                    )
                    var threadInsert4 = Thread {
                        db.getDaoGenerated().insertItem(generated)
                    }
                    threadInsert4.start()
                    threadInsert4.join()
                }

            },
            {
                Log.d("AdvFragment","Volley error: $it")
            }
        )
        queue.add(stringRequest)
    }
    private fun getSavedQuotes(context: Context, ten:Boolean)= with(binding){
        val db = MainDB.getDB(context)
        var list1 = ArrayList<QuoteModel>()
        if(ten){
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

        else {
            db.getDaoGenerated().getLastItem().asLiveData().observe(viewLifecycleOwner){list->
                list.forEach {
                    val item1 = QuoteModel(
                        it.id!!, it.anime, it.character, it.quote, it.time
                    )
                    list1.add(item1)
                }
                model.QuotesList.value=list1
            }
        }
    }
    private fun ReadDataFromDB()= with(binding){
        model.QuotesList.observe(viewLifecycleOwner){
            adapter.submitList(it.subList(0, it.size))
        }
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
        fun newInstance() = GlistAdvFragment()
    }

}