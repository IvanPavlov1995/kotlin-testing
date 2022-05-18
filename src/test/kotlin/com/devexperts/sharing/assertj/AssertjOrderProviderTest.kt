package com.devexperts.sharing.assertj

import com.devexperts.sharing.*
import org.assertj.core.api.AbstractObjectAssert
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.assertj.core.api.ObjectAssert
import org.assertj.core.api.SoftAssertions.assertSoftly
import org.junit.jupiter.api.Test

class AssertjOrderProviderTest : OrderProviderTest {

    @Test
    override fun `simple assert pass`() {
        assertThat(OrderProvider.provideOrder1())
            .isEqualTo(
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
        assertThat(OrderProvider.provideOrder1())
            .isEqualTo(
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
        assertThat(OrderProvider.provideOrder2())
            .isNotNull
            .extracting { it!!.orderType }
            .isEqualTo(OrderType.REGULAR)
    }

    @Test
    override fun `nullable fluent assert fail`() {
        assertThat(OrderProvider.provideOrder2())
            .isNotNull
            .extracting { it!!.orderType }
            .isEqualTo(OrderType.EXERCISE)
    }

    @Test
    override fun `multiple fields on the same object pass`() {
        assertSoftly {
            with(OrderProvider.provideOrder1()) {
                it.assertThat(orderType)
                    .isEqualTo(OrderType.REGULAR)
                it.assertThat(orderSide)
                    .isEqualTo(OrderSide.BUY)
                it.assertThat(quantity)
                    .isEqualTo(10)
            }
        }
    }

    @Test
    override fun `multiple fields on the same object fail`() {
        assertSoftly {
            with(OrderProvider.provideOrder1()) {
                it.assertThat(orderType)
                    .isEqualTo(OrderType.EXERCISE)
                it.assertThat(orderSide)
                    .isEqualTo(OrderSide.SELL)
                it.assertThat(quantity)
                    .isEqualTo(-10)
            }
        }
    }

    @Test
    override fun `collection elements pass`() {
        assertThat(OrderProvider.providerOrderList())
            .containsExactlyInAnyOrder(OrderProvider.provideOrder1(), OrderProvider.provideOrder2())
    }

    @Test
    override fun `collection elements fail`() {
        assertThat(OrderProvider.providerOrderList())
            .containsExactlyInAnyOrder(OrderProvider.provideOrder2(), Order(quantity = 30))
    }

    @Test
    override fun `multiple subjects softly fail`() {
        assertSoftly {
            it.assertThat(OrderProvider.provideOrder1())
                .isEqualTo(OrderProvider.provideOrder2())
            it.assertThat(OrderProvider.provideOrder2())
                .isEqualTo(OrderProvider.provideOrder1())
        }
    }

    @Test
    override fun `custom assertion fail`() {
        assertThatOrder(OrderProvider.provideOrder1().copy(orderSide = OrderSide.SELL, quantity = -1))
            .isBuy()
    }

    @Test
    fun `custom assertion extension fail`() {
        assertThat(OrderProvider.provideOrder1().copy(orderSide = OrderSide.SELL, quantity = -1))
            .isBuy()
    }

    @Test
    override fun `exception thrown`() {
        assertThatThrownBy {
            error("Fail in test?")
        }.isExactlyInstanceOf(IllegalStateException::class.java)
            .hasMessage("Fail in test")
    }

    @Test
    override fun `map entry pass`() {
        assertThat(OrderProvider.providerOrderList().groupBy { it.orderSide })
            .containsEntry(OrderSide.BUY, listOf(OrderProvider.provideOrder1()))
    }

    @Test
    override fun `map entry fail`() {
        assertThat(OrderProvider.providerOrderList().groupBy { it.orderSide })
            .containsEntry(OrderSide.BUY, listOf(OrderProvider.provideOrder2()!!))
    }

    @Test
    override fun `compare by fields fail`() {
        assertThat(OrderProvider.provideOrder1())
            .usingRecursiveComparison()
            .ignoringFields("status", "timeInForce")
            .isEqualTo(
                Order(
                    actionType = ActionType.REPLACE,
                    orderType = OrderType.SHORT_LOCATE,
                    status = OrderStatus.CANCELED,
                    timeInForce = OrderTimeInForce.FOK,
                    orderSide = OrderSide.BUY,
                    quantity = 10,
                )
            )
    }

    @Test
    override fun `check is instance and starts with`() {
        val value = "" as Any
        assertThat(value)
            .isInstanceOf(String::class.java)
        value as String
        assertThat(value)
            .startsWith("abc")
    }

    private fun assertThatOrder(order: Order) = OrderAssertion(order)

    class OrderAssertion(order: Order) : AbstractObjectAssert<OrderAssertion, Order>(order, OrderAssertion::class.java) {
        fun isBuy(): OrderAssertion = apply {
            if (actual.orderSide != OrderSide.BUY) {
                failWithMessage("Expected order to have side ${OrderSide.BUY}, but got ${actual.orderSide}. Original order was $actual")
            }
        }
    }

}

private fun ObjectAssert<Order>.isBuy() = apply {
    extracting { it.orderSide }.isEqualTo(OrderSide.BUY)
}