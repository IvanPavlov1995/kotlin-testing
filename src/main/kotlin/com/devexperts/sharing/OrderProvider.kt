package com.devexperts.sharing

object OrderProvider {

    fun provideOrder1(): Order = Order(
        actionType = ActionType.NEW,
        orderType = OrderType.REGULAR,
        status = OrderStatus.NEW,
        timeInForce = OrderTimeInForce.GTC,
        orderSide = OrderSide.BUY,
        quantity = 10,
    )

    fun provideOrder2(): Order? = Order(
        actionType = ActionType.REPLACE,
        orderType = OrderType.REGULAR,
        status = OrderStatus.NEW,
        timeInForce = OrderTimeInForce.GTC,
        orderSide = OrderSide.SELL,
        quantity = 10,
    )

    fun providerOrderList(): List<Order> = listOf(
        provideOrder1(),
        provideOrder2()!!,
    )

}