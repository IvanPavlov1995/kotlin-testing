package com.devexperts.sharing

import com.devexperts.sharing.OrderSide

enum class OrderSide {
    BUY, SELL;

    val sign: Int
        get() = if (this == BUY) 1 else -1
}