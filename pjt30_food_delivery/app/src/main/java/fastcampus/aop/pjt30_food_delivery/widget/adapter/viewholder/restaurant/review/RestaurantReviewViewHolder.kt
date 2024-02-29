package fastcampus.aop.pjt30_food_delivery.widget.adapter.viewholder.restaurant.review

import androidx.core.view.isGone
import androidx.core.view.isVisible
import fastcampus.aop.pjt30_food_delivery.databinding.ViewholderRestaurantReviewBinding
import fastcampus.aop.pjt30_food_delivery.extension.clear
import fastcampus.aop.pjt30_food_delivery.extension.load
import fastcampus.aop.pjt30_food_delivery.model.restaurant.review.RestaurantReviewModel
import fastcampus.aop.pjt30_food_delivery.screen.base.BaseViewModel
import fastcampus.aop.pjt30_food_delivery.util.provider.ResourcesProvider
import fastcampus.aop.pjt30_food_delivery.widget.adapter.listener.AdapterListener
import fastcampus.aop.pjt30_food_delivery.widget.adapter.viewholder.ModelViewHolder

class RestaurantReviewViewHolder(
    private val binding: ViewholderRestaurantReviewBinding,
    viewModel: BaseViewModel,
    resourcesProvider: ResourcesProvider
): ModelViewHolder<RestaurantReviewModel>(binding, viewModel, resourcesProvider) {

    override fun reset() = with(binding) {
        reviewThumbnailImageView.clear()
        reviewThumbnailImageView.isGone = true
    }

    override fun bindData(model: RestaurantReviewModel) = with(binding) {
        super.bindData(model)

        if (model.thumbnailImageUri != null) {
            reviewThumbnailImageView.isVisible = true
            reviewThumbnailImageView.load(model.thumbnailImageUri.toString(), 24f)
        } else {
            reviewThumbnailImageView.isGone = true
        }
        reviewTitleTextView.text = model.title
        reviewContentTextView.text = model.description
        ratingBar.rating = model.grade.toFloat()
    }

    override fun bindViews(model: RestaurantReviewModel, adapterListener: AdapterListener) = Unit
}