package com.feup.cmov.acme_client.screens.main_menu.cart

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import com.feup.cmov.acme_client.database.models.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.feup.cmov.acme_client.R
import com.feup.cmov.acme_client.databinding.FragmentCartBinding
import com.feup.cmov.acme_client.databinding.FragmentStoreBinding
import com.feup.cmov.acme_client.screens.main_menu.CartViewModel
import com.feup.cmov.acme_client.screens.main_menu.MainMenuFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CartFragment() : Fragment(), CartHandler {

    private val viewModel: CartViewModel by viewModels()
    lateinit var binding: FragmentCartBinding
    private var adapter: CartItemAdapter = CartItemAdapter()
    private lateinit var mainMenu: MainMenuFragment

    constructor(mainMenu: MainMenuFragment): this() {
        this.mainMenu = mainMenu
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_cart, container, false
        )

        // Setting binding params
        binding.viewModel = viewModel
        binding.handler = this

        val toolbar = binding.checkoutTopAppBar

        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24)
        toolbar.setNavigationOnClickListener{
            activity?.onBackPressed();
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.mainMenuFragmentItemsList.adapter = adapter
    }

    fun setAdapterData(cartItems: List<MenuItem>) {
        adapter.data = cartItems
    }

}