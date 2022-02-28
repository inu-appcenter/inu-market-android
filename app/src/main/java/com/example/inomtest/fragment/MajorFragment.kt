package com.example.inomtest.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.inomtest.R
import com.example.inomtest.dataClass.*
import com.example.inomtest.databinding.FragmentMajorBinding
import com.example.inomtest.databinding.ItemMajorBinding
import okhttp3.internal.cache2.Relay.Companion.edit
import kotlin.collections.ArrayList


class MajorFragment : Fragment() {
    private var _binding: FragmentMajorBinding? = null
    private val binding get() = _binding!!
    private lateinit var recyclerViewAdapter:RecyclerViewAdapter
    private lateinit var recyclerViewAdapter1:RecyclerViewAdapterM
    private lateinit var accessToken: String
    private var collageList = arrayListOf(
        majorList("인문대학",-1),
        majorList("자연과학대학",-2),
        majorList("사회과학대학",-3),
        majorList("글로벌정경대학",-4),
        majorList("공과대학",-5),
        majorList("정보기술대학",-6),
        majorList("경영대학",-7),
        majorList("예술체육대학",-8),
        majorList("사범대학",-9),
        majorList("도시과학대학",-10),
        majorList("생명과학기술대학",-11),
        majorList("동북아국제통상학부",-12),
        majorList("법학부",60)
    )
    private var liberalArtsList = arrayListOf(
        majorDepartList("국어국문학과",1),
        majorDepartList("영어영문학과",2),
        majorDepartList("독어독문학과",3),
        majorDepartList("불어불문학과",4),
        majorDepartList("일어일문학과",5),
        majorDepartList("중어중국학과",6),
    )
    private var NaturalScienceList = arrayListOf(
        majorDepartList("수학과",7),
        majorDepartList("물리학과",8),
        majorDepartList("화학과",9),
        majorDepartList("패션산업학과",10),
        majorDepartList("해양학과",11),
    )
    private var socialScienceList = arrayListOf(
        majorDepartList("사회복지학과",12),
        majorDepartList("신문방송학과",13),
        majorDepartList("문헌정보학과",14),
        majorDepartList("창의인재개발학과",15)
    )
    private var globalEconomicsList = arrayListOf(
        majorDepartList("행정학과",16),
        majorDepartList("정치외교",17),
        majorDepartList("경제학과",18),
        majorDepartList("무역학과",19),
        majorDepartList("소비자학과",20),
    )
    private var engineeringList = arrayListOf(
        majorDepartList("기계공학과",21),
        majorDepartList("메카트로닉스공학과",22),
        majorDepartList("전기공학과",23),
        majorDepartList("전자공학과",24),
        majorDepartList("산업경영공학과",25),
        majorDepartList("신소재공학과",26),
        majorDepartList("안전공학과",27),
        majorDepartList("에너지화학공학과",28)
    )
    private var informationTechnologyList = arrayListOf(
        majorDepartList("컴퓨터공학부",29),
        majorDepartList("정보통신공학과",30),
        majorDepartList("임베디드시스템공학과",31)
    )
    private var businessList = arrayListOf(
        majorDepartList("경영학부",32),
        majorDepartList("세무회계학과",33),
        majorDepartList("테크노경영학과",34)
    )
    private var artsPhysicalList = arrayListOf(
        majorDepartList("조형예술학부",35),
        majorDepartList("디자인학부",36),
        majorDepartList("공연예술학과",37),
        majorDepartList("체육학부",38),
        majorDepartList("운동건강학부",39),
    )
    private var educationList = arrayListOf(
        majorDepartList("국어교육과",40),
        majorDepartList("영어교육과",41),
        majorDepartList("일어교육과",42),
        majorDepartList("수학교육과",43),
        majorDepartList("체육교육과",44),
        majorDepartList("유아교육과",45),
        majorDepartList("역사교육과",46),
        majorDepartList("윤리교육과",47)
    )
    private var urbanScienceList = arrayListOf(
        majorDepartList("도시행정학과",48),
        majorDepartList("도시공학과",49),
        majorDepartList("건설환경공학과",50),
        majorDepartList("건축공학과",51),
        majorDepartList("환경공학과",52),
        majorDepartList("도시건축학과",53)
    )
    private var bioScienceList = arrayListOf(
        majorDepartList("분자의생명학과",54),
        majorDepartList("생명과학과",55),
        majorDepartList("생명공학과",56),
        majorDepartList("나노바이오학과",57)
    )
    private var northeastAsianList = arrayListOf(
        majorDepartList("동북아통상전공",58),
        majorDepartList("한국통상전공",59)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        accessToken = arguments?.getString("accessToken").toString()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMajorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        collageSetting()
        binding.backBtn.setOnClickListener{
            it.findNavController().navigate(R.id.action_majorFragment_to_homeFragment)
        }
    }
    fun collageSetting(){
        binding.categoryTop.text = "학과 카테고리"
        recyclerViewAdapter = RecyclerViewAdapter()
        recyclerViewAdapter.submitListC(collageList)
        binding.majorRecycler.adapter = recyclerViewAdapter
        binding.majorRecycler.layoutManager = LinearLayoutManager(context)
    }
    fun collageClicked(name:String,id:Int){
        binding.categoryTop.text = name
        recyclerViewAdapter1 = RecyclerViewAdapterM()
        when (id){
            -1 -> recyclerViewAdapter1.submitListM(liberalArtsList)
            -2 -> recyclerViewAdapter1.submitListM(NaturalScienceList)
            -3 -> recyclerViewAdapter1.submitListM(socialScienceList)
            -4 -> recyclerViewAdapter1.submitListM(globalEconomicsList)
            -5 -> recyclerViewAdapter1.submitListM(engineeringList)
            -6 -> recyclerViewAdapter1.submitListM(informationTechnologyList)
            -7 -> recyclerViewAdapter1.submitListM(businessList)
            -8 -> recyclerViewAdapter1.submitListM(artsPhysicalList)
            -9 -> recyclerViewAdapter1.submitListM(educationList)
            -10 -> recyclerViewAdapter1.submitListM(urbanScienceList)
            -11 -> recyclerViewAdapter1.submitListM(bioScienceList)
            -12 -> recyclerViewAdapter1.submitListM(northeastAsianList)
        }
        //recyclerViewAdapter1.submitListM()
        binding.majorRecycler.adapter = recyclerViewAdapter1
        binding.majorRecycler.layoutManager = LinearLayoutManager(context)
        binding.backBtn.setOnClickListener {
            collageSetting()
        }
    }
    //단과대 리싸이클러뷰어댑터
    inner class RecyclerViewAdapter : RecyclerView.Adapter<RecyclerViewAdapter.CustomViewHolder>() {

        private var majorList = ArrayList<majorList>()

        //단과대 리싸이클러뷰 뷰홀더
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
            val binding = ItemMajorBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return CustomViewHolder(binding)
        }

        inner class CustomViewHolder(val binding: ItemMajorBinding) :
            RecyclerView.ViewHolder(binding.root) {
            fun bind(rArray : majorList) {
                with(binding) {
                    textView.text = rArray.college
                }
            }
        }

        override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
            if (holder is RecyclerViewAdapter.CustomViewHolder) {
                holder.bind(this.majorList[position])
                holder.itemView.setOnClickListener {
                    if (majorList[position].id ==60){
                        it.findNavController().navigate(R.id.action_majorFragment_to_homeFragment)
                        val SharedPreferences = activity?.getSharedPreferences("access", Context.MODE_PRIVATE)
                        val prefEdit = SharedPreferences?.edit()
                        var major = majorList[position].id
                        prefEdit?.putInt("major",major)
                        prefEdit?.apply()
                    }
                    collageClicked(majorList[position].college,majorList[position].id)
                }
            }
        }
        override fun getItemCount(): Int {
            return majorList.size
        }
        fun submitListC(mList1: ArrayList<majorList>){
            this.majorList = mList1
        }
    }
    //학과 리싸이클러뷰어댑터
    inner class RecyclerViewAdapterM : RecyclerView.Adapter<RecyclerViewAdapterM.CustomViewHolder>() {

        private var majorListM = ArrayList<majorDepartList>()

        //학과 리싸이클러뷰 뷰홀더
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
            val binding = ItemMajorBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return CustomViewHolder(binding)
        }

        inner class CustomViewHolder(val binding: ItemMajorBinding) :
            RecyclerView.ViewHolder(binding.root) {
            fun bind(rArray : majorDepartList) {
                with(binding) {
                    textView.text = rArray.name
                }
            }
        }

        override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
            if (holder is RecyclerViewAdapterM.CustomViewHolder) {
                holder.bind(this.majorListM[position])
                holder.itemView.setOnClickListener {
                    it.findNavController().navigate(R.id.action_majorFragment_to_homeFragment)
                    val SharedPreferences = activity?.getSharedPreferences("access", Context.MODE_PRIVATE)
                    val prefEdit = SharedPreferences?.edit()
                    var major = majorListM[position].id
                    prefEdit?.putInt("major",major)
                    prefEdit?.apply()
                }
            }
        }
        override fun getItemCount(): Int {
            return majorListM.size
        }
        fun submitListM(mList: ArrayList<majorDepartList>){
            this.majorListM = mList
        }
    }

}