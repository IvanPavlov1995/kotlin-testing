package com.devexperts.sharing.atrium

import ch.tutteli.atrium.api.fluent.en_GB.*
import ch.tutteli.atrium.api.verbs.expect
import ch.tutteli.atrium.creating.Expect
import com.devexperts.sharing.*
import org.junit.jupiter.api.Test

class AtriumOrderProviderTest : OrderProviderTest {
    @Test
    override fun `simple assert pass`() {
        expect(OrderProvider.provideOrder1())
            .toEqual(
                Order(
                    actionType = ActionType.NEW,
                    orderType = OrderType.REGULAR,
                    status = OrderStatus.NEW,
                    timeInForce = OrderTimeInForce.GTC,
                    orderSide = OrderSide.BUY,
                    quantity = 10,
                )
            )
    }

    @Test
    override fun `simple assert fail`() {
        expect(OrderProvider.provideOrder1())
            .toEqual(
                Order(
                    actionType = ActionType.CANCEL,
                    orderType = OrderType.EXERCISE,
                    status = OrderStatus.CANCELED,
                    timeInForce = OrderTimeInForce.FOK,
                    orderSide = OrderSide.SELL,
                    quantity = 11,
                )
            )
    }

    @Test
    override fun `nullable fluent assert pass`() {
        expect(OrderProvider.provideOrder2())
            .notToEqualNull()
            .its({ orderType }) {
                toEqual(OrderType.REGULAR)
            }
    }

    @Test
    override fun `nullable fluent assert fail`() {
        expect(OrderProvider.provideOrder2())
            .notToEqualNull()
            .its({ orderType }) {
                toEqual(OrderType.EXERCISE)
            }
    }

    @Test
    override fun `multiple fields on the same object pass`() {
        expect(OrderProvider.provideOrder1()) {
            its { orderType }.toEqual(OrderType.REGULAR)
            its { orderSide }.toEqual(OrderSide.BUY)
            its { quantity }.toEqual(10)
        }
    }

    @Test
    override fun `multiple fields on the same object fail`() {
        expect(OrderProvider.provideOrder1()) {
            its { orderType }.toEqual(OrderType.EXERCISE)
            its { orderSide }.toEqual(OrderSide.SELL)
            its { quantity }.toEqual(-10)
        }
    }

    @Test
    override fun `collection elements pass`() {
        expect(OrderProvider.providerOrderList())
            .toContain.inAnyOrder.only.values(OrderProvider.provideOrder1(), OrderProvider.provideOrder2()!!)
    }

    @Test
    override fun `collection elements fail`() {
        expect(OrderProvider.providerOrderList())
            .toContain.inAnyOrder.only.values(OrderProvider.provideOrder2()!!, Order(quantity = 30))
    }

    @Test
    override fun `multiple subjects softly fail`() {
        TODO("No grouping for multiple subjects")
    }

    @Test
    override fun `custom assertion fail`() {
        expect(OrderProvider.provideOrder1().copy(orderSide = OrderSide.SELL))
            .toBeBuy()
    }

    @Test
    override fun `exception thrown`() {
        expect {
            error("Fail in test?")
        }.toThrow<IllegalStateException>()
            .message.toEqual("Fail in test")
    }

    @Test
    override fun `map entry pass`() {
        expect(OrderProvider.providerOrderList().groupBy { it.orderSide })
            .toContain(OrderSide.BUY to listOf(OrderProvider.provideOrder1()))
    }

    @Test
    override fun `map entry fail`() {
        expect(OrderProvider.providerOrderList().groupBy { it.orderSide })
            .toContain(OrderSide.BUY to listOf(OrderProvider.provideOrder2()))
    }

    @Test
    override fun `compare by fields fail`() {
        TODO("Not implemented in atrium")
    }

    @Test
    override fun `check is instance and starts with`() {
        expect("" as Any)
            .toBeAnInstanceOf<String>()
            .toStartWith("abc")
    }

}

private fun Expect<Order>.toBeBuy() = its { orderSide }.toEqual(OrderSide.BUY)
