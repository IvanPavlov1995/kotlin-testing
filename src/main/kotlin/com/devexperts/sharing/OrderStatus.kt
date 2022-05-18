package com.devexperts.sharing

enum class OrderStatus {
    NEW,
    RISK_PENDING,
    EXCHANGE_PENDING, WAITING_TRIGGER, WAITING_LIMIT, WAITING_STOP,
    WAITING, WAITING_TRAIL_STOP, CANCELED, REPLACED, WORKING,
    EXECUTING,  //wait for response from STP / on Manual execution / BBook sleep in activator
    COMPLETED, EXPIRED, REJECTED,
    TRIGGER_PENDING,
    PLACED,
    WAITING_CONDITION;
}