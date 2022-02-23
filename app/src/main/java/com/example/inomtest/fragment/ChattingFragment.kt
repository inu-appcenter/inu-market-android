package com.example.inomtest.fragment

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.inomtest.R
import com.example.inomtest.dataClass.ChatModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import java.util.*
import java.util.Collections.reverseOrder
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class ChattingFragment : Fragment() {
    companion object{
        fun newInstance() : ChattingFragment {
            return ChattingFragment()
        }
    }
    private val fireDatabase = FirebaseDatabase.getInstance().reference

    //메모리에 올라갔을 때
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    //프레그먼트를 포함하고 있는 액티비티에 붙었을 때
    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    //뷰가 생성되었을 때
    //프레그먼트와 레이아웃을 연결시켜주는 부분
    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_chatting, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.chattRecycler)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = RecyclerViewAdapter()

        return view
    }

    //채팅방 리싸이클러뷰어댑터
    inner class RecyclerViewAdapter : RecyclerView.Adapter<RecyclerViewAdapter.CustomViewHolder>() {

        private val chatModel = ArrayList<ChatModel>()
        private var uid : String? = null
        private val destinationUsers : ArrayList<String> = arrayListOf()

        init {
           // uid = Firebase.auth.currentUser?.uid.toString()
            println(uid)
//아직 채팅방 생성하지 못해서 아래 코드는 오류가 납니다
//            fireDatabase.child("chatrooms").orderByChild("users/$uid").equalTo(true).addListenerForSingleValueEvent(object : ValueEventListener{
//                override fun onCancelled(error: DatabaseError) {
//                }
//                override fun onDataChange(snapshot: DataSnapshot) {
//                    chatModel.clear()
//                    for(data in snapshot.children){
//                        chatModel.add(data.getValue<ChatModel>()!!)
//                        println(data)
//                    }
//                    notifyDataSetChanged()
//                }
//            })
        }
        //채팅방리싸이클러뷰 뷰홀더
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {

            return CustomViewHolder(LayoutInflater.from(context).inflate(R.layout.item_chat, parent, false))
        }

        inner class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val imageView: ImageView = itemView.findViewById(R.id.chat_item_imageview)
            val textView_title : TextView = itemView.findViewById(R.id.chat_textview_title)
            val textView_lastMessage : TextView = itemView.findViewById(R.id.chat_item_textview_lastmessage)
        }

        override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
            var destinationUid: String? = null
            //채팅방에 있는 유저 모두 체크
            for (user in chatModel[position].users.keys) {
                if (!user.equals(uid)) {
                    destinationUid = user
                    destinationUsers.add(destinationUid)
                }
            }
            fireDatabase.child("users").child("$destinationUid").addListenerForSingleValueEvent(object :
                ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                }
                override fun onDataChange(snapshot: DataSnapshot) {
                    //친구목록불러오기
//                    val friend = snapshot.getValue<Friend>()
//                    Glide.with(holder.itemView.context).load(friend?.profileImageUrl)
//                        .apply(RequestOptions().circleCrop())
//                        .into(holder.imageView)
//                    holder.textView_title.text = friend?.name
                }
            })
            //메세지 내림차순 정렬 후 마지막 메세지의 키값을 가져옴
            val commentMap = TreeMap<String, ChatModel.Comment>(reverseOrder())
            commentMap.putAll(chatModel[position].comments)
            val lastMessageKey = commentMap.keys.toTypedArray()[0]
            holder.textView_lastMessage.text = chatModel[position].comments[lastMessageKey]?.message

            //채팅창 선책 시 이동_리싸이클러뷰 클릭 리스터 수정해야함
//            holder.itemView.setOnClickListener {
//                val intent = Intent(context, MessageActivity::class.java)
//                intent.putExtra("destinationUid", destinationUsers[position])
//                context?.startActivity(intent)
//            }
        }
        override fun getItemCount(): Int {
            return chatModel.size
        }
    }
}