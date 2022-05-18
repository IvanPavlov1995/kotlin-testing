package com.devexperts.sharing

data class Order(
    val actionType: ActionType = ActionType.NEW,
    val orderType: OrderType = OrderType.REGULAR,
    val status: OrderStatus = OrderStatus.NEW,
    val timeInForce: OrderTimeInForce = OrderTimeInForce.GTC,
    val closedTime: Long? = null,
    val limitPrice: Double? = null,
    val stopPrice: Double? = null,
    val stopPriceOffset: Double? = null,
    val trailPrice: Double? = null,
    val orderSide: OrderSide = OrderSide.BUY,
    val quantity: Int? = null,
    val effectiveQuantity: Int? = null,
    val filledQuantity: Int? = null,
)