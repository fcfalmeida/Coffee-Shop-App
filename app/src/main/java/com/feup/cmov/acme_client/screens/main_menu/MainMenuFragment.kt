package com.feup.cmov.acme_client.screens.main_menu

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.feup.cmov.acme_client.AcmeApplication
import com.feup.cmov.acme_client.R
import com.feup.cmov.acme_client.database.models.MenuItem
import com.feup.cmov.acme_client.databinding.FragmentMainMenuBinding
import com.google.android.material.badge.BadgeDrawable
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainMenuFragment : Fragment(), MainMenuHandler {

    private lateinit var myContext: FragmentActivity
    private val viewModel: MainMenuViewModel by viewModels()
    private val cartViewModel: CartViewModel by viewModels()
    lateinit var binding: FragmentMainMenuBinding


    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        myContext = activity as FragmentActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_main_menu, container, false
        )

        binding.viewModel = viewModel
        binding.cartViewModel = cartViewModel
        binding.handler = this

        //binding.cartButton.visibility = if (totalCartItems > 0) View.VISIBLE else View.GONE

        val bottomNavigation = binding.bottomNavigation

        // Create the fragments
        makeCurrentFragment(viewModel.getCurrentFragment(), true)

        cartViewModel.getTotalCartItems().observe(viewLifecycleOwner, Observer observe@{ totalCartItems ->
            binding.cartButton.visibility = if (totalCartItems > 0) View.VISIBLE else View.GONE
            binding.cartButtonNumberItems.text = totalCartItems.toString()
        })

        val priceStringFormat: String = AcmeApplication.getAppContext().getString(R.string.total_cart_price)
        cartViewModel.getTotalCartPrice().observe(viewLifecycleOwner, Observer observe@{ totalCartPrice ->
            binding.cartButtonTotalPrice.text = String.format(priceStringFormat, totalCartPrice)
        })

        bottomNavigation.setOnNavigationItemSelectedListener { item ->
            val menuItem = bottomNavigation.menu.findItem(item.itemId)
            if(menuItem.itemId != viewModel.getCurrentAction()) {
                menuItem.isChecked = true
                viewModel.setCurrentAction(menuItem.itemId)
                makeCurrentFragment(viewModel.getCurrentFragment(), false)
            }
            false
        }


        return binding.root
    }

    private fun makeCurrentFragment(fragment: Fragment, refresh: Boolean) {
        with(myContext.supportFragmentManager.beginTransaction()) {
            if(refresh) {
                detach(fragment);
                attach(fragment);
            }
            replace(R.id.content_frame, fragment)
            commit()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.d("DEBGUG: onSIS", "mainMenu")
    }

    override fun onShowCartButtonClick(v: View) {
        Log.d("onCartButtonClick","yes")
        println("plsssssssss")
        v.findNavController()
            .navigate(R.id.action_mainMenuFragment_to_cartFragment)
    }

}