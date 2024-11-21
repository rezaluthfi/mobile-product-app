package com.example.bandageapp.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bandageapp.R
import com.example.bandageapp.adapter.ProductAdapter
import com.example.bandageapp.databinding.FragmentHomeBinding
import com.example.bandageapp.model.Product
import com.google.firebase.database.*

class HomeFragment : Fragment(R.layout.fragment_home) {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var database: DatabaseReference
    private var productList: MutableList<Product> = mutableListOf()
    private var filteredList: MutableList<Product> = mutableListOf()
    private lateinit var adapter: ProductAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        database = FirebaseDatabase.getInstance().reference

        // Inisialisasi RecyclerView dan adapter
        setupRecyclerView()

        // Ambil data produk dari Firebase
        fetchProductData()

        return binding.root
    }

    private fun fetchProductData() {
        database.child("products").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                productList.clear()
                if (snapshot.exists()) {
                    for (productSnapshot in snapshot.children) {
                        val product = productSnapshot.getValue(Product::class.java)
                        if (product != null) {
                            productList.add(product)
                        }
                    }
                    filteredList.clear()
                    filteredList.addAll(productList)
                    adapter.notifyDataSetChanged()
                } else {
                    Toast.makeText(activity, "No products found", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(activity, "Error fetching data: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setupRecyclerView() {
        adapter = ProductAdapter(filteredList) { selectedProduct ->
            navigateToProductDetail(selectedProduct)
        }
        binding.rvProducts.layoutManager = LinearLayoutManager(requireContext())
        binding.rvProducts.adapter = adapter
    }

    private fun navigateToProductDetail(selectedProduct: Product) {
        Log.d("HomeFragment", "Navigating with productList size: ${productList.size}")

        if (productList.isEmpty()) {
            Toast.makeText(requireContext(), "No products available to navigate", Toast.LENGTH_SHORT).show()
            return
        }

        val detailFragment = DetailProductFragment().apply {
            arguments = Bundle().apply {
                putSerializable("product", selectedProduct)
                putSerializable("productList", ArrayList(productList))
            }
        }

        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.container, detailFragment)
            .addToBackStack(null)
            .commit()
    }


    fun filterProducts(query: String) {
        filteredList.clear()
        if (query.isEmpty()) {
            filteredList.addAll(productList)
        } else {
            filteredList.addAll(productList.filter {
                it.details?.name?.contains(query, ignoreCase = true) == true
            })
        }
        binding.tvNoProduct.visibility = if (filteredList.isEmpty()) View.VISIBLE else View.GONE
        adapter.notifyDataSetChanged()
    }
}