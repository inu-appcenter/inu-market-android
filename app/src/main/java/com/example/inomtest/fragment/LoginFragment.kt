package com.example.inomtest.fragment

import android.content.ContentValues.TAG
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.example.inomtest.*
import com.example.inomtest.dataClass.LoginData
import com.example.inomtest.databinding.FragmentLoginBinding
import com.example.inomtest.databinding.FragmentSignupFinishBinding
import com.example.inomtest.network.InomApi
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    lateinit var navController: NavController

    lateinit var inuID : String
    lateinit var password : String
    var accessToken: String = ""

    val bundle = Bundle()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.loginBtn.setOnClickListener{
            inuID = binding.loginIDEdit.text.toString()
            password = binding.loginPWEdit.text.toString()

            accessToken = login(inuID, password)
            if (accessToken != "") {
                it.findNavController().navigate(R.id.action_loginFragment_to_homeFragment, bundle)
            }
        }
        binding.loginToSignup.setOnClickListener{
            it.findNavController().navigate(R.id.action_loginFragment_to_signupMainFragment)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            LoginFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }

    fun login(inuID: String, password: String) : String {
        var LoginData = LoginData()
        LoginData.inuId = inuID
        LoginData.password = password
        LoginData.pushToken = "pushToken"

        val paramObject = JSONObject()
        paramObject.put("inuId", inuID)
        paramObject.put("password", password)
        paramObject.put("pushToken", "pushToken")
        //val request = RequestBody.create(MediaType.parse("application/json"),paramObject.toString())
        val request = RequestBody.create("application/json"?.toMediaTypeOrNull(),paramObject.toString())

        val call = InomApi.createApi().login(request)

        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {

                    Log.d("로그인결과1", "통신결과"+response.code().toString())
                    Log.d("액세스토큰", "통신결과"+response.headers().get("Authorization"))
                    accessToken = response.headers().get("Authorization").toString()
                    bundle.putString("accessToken", accessToken)
                    bundle.putString("accessToken", response.headers().get("Authorization"))
                    //SharedPreference 추가하였습니다
                    val SharedPreferences = activity?.getSharedPreferences("access", MODE_PRIVATE)
                    val prefEdit = SharedPreferences?.edit()
                    var header = response.headers().get("Authorization").toString()
                    prefEdit?.putString("accessToken",header)
                    prefEdit?.apply()
                }

                else {
                    Log.d("로그인결과2", "통신결과"+response.code().toString())
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.d("로그인결과3", "통신결과: $t")
            }
        })

        return accessToken
    }
}