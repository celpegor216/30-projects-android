package fastcampus.aop.pjt14_c2c_marketplace.chatlist

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import fastcampus.aop.pjt14_c2c_marketplace.DBKey
import fastcampus.aop.pjt14_c2c_marketplace.DBKey.Companion.CHILD_CHAT
import fastcampus.aop.pjt14_c2c_marketplace.R
import fastcampus.aop.pjt14_c2c_marketplace.chatdetail.ChatRoomActivity
import fastcampus.aop.pjt14_c2c_marketplace.databinding.FragmentChatlistBinding

class ChatListFragment: Fragment(R.layout.fragment_chatlist) {

    private lateinit var binding: FragmentChatlistBinding
    private lateinit var chatListAdapter: ChatListAdapter
    private val chatRoomList = mutableListOf<ChatListItem>()

    private val auth: FirebaseAuth by lazy {
        Firebase.auth
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentChatlistBinding.bind(view)

        chatListAdapter = ChatListAdapter(onItemClicked = { chatRoom ->
            context?.let {
                val intent = Intent(it, ChatRoomActivity::class.java)
                intent.putExtra("chatKey", chatRoom.key)
                startActivity(intent)
            }
        })

        chatRoomList.clear()

        binding.chatListRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.chatListRecyclerView.adapter = chatListAdapter

        if (auth.currentUser == null) {
            return
        }

        val chatDB = Firebase.database.reference.child(DBKey.DB_USERS).child(auth.currentUser!!.uid).child(CHILD_CHAT)

        // 한 번만 실행(데이터가 바뀔 때마다 실행되는 것 아님)
        chatDB.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach {
                    val model = it.getValue(ChatListItem::class.java)
                    model ?: return

                    chatRoomList.add(model)
                }

                chatListAdapter.submitList(chatRoomList)
                chatListAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    override fun onResume() {
        super.onResume()

        chatListAdapter.notifyDataSetChanged()
    }
}