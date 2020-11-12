package com.feup.cmov.acme_client.screens.orders.pickup_order

import androidx.databinding.ObservableField
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feup.cmov.acme_client.database.models.Order
import com.feup.cmov.acme_client.database.models.User
import com.feup.cmov.acme_client.database.models.composed_models.OrderWithItems
import com.feup.cmov.acme_client.repositories.OrderRepository
import com.feup.cmov.acme_client.repositories.UserRepository
import com.feup.cmov.acme_client.utils.ShowFeedback
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.lang.Exception


class OrderPickupViewModel @ViewModelInject constructor(
    private val orderRepository: OrderRepository
) : ViewModel() {

    private val completeOrder = MutableLiveData<OrderWithItems>(null)
    fun getCompleteOrder() = completeOrder

    fun startRefresh(order: OrderWithItems) {
        viewModelScope.launch {
            while(true) {
                completeOrder.postValue(orderRepository.hasOrderBeenPickedUp(order.order))
                delay(1000)
            }
        }
    }
}