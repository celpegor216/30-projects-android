package fastcampus.aop.pjt29_movie_review.presentation.home

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration

class GridSpacingItemDecoration(
    private val spanCount: Int,
    private val spacing: Int
) : ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val adapterPosition = parent.getChildAdapterPosition(view)
        val gridLayoutManager = parent.layoutManager as GridLayoutManager
        val spanSize = gridLayoutManager.spanSizeLookup.getSpanSize(adapterPosition)

        if (spanSize == spanCount) {
            outRect.left = spacing
            outRect.right = spacing
            outRect.top = spacing
            outRect.bottom = spacing
            return
        }

        // 한 칸만을 차지하는 경우 여백을 다르게 지정해야 함
        // 이때 어느 열에 있는지 확인할 수 있는 방법이 spanIndex
        val column = (view.layoutParams as GridLayoutManager.LayoutParams).spanIndex
        val itemHorizontalSpacing = ((spanCount + 1) * spacing) / spanCount.toFloat()

        when (column) {
            0 -> {
                outRect.left = spacing
                outRect.right = (itemHorizontalSpacing - spacing).toInt()
            }

            (spanCount - 1) -> {
                outRect.left = (itemHorizontalSpacing - spacing).toInt()
                outRect.right = spacing
            }

            else -> {
                outRect.left = (itemHorizontalSpacing / 2).toInt()
                outRect.right = (itemHorizontalSpacing / 2).toInt()
            }
        }
        outRect.top = spacing
        outRect.bottom = spacing
    }
}