package com.university.project_1_2.presentation.fragments

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.university.project_1_2.R
import com.university.project_1_2.data.dbs.MainDB
import com.university.project_1_2.data.tables.Generated
import com.university.project_1_2.databinding.FragmentHomeBinding
import com.university.project_1_2.domain.MainViewModel
import com.university.project_1_2.presentation.adapters.QuoteModel
import org.json.JSONArray
import org.json.JSONObject
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.properties.Delegates


class HomeFragment : Fragment() {
    val URL_RANDOM = "https://animechan.vercel.app/api/random"
    val URL_RANDOM10 = "https://animechan.vercel.app/api/quotes"
    private var check1 by Delegates.notNull<Int>()
    private var TEN = false
    private val NEW = true
    private lateinit var binding: FragmentHomeBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        clickFun()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun clickFun(){
        check1 = 0
        binding.bGenerate.setOnClickListener{
            if (check1==0) {
                binding.cvBtn1Btns.isVisible = true
                check1 = 1
            }
            else if (check1==1) {
                binding.cvBtn1Btns.isGone = true
                check1 = 0
            }
        }
        binding.bGenerate10unpressed.setOnClickListener {
            binding.bGenerate10unpressed.isGone = true
            binding.bGenerate10pressed.isVisible = true
            TEN = true
        }
        binding.bGenerate10pressed.setOnClickListener {
            binding.bGenerate10unpressed.isVisible = true
            binding.bGenerate10pressed.isGone = true
            TEN = false
        }
        binding.bRandom.setOnClickListener {
            val context = context
            if(TEN){
                GenerateTenQuotes(URL_RANDOM10, context!!)
                openGlistRandFragment()
            }
            else {
                GenerateOneQuote(URL_RANDOM, context!!)
                openGdetailFragment()
            }
        }
        binding.bByKeyWord.setOnClickListener {
            openGlistAdvFragment()
        }
        binding.bSaved.setOnClickListener {
            openSaveFragment()
        }
        binding.bHistory.setOnClickListener {
            openHistoryFragment()
        }
    }

    private fun openGlistRandFragment(){
        val controller = findNavController()
        val bundle = Bundle()
        bundle.putBoolean("isNew", NEW)
        controller.navigate(R.id.glistRandFragment, bundle)
    }

    private fun openGlistAdvFragment(){
        val controller = findNavController()
        val bundle = Bundle()
        bundle.putBoolean("isNewAdv", NEW)
        bundle.putBoolean("isTen", TEN)
        controller.navigate(R.id.glistAdvFragment, bundle)
    }

    private fun openGdetailFragment(){
        val controller = findNavController()
        controller.navigate(R.id.gdetailFragment)
    }

    private fun openSaveFragment(){
        val controller = findNavController()
        controller.navigate(R.id.saveFragment)
    }

    private fun openHistoryFragment(){
        val controller = findNavController()
        controller.navigate(R.id.historyFragment)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun GenerateOneQuote(url:String, context: Context){
        val queue = Volley.newRequestQueue(context)
        val stringRequest = StringRequest(
            com.android.volley.Request.Method.GET,url,
            { responce->
                val formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm")
                val time = LocalDateTime.now().format(formatter)
                val obj = JSONObject(responce)
                val db = MainDB.getDB(context)
                val generated = Generated(
                    null,
                    obj.getString("anime"),
                    obj.getString("character"),
                    obj.getString("quote"),
                    time
                )
                var threadInsert1 = Thread {
                    db.getDaoGenerated().insertItem(generated)
                }
                threadInsert1.start()
                threadInsert1.join()
            },
            {
                Log.d("HomeFragment","Volley error: $it")
            }
        )
        queue.add(stringRequest)
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun GenerateTenQuotes(url:String, context: Context){
        val queue = Volley.newRequestQueue(context)
        val stringRequest = StringRequest(
            com.android.volley.Request.Method.GET,url,
            { responce->
                var arrayObj = JSONArray(responce)
                var list = ArrayList<QuoteModel>()
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
                    var threadInsert2 = Thread {
                        db.getDaoGenerated().insertItem(generated)
                    }
                    threadInsert2.start()
                    threadInsert2.join()
                }
            },
            {
                Log.d("HomeFragment","Volley error: $it")
            }
        )
        queue.add(stringRequest)
    }

    companion object {
        @JvmStatic
        fun newInstance() = HomeFragment()
    }
}