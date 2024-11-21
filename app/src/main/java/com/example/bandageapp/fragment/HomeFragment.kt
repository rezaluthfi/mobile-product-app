package com.example.bandageapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bandageapp.model.Product
import com.example.bandageapp.databinding.FragmentHomeBinding
import com.example.bandageapp.adapter.ProductAdapter
import com.example.bandageapp.R
import com.google.firebase.database.*

class HomeFragment : Fragment(R.layout.fragment_home) {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var database: DatabaseReference
    private lateinit var productList: MutableList<Product>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        database = FirebaseDatabase.getInstance().reference
        productList = mutableListOf()

        // Setup fetchProductData function
        fetchProductData()

        return binding.root
    }

    // Fetch product data from Firebase
    private fun fetchProductData() {
        database.child("products").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                productList.clear()
                if (snapshot.exists()) {
                    for (productSnapshot in snapshot.children) {
                        // Map the snapshot to the Product model class
                        val product = productSnapshot.getValue(Product::class.java)
                        if (product != null) {
                            productList.add(product)
                        }
                    }
                    setupRecyclerView()
                } else {
                    Toast.makeText(activity, "No products found", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(activity, "Error fetching data: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // Setup RecyclerView to display products
    private fun setupRecyclerView() {
        val adapter = ProductAdapter(productList) { product ->
            val bundle = Bundle()
            bundle.putSerializable("product", product)
            bundle.putSerializable("productList", ArrayList(productList))
            val detailFragment = DetailProductFragment()
            detailFragment.arguments = bundle
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.container, detailFragment)
                .addToBackStack(null)
                .commit()
        }

        binding.rvProducts.layoutManager = LinearLayoutManager(requireContext())
        binding.rvProducts.adapter = adapter
        adapter.notifyDataSetChanged()
    }

}
