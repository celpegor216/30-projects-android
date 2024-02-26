package fastcampus.aop.pjt30_food_delivery.model

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil

// RecyclerView에서 diffUtil을 기반으로 데이터 변경 시
// 자동으로 UI를 변경하기 위해 필요한 요소들을 담은 Model
abstract class Model(
    open val id: Long,
    open val type: CellType
) {
    companion object {
        // id와 내용을 기반으로 동일 여부 판단하여 notify
        val DIFF_CALLBACK: DiffUtil.ItemCallback<Model> = object : DiffUtil.ItemCallback<Model>() {
            override fun areItemsTheSame(oldItem: Model, newItem: Model): Boolean {
                return oldItem.id == newItem.id && oldItem.type == newItem.type
            }

            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(oldItem: Model, newItem: Model): Boolean {
                return oldItem == newItem
            }
        }
    }
}