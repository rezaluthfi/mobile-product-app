package com.example.bandageapp.fragment

import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.bandageapp.R
import com.example.bandageapp.adapter.ColorAdapter
import com.example.bandageapp.adapter.ImageSliderAdapter
import com.example.bandageapp.adapter.ProductAdapter
import com.example.bandageapp.adapter.ThumbnailAdapter
import com.example.bandageapp.databinding.FragmentProductDetailBinding
import com.example.bandageapp.model.Product

class DetailProductFragment : Fragment(R.layout.fragment_product_detail) {

    private lateinit var binding: FragmentProductDetailBinding
    private var product: Product? = null
    private lateinit var dots: ArrayList<TextView>
    private var productList: List<Product> = emptyList()
    private lateinit var productAdapter: ProductAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProductDetailBinding.inflate(inflater, container, false)

        // Retrieve product data from arguments
        retrieveArguments()

        // Setup product details
        product?.let {
            displayProductDetails(it)
            setupProductImages(it)
            setupProductColors(it)
            setupThumbnails(it)
            setupProductListRecyclerView()
        }

        return binding.root
    }

    private fun retrieveArguments() {
        product = arguments?.getSerializable("product") as? Product
        productList = arguments?.getSerializable("productList") as? List<Product> ?: emptyList()
    }

    private fun displayProductDetails(product: Product) {
        binding.tvProductName.text = product.details?.name
        binding.tvDescription.text = product.details?.description
        binding.tvPrice.text = "$${product.new_price}"
        binding.tvReviews.text = "${product.details?.reviews} Reviews"
        binding.tvAvailability.text = if (product.details?.price != null) "In Stock" else "Out of Stock"
    }

    private fun setupProductImages(product: Product) {
        val imageSliderAdapter = ImageSliderAdapter(product.images ?: emptyList()) { position ->
            binding.viewPager.setCurrentItem(position, true)
            updateDots(position)
        }
        binding.viewPager.adapter = imageSliderAdapter

        setupImageIndicator(product.images?.size ?: 0)

        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                updateDots(position)
            }
        })
    }

    private fun setupImageIndicator(count: Int) {
        binding.dotsIndicator.removeAllViews()
        dots = ArrayList()

        for (i in 0 until count) {
            val dot = TextView(requireContext()).apply {
                text = Html.fromHtml("&#8226;", Html.FROM_HTML_MODE_LEGACY)
                textSize = 24F
                setTextColor(resources.getColor(R.color.light_grey))
            }
            dots.add(dot)
            binding.dotsIndicator.addView(dot)
        }

        if (dots.isNotEmpty()) {
            dots[0].setTextColor(resources.getColor(R.color.blue))
        }
    }

    private fun updateDots(position: Int) {
        dots.forEachIndexed { index, dot ->
            dot.setTextColor(
                if (index == position) resources.getColor(R.color.blue)
                else resources.getColor(R.color.light_grey)
            )
        }
    }

    private fun setupProductColors(product: Product) {
        val colorsAdapter = ColorAdapter(product.details?.colors ?: emptyList())
        binding.rvColors.apply {
            adapter = colorsAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private fun setupThumbnails(product: Product) {
        val thumbnailsAdapter = ThumbnailAdapter(product.images ?: emptyList()) { position ->
            binding.viewPager.setCurrentItem(position, true)
            updateDots(position)
        }
        binding.rvThumbnails.apply {
            adapter = thumbnailsAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private fun setupProductListRecyclerView() {
        productAdapter = ProductAdapter(productList) { selectedProduct ->
            navigateToProductDetail(selectedProduct)
        }
        binding.rvHomeProducts.apply {
            adapter = productAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun navigateToProductDetail(selectedProduct: Product) {
        val fragment = DetailProductFragment().apply {
            arguments = Bundle().apply {
                putSerializable("product", selectedProduct)
                putSerializable("productList", ArrayList(productList))
            }
        }
        parentFragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
            .addToBackStack(null)
            .commit()
    }
}
