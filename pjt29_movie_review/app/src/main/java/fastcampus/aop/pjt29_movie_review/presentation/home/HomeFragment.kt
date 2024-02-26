package fastcampus.aop.pjt29_movie_review.presentation.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import fastcampus.aop.pjt29_movie_review.databinding.FragmentHomeBinding
import fastcampus.aop.pjt29_movie_review.domain.model.FeaturedMovie
import fastcampus.aop.pjt29_movie_review.domain.model.Movie
import fastcampus.aop.pjt29_movie_review.extension.dip
import fastcampus.aop.pjt29_movie_review.extension.toGone
import fastcampus.aop.pjt29_movie_review.extension.toVisible
import fastcampus.aop.pjt29_movie_review.presentation.home.HomeAdapter.Companion.ITEM_VIEW_TYPE_FEATURED
import fastcampus.aop.pjt29_movie_review.presentation.home.HomeAdapter.Companion.ITEM_VIEW_TYPE_SECTION_HEADER
import org.koin.android.ext.android.inject
import org.koin.androidx.scope.ScopeFragment

class HomeFragment : ScopeFragment(), HomeContract.View {

    private var binding: FragmentHomeBinding? = null

    override val presenter: HomeContract.Presenter by inject()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = FragmentHomeBinding.inflate(inflater)
        .also { binding = it }
        .root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        bindViews()
        presenter.onViewCreated()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.onDestroyView()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDestroy()
    }

    override fun showLoadingIndicator() {
        binding?.progressBar?.toVisible()
    }

    override fun hideLoadingIndicator() {
        binding?.progressBar?.toGone()
    }

    override fun showMovies(featuredMovie: FeaturedMovie?, movies: List<Movie>) {
        binding?.recyclerView?.toVisible()
        binding?.errorDescriptionTextView?.toGone()
        (binding?.recyclerView?.adapter as? HomeAdapter)?.run {
            addData(featuredMovie, movies)
            notifyDataSetChanged()
        }

    }

    override fun showErrorDescription(message: String) {
        binding?.recyclerView?.toGone()
        binding?.errorDescriptionTextView?.toVisible()
        binding?.errorDescriptionTextView?.text = message
    }

    private fun initViews() {
        binding?.recyclerView?.apply {
            val gridLayoutManager = createGridLayoutManager()
            layoutManager = gridLayoutManager
            adapter = HomeAdapter()
            addItemDecoration(GridSpacingItemDecoration(gridLayoutManager.spanCount, dip(6F)))
        }
    }

    private fun bindViews() {
        (binding?.recyclerView?.adapter as? HomeAdapter)?.apply {
            onMovieClickListener = { movie ->
                val action = HomeFragmentDirections.toReviewsAction(movie)
                findNavController().navigate(action)
            }
        }
    }

    private fun RecyclerView.createGridLayoutManager(): GridLayoutManager =
        GridLayoutManager(context, 3, RecyclerView.VERTICAL, false).apply {
            // 섹션 헤더, 추천 영화는 한 줄을 모두 차지하고
            // 나머지는 한 칸을 차지하도록 지정
            spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int =
                    when (adapter?.getItemViewType(position)) {
                        ITEM_VIEW_TYPE_SECTION_HEADER, ITEM_VIEW_TYPE_FEATURED -> {
                            spanCount
                        }

                        else -> {
                            1
                        }
                    }

            }
        }
}