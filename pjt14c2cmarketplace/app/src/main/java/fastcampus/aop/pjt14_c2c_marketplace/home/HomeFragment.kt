package fastcampus.aop.pjt14_c2c_marketplace.home

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import fastcampus.aop.pjt14_c2c_marketplace.DBKey.Companion.CHILD_CHAT
import fastcampus.aop.pjt14_c2c_marketplace.DBKey.Companion.DB_ARTICLES
import fastcampus.aop.pjt14_c2c_marketplace.DBKey.Companion.DB_USERS
import fastcampus.aop.pjt14_c2c_marketplace.R
import fastcampus.aop.pjt14_c2c_marketplace.chatlist.ChatListItem
import fastcampus.aop.pjt14_c2c_marketplace.databinding.FragmentHomeBinding

class HomeFragment: Fragment(R.layout.fragment_home) {

    private lateinit var userDB: DatabaseReference
    private lateinit var articleDB: DatabaseReference
    private lateinit var articleAdapter: ArticleAdapter

    private val articleList = mutableListOf<ArticleModel>()
    private val listener = object : ChildEventListener {
        override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
            // ArticleModel 인스턴스 자체를 받아오기
            val articleModel = snapshot.getValue(ArticleModel::class.java)
            articleModel ?: return

            articleList.add(articleModel)
            articleAdapter.submitList(articleList)
        }

        override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}

        override fun onChildRemoved(snapshot: DataSnapshot) {}

        override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}

        override fun onCancelled(error: DatabaseError) {}
    }

    private lateinit var binding: FragmentHomeBinding
    private val auth: FirebaseAuth by lazy {
        Firebase.auth
    }


    // RecyclerView에 adapter를 연결하거나, View의 setOnClickListener를 추가하는 등 이벤트를 처리
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentHomeBinding.bind(view)

        // 중복 생성 방지를 위해 초기화
        articleList.clear()

        userDB = Firebase.database.reference.child(DB_USERS)
        articleDB = Firebase.database.reference.child(DB_ARTICLES)
        articleAdapter = ArticleAdapter(onItemClicked = { articleModel ->
            if (auth.currentUser != null) {
                val currentUser = auth.currentUser!!
                if (currentUser.uid != articleModel.sellerId) {
                    val chatRoom = ChatListItem(
                        currentUser!!.uid,
                        articleModel.sellerId,
                        articleModel.title,
                        System.currentTimeMillis()
                    )

                    userDB.child(currentUser.uid)
                        .child(CHILD_CHAT)
                        .push()
                        .setValue(chatRoom)

                    userDB.child(articleModel.sellerId)
                        .child(CHILD_CHAT)
                        .push()
                        .setValue(chatRoom)

                    Snackbar.make(view, "채팅방이 생성되었습니다. 채팅탭에서 확인해주세요.", Snackbar.LENGTH_LONG).show()
                } else {
                    Snackbar.make(view, "내가 올린 아이템입니다.", Snackbar.LENGTH_LONG).show()
                }
            } else {
                Snackbar.make(view, "로그인이 필요합니다.", Snackbar.LENGTH_LONG).show()
            }
        })

        binding.articleRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.articleRecyclerView.adapter =articleAdapter

        binding.addFloatingButton.setOnClickListener {
            if (auth.currentUser != null) {
                val intent = Intent(requireContext(), AddArticleActivity::class.java)
                startActivity(intent)
            } else {
                Snackbar.make(view, "로그인이 필요합니다.", Snackbar.LENGTH_LONG).show()
            }
        }

        articleDB.addChildEventListener(listener)
    }

    override fun onResume() {
        super.onResume()

        articleAdapter.notifyDataSetChanged()
    }

    override fun onDestroyView() {
        super.onDestroyView()

        articleDB.removeEventListener(listener)
    }
}