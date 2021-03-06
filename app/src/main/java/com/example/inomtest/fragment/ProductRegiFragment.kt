package com.example.inomtest.fragment

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.inomtest.R
import com.example.inomtest.dataClass.ItemData
import com.example.inomtest.dataClass.ResponseImgURL
import com.example.inomtest.databinding.FragmentProductRegiBinding
import com.example.inomtest.network.InomApi
import com.example.inomtest.recyclerview.RegiRecyclerAdapter
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ProductRegiFragment : Fragment() {
    private var _binding: FragmentProductRegiBinding? = null
    private val binding get() = _binding!!

    private lateinit var accessToken: String

    var itemlist = ArrayList<Uri>()
    private lateinit var recyclerAdapter: RegiRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        accessToken = arguments?.getString("accessToken").toString()
    }

    private fun setupSpinnerMajor() {
        val items = resources.getStringArray(R.array.spinner_major)
        val adapter1 =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, items)
        _binding?.regiMajor?.adapter = adapter1

        setupSpinnerMajorHandler()
    }

    private fun setupSpinnerMajorHandler() {
        _binding?.regiMajor?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                Log.d("??????????????????", _binding?.regiMajor?.getItemAtPosition((position)).toString())
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                Log.d("??????????????????", "???????????? ???????????? ??????.")
            }

        }
    }

    private fun setupSpinnerCategory() {
        val items = resources.getStringArray(R.array.spineer_category)
        val apapter2 =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, items)
        _binding?.regiCategory?.adapter = apapter2

        setupSpinnerCategoryHandler()
    }

    private fun setupSpinnerCategoryHandler() {
        _binding?.regiCategory?.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    Log.d(
                        "?????????????????????",
                        _binding?.regiCategory?.getItemAtPosition((position)).toString()
                    )
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                    Log.d("?????????????????????", "???????????? ???????????? ??????.")
                }

            }
    }

    private fun kt_multi_img_intentSetting() {
        // ????????? ???????????? ??????
        _binding?.regiPicture?.setOnClickListener {
            var intent = Intent(Intent.ACTION_PICK)
            intent.data = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            intent.action = Intent.ACTION_GET_CONTENT

            startActivityForResult(intent, 200)
        }

        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        _binding?.regiRecyclerview?.layoutManager = layoutManager
        recyclerAdapter = RegiRecyclerAdapter(itemlist, requireContext())
        _binding?.regiRecyclerview?.adapter = recyclerAdapter
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


        if (resultCode == RESULT_OK && requestCode == 200) {
            itemlist.clear()

            if (data?.clipData != null) { // ?????? ????????? ????????? ??????
                val count = data.clipData!!.itemCount
                if (count > 10) {
                    Toast.makeText(requireContext(), "????????? 10????????? ?????? ???????????????.", Toast.LENGTH_LONG)
                    return
                }
                for (i in 0 until count) {
                    val imageUri = data.clipData!!.getItemAt(i).uri
                    itemlist.add(imageUri)
                }

            } else { // ?????? ??????
                data?.data?.let { uri ->
                    val imageUri: Uri? = data?.data
                    if (imageUri != null) {
                        itemlist.add(imageUri)
                    }
                }
            }
            recyclerAdapter.notifyDataSetChanged()


            // TODO commit ???????????? ?????? ????????? ?????? - ????????? ??????
            // ????????? ?????? ?????? ??????
//            uploadImageToS3(accessToken, itemlist)
        }

    }

//    fun uploadImageToS3(accessToken: String, arrayListUri: ArrayList<Uri>) {
//
//        var files : ArrayList<MultipartBody.Part> = ArrayList<MultipartBody.Part>()
//
//        // ?????? ???????????? ????????? ?????? 'arrayListUri'
//        for (i: Int in 0 until arrayListUri.count()){
//            var fileBody = RequestBody.create("image/jpeg".toMediaTypeOrNull(), arrayListUri.get(i).toString())
//            var fileName = "?????????$i.png"
//            var filePart = MultipartBody.Part.createFormData("images", fileName, fileBody)
//
//            files.add(filePart)
//        }
//
//        val call = InomApi.createApi().createImgURL(accessToken, files)
//
//        call.enqueue(object : Callback<ResponseImgURL> {
//            override fun onResponse(
//                call: Call<ResponseImgURL>,
//                response: Response<ResponseImgURL>
//            ) {
//                if (response.isSuccessful) {
//                    Log.d("?????????S3???????????????", "????????????"+response.code().toString())
//
//                    Log.d("??????????????????URI", response.body()?.imageUrls?.get(0).toString())
//                }
//
//                else {
//                    Log.d("?????????S3???????????????", "????????????"+response.code().toString())
//                }
//            }
//
//            override fun onFailure(call: Call<ResponseImgURL>, t: Throwable) {
//                Log.d("????????????????????????", "????????????: $t")
//            }
//        })
//    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProductRegiBinding.inflate(inflater, container, false)

        setupSpinnerMajor()
        setupSpinnerCategory()

        kt_multi_img_intentSetting()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.regiBackButton.setOnClickListener {
            it.findNavController().navigate(R.id.action_productRegiFragment_to_homeFragment)
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ProductRegiFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProductRegiFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}