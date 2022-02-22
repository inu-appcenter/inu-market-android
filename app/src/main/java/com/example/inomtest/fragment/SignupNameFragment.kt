package com.example.inomtest.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.example.inomtest.R
import com.example.inomtest.dataClass.LoginData
import com.example.inomtest.databinding.FragmentSignupNameBinding
import com.example.inomtest.network.InomApi
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignupNameFragment : Fragment() {
    private var _binding: FragmentSignupNameBinding? = null
    private val binding get() = _binding!!
    lateinit var navController: NavController

    val bundle = Bundle()
    var inuId : String = ""
    var password : String = ""
    var nickname : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        inuId = arguments?.getString("inuID").toString()
        password = arguments?.getString("password").toString()

        Log.d("아이디", "$inuId")
        Log.d("비밀번호", "$password")
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSignupNameBinding.inflate(inflater, container, false)

        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.signupNameBtn.setOnClickListener{
            nickname = binding.signupNameEdit.toString()

            login()

            it.findNavController().navigate(R.id.action_signupNameFragment_to_signupFinishFragment)
        }
    }


    companion object {
        @JvmStatic
        fun newInstance() =
            SignupNameFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun login() {
        var LoginData = LoginData()
        LoginData.inuId = inuId
        LoginData.password = password
        LoginData.pushToken = "pushToken"


        val paramObject = JSONObject()
        paramObject.put("inuId", inuId)
        paramObject.put("password", password)
        paramObject.put("pushToken", "pushToken")
        //val request = RequestBody.create(MediaType.parse("application/json"),paramObject.toString())
        val request = RequestBody.create("application/json"?.toMediaTypeOrNull(),paramObject.toString())

        val call = InomApi.createApi().signUp(request)

        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Log.d("로그인결과1", "통신결과"+response.code().toString())
                }

                else {
                    Log.d("로그인결과2", "통신결과"+response.code().toString())
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.d("로그인결과3", "통신결과: $t")
            }
        })
    }
}