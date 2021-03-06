package com.feup.cmov.acme_client.screens.checkout.cart

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getColor
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.feup.cmov.acme_client.AcmeApplication
import com.feup.cmov.acme_client.R
import com.feup.cmov.acme_client.database.models.Order
import com.feup.cmov.acme_client.database.models.composed_models.OrderWithItems
import com.feup.cmov.acme_client.databinding.FragmentCartBinding
import com.feup.cmov.acme_client.screens.checkout.CartViewModel
import com.feup.cmov.acme_client.screens.main_menu.MainMenuFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CartFragment() : Fragment(), CartHandler {

    private val viewModel: CartViewModel by activityViewModels()
    lateinit var binding: FragmentCartBinding
    private var cartItemAdapter: CartItemAdapter = CartItemAdapter()
    private var voucherUsedAdapter: VoucherUsedAdapter = VoucherUsedAdapter()

    @SuppressLint("SetTextI18n")
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

        binding.seeMenu.setOnClickListener { activity?.onBackPressed() }

        viewModel.getCartListLiveData().observe(viewLifecycleOwner, Observer observe@{ cartList ->
            if (cartList.isEmpty()) {
                binding.noCartItemsText.visibility = View.VISIBLE
                binding.cartItemList.visibility = View.GONE
                binding.nextButton.isEnabled = false
                binding.nextButton.isClickable = false
                binding.nextButton.setBackgroundColor(getColor(AcmeApplication.getAppContext(), R.color.grey_800))
            } else {
                binding.noCartItemsText.visibility = View.GONE
                binding.cartItemList.visibility = View.VISIBLE
                binding.nextButton.isEnabled = true
                binding.nextButton.isClickable = true
                binding.nextButton.setBackgroundColor(getColor(AcmeApplication.getAppContext(), R.color.black))
            }

            cartItemAdapter.data = cartList.values.toList()
        })

        val priceStringFormat: String = AcmeApplication.getAppContext().getString(R.string.cart_price)
        viewModel.getSubtotalCartPrice().observe(viewLifecycleOwner, Observer observe@{ totalCartPrice ->
            binding.subtotalPrice.text = String.format(priceStringFormat, totalCartPrice)
        })

        viewModel.getSelectedVouchers().observe(viewLifecycleOwner, Observer observe@{ vouchersList ->
            if (vouchersList.isEmpty()) {
                binding.noVoucherText.visibility = View.VISIBLE
                binding.cartVoucherList.visibility = View.GONE
            } else {
                binding.noVoucherText.visibility = View.GONE
                binding.cartVoucherList.visibility = View.VISIBLE
            }

            // Calculate Savings Per Voucher
            voucherUsedAdapter.data = viewModel.getSavingsForSelectedVouchers()
        })

        viewModel.getPlacedOrder().observe(viewLifecycleOwner, Observer observe@{ order ->
            if(viewLifecycleOwner.lifecycle.currentState != Lifecycle.State.RESUMED)
                return@observe

            if(order != null) {
                container!!.findNavController()
                    .navigate(R.id.action_cartFragment_to_orderPlacedFragment, bundleOf("order" to OrderWithItems.serialize(order)))
            }
        })

        viewModel.updateTotalSavings()
        viewModel.getTotalSavings().observe(viewLifecycleOwner, Observer observe@{ totalSavings ->
            binding.voucherPrice.text = (if(totalSavings > 0) "- " else "") +
                    String.format(priceStringFormat, totalSavings)
        })

        viewModel.updateTotalPrice()
        viewModel.getTotalPrice().observe(viewLifecycleOwner, Observer observe@{ totalPrice ->
            binding.totalPrice.text = String.format(priceStringFormat, totalPrice)
            binding.totalPriceNextButton.text = String.format(priceStringFormat, totalPrice)
        })

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.cartItemList.adapter = cartItemAdapter
        binding.cartVoucherList.adapter = voucherUsedAdapter
    }

    override fun onAddVoucherClick(v: View) {
        v.findNavController()
            .navigate(R.id.action_cartFragment_to_voucherSelectionFragment)
    }

    override fun placeOrder(v: View) {
        viewModel.placeOrder()
    }

}