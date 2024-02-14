package fastcampus.aop.pjt24_online_store.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import fastcampus.aop.pjt24_online_store.data.entity.product.ProductEntity
import fastcampus.aop.pjt24_online_store.databinding.ViewholderProductItemBinding
import fastcampus.aop.pjt24_online_store.extensions.loadCenterCrop

class ProductListAdapter: RecyclerView.Adapter<ProductListAdapter.ProductItemViewHolder>() {

    private var productList: List<ProductEntity> = listOf()
    private lateinit var productItemClickListener: (ProductEntity) -> Unit

    inner class ProductItemViewHolder(
        private val binding: ViewholderProductItemBinding,
        val productItemClickListener: (ProductEntity) -> Unit
    ): RecyclerView.ViewHolder(binding.root) {

        fun bind(data: ProductEntity) = with(binding) {
            productNameTextView.text = data.productName
            productPriceTextView.text = "${data.productPrice}원"
            productImageView.loadCenterCrop(data.productImage, 8f)

            root.setOnClickListener {
                productItemClickListener(data)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductItemViewHolder {
        return ProductItemViewHolder(ViewholderProductItemBinding.inflate(LayoutInflater.from(parent.context), parent, false), productItemClickListener)
    }

    override fun onBindViewHolder(holder: ProductItemViewHolder, position: Int) {
        holder.bind(productList[position])
    }

    override fun getItemCount(): Int = productList.size

    fun setProductList(
        productList: List<ProductEntity>,
        productItemClickListener: (ProductEntity) -> Unit = {}
    ) {
        this.productList = productList
        this.productItemClickListener = productItemClickListener
        notifyDataSetChanged()
    }
}