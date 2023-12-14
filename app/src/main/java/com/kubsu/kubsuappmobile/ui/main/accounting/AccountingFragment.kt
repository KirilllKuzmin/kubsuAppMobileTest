package com.kubsu.kubsuappmobile.ui.main.accounting

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.kubsu.kubsuappmobile.R
import com.kubsu.kubsuappmobile.adapter.AccountingAdapter
import com.kubsu.kubsuappmobile.adapter.TimetableAdapter
import com.kubsu.kubsuappmobile.databinding.FragmentAccountingBinding
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

class AccountingFragment : Fragment() {

    private lateinit var adapter: AccountingAdapter

    private lateinit var binding: FragmentAccountingBinding

    private lateinit var mainService: MainService

    private val viewModel : AuthViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAccountingBinding.inflate(inflater, container, false)
        binding.bNavigationAccounting.selectedItemId = R.id.menu_accounting
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRetrofit()
        initRcView()
        binding.bNavigationAccounting.selectedItemId = R.id.menu_accounting
        binding.bNavigationAccounting.setOnNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.menu_timetable -> {
                    findNavController().navigate(R.id.TimetableFragment)
                }
            }
            true
        }
        viewModel.token.observe(viewLifecycleOwner) {token ->
            CoroutineScope(Dispatchers.IO).launch {
                var courses = mainService.getLecturerCourses("Bearer " + token)
                    .sortedBy { it.name }

                requireActivity().runOnUiThread {
                    adapter.submitList(courses)
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

    private fun initRcView() = with(binding) {
        adapter = AccountingAdapter()
        rcView.layoutManager = LinearLayoutManager(context)
        rcView.adapter = adapter
    }

}