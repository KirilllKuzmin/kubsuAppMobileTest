package com.kubsu.kubsuappmobile.ui.main.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.kubsu.kubsuappmobile.R
import com.kubsu.kubsuappmobile.data.model.AuthRequest
import com.kubsu.kubsuappmobile.databinding.FragmentAuthBinding
import com.kubsu.kubsuappmobile.network.MainService
import com.kubsu.kubsuappmobile.ui.auth.AuthViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AuthFragment : Fragment() {

    private lateinit var binding: FragmentAuthBinding

    private lateinit var mainService : MainService

    private val viewModel : AuthViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentAuthBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRetrofit()

        binding.apply {
            binding.bNext.setOnClickListener {
                findNavController().navigate(R.id.action_LoginFragment_to_ProductsFragment)

            }
            bSignIn.setOnClickListener {
                auth(
                    AuthRequest(
                        login.text.toString(),
                        password.text.toString()
                    )
                )
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
            .baseUrl("http://10.0.2.2:8080/").client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        mainService = retrofit.create(MainService::class.java)
    }

    private fun auth(authRequest: AuthRequest) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = mainService.auth(authRequest)

            val errorMessage = response.errorBody()?.string()?.let {
                JSONObject(it).getString("error")
            }

            requireActivity().runOnUiThread {
                binding.errorMessage.text = errorMessage
                val user = response.body()

                if (user != null) {
                    binding.name.text = user.username
                    binding.bNext.visibility = View.VISIBLE

                    viewModel.token.value = user.token
                }
            }
        }
    }
}