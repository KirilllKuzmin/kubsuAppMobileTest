package com.kubsu.kubsuappmobile.ui.main.timetable

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.kubsu.kubsuappmobile.adapter.TimetableAdapter
import com.kubsu.kubsuappmobile.databinding.FragmentTimetableBinding
import com.kubsu.kubsuappmobile.network.MainService
import com.kubsu.kubsuappmobile.ui.auth.AuthViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDate
import java.time.OffsetDateTime
import java.time.ZoneOffset

class TimetableFragment : Fragment() {

    private lateinit var adapter: TimetableAdapter

    private lateinit var binding: FragmentTimetableBinding

    private lateinit var mainService: MainService

    private val viewModel : AuthViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentTimetableBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRetrofit()
        initRcView()
        viewModel.token.observe(viewLifecycleOwner) {token ->
            CoroutineScope(Dispatchers.IO).launch {
                val sysdate = OffsetDateTime.of(LocalDate.now().atStartOfDay(), ZoneOffset.UTC)
                val timetables = mainService.getTimetables("Bearer " + token, sysdate, sysdate)
                    .sortedBy { it.numberTimeClassHeld.startTime }
                initRetrofitAuth()
                val groups = mainService.getGroups("Bearer " + token)
                //Далее проходимся по каждой паре, в которой проходимся по списку групп, которым присваиваем
                //name из groups
                requireActivity().runOnUiThread {
                    adapter.submitList(timetables)
                }
            }
        }

    }

    private fun initRetrofit() {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        val client = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8081/").client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        mainService = retrofit.create(MainService::class.java)
    }

    private fun initRetrofitAuth() {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        val client = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8080/").client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        mainService = retrofit.create(MainService::class.java)
    }

    private fun initRcView() = with(binding) {
        adapter = TimetableAdapter()
        rcView.layoutManager = LinearLayoutManager(context)
        rcView.adapter = adapter
    }
}