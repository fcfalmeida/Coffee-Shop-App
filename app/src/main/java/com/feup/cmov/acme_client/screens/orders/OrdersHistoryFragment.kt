package com.feup.cmov.acme_client.screens.orders

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import com.feup.cmov.acme_client.R
import com.feup.cmov.acme_client.databinding.FragmentOrdersHistoryBinding
import com.feup.cmov.acme_client.screens.main_menu.store.MenuItemAdapter
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class OrdersHistoryFragment : Fragment(), OrdersHistoryHandler {

    private val viewModel: OrdersHistoryViewModel by viewModels()
    lateinit var binding: FragmentOrdersHistoryBinding
    var adapter: OrderItemAdapter =  OrderItemAdapter(this)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_orders_history, container, false
        )

        binding.viewModel = viewModel
        binding.handler = this

        viewModel.getOrders().observe(viewLifecycleOwner) { orders ->
            Log.e("ABC", orders.size.toString())
            adapter.data = orders
        };

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.orderHistoryOrdersItems.adapter = adapter
    }

}