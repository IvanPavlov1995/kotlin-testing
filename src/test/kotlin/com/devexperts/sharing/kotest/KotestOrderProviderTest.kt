package com.devexperts.sharing.kotest

import com.devexperts.sharing.*
import io.kotest.assertions.assertSoftly
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.Matcher
import io.kotest.matchers.MatcherResult
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.equality.shouldBeEqualToComparingFieldsExcept
import io.kotest.matchers.maps.shouldContain
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldStartWith
import io.kotest.matchers.throwable.shouldHaveMessage
import io.kotest.matchers.types.shouldBeInstanceOf
import org.junit.jupiter.api.Test

class KotestOrderProviderTest : OrderProviderTest {

    @Test
    override fun `01 - simple assert pass`() {
        OrderProvider.provideOrder1()
            .shouldBe(
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
    override fun `02 - simple assert fail`() {
        OrderProvider.provideOrder1()
            .shouldBe(
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
    override fun `03 - nullable fluent assert pass`() {
        assertSoftly(OrderProvider.provideOrder2()) {
            shouldNotBeNull()
            orderType.shouldBe(OrderType.REGULAR)
        }
    }

    @Test
    override fun `04 - nullable fluent assert fail`() {
        assertSoftly(OrderProvider.provideOrder2()) {
            shouldNotBeNull()
            orderType.shouldBe(OrderType.EXERCISE)
        }
    }

    @Test
    override fun `05 - multiple fields on the same object pass`() {
        assertSoftly(OrderProvider.provideOrder1()) {
            orderType.shouldBe(OrderType.REGULAR) // NO typesafety
            orderSide.shouldBe(OrderSide.BUY)
            quantity.shouldBe(10)
        }
    }

    @Test
    override fun `06 - multiple fields on the same object fail`() {
        assertSoftly(OrderProvider.provideOrder1()) {
            orderType.shouldBe(OrderType.EXERCISE) // NO typesafety
            orderSide.shouldBe(OrderSide.SELL)
            quantity.shouldBe(-10)
        }
    }

    @Test
    override fun `07 - collection elements pass`() {
        OrderProvider.providerOrderList()
            .shouldContainExactlyInAnyOrder(OrderProvider.provideOrder1(), OrderProvider.provideOrder2())
    }

    @Test
    override fun `08 - collection elements fail`() {
        OrderProvider.providerOrderList()
            .shouldContainExactlyInAnyOrder(OrderProvider.provideOrder2(), Order(quantity = 30))
    }

    @Test
    override fun `09 - multiple subjects softly fail`() {
        assertSoftly {
            OrderProvider.provideOrder1().shouldBe(OrderProvider.provideOrder2())
            OrderProvider.provideOrder2().shouldBe(OrderProvider.provideOrder1())
        }
    }

    @Test
    override fun `10 - custom assertion fail`() {
        OrderProvider.provideOrder1().copy(orderSide = OrderSide.SELL)
            .shouldBeBuy()
    }

    @Test
    fun `custom assertion fail - custom matcher`() {
        OrderProvider.provideOrder1().copy(orderSide = OrderSide.SELL)
            .should(beBuy)
    }

    @Test
    override fun `11 - exception thrown`() {
        shouldThrow<IllegalStateException> {
            error("Fail in test?")
        }.shouldHaveMessage("Fail in test")
    }

    @Test
    override fun `12 - map entry pass`() {
        OrderProvider.providerOrderList().groupBy { it.orderSide }
            .shouldContain(OrderSide.BUY, listOf(OrderProvider.provideOrder1()))
    }

    @Test
    override fun `13 - map entry fail`() {
        OrderProvider.providerOrderList().groupBy { it.orderSide }
            .shouldContain(OrderSide.BUY, listOf(OrderProvider.provideOrder2()))
    }

    @Test
    override fun `14 - compare by fields fail`() {
        OrderProvider.provideOrder1()
            .shouldBeEqualToComparingFieldsExcept(
                Order(
                    actionType = ActionType.REPLACE,
                    orderType = OrderType.SHORT_LOCATE,
                    status = OrderStatus.CANCELED,
                    timeInForce = OrderTimeInForce.FOK,
                    orderSide = OrderSide.BUY,
                    quantity = 10,
                ),
                Order::status,
                Order::timeInForce,
            )
    }

    @Test
    override fun `15 - check is instance and starts with`() {
        ("" as Any)
            .shouldBeInstanceOf<String>()
            .shouldStartWith("abc")
    }

}

private fun Order.shouldBeBuy() = orderSide.shouldBe(OrderSide.BUY)

private val beBuy = object : Matcher<Order> {
    override fun test(value: Order) = MatcherResult(
        value.orderSide == OrderSide.BUY,
        { "Order $value should be BUY order, but was ${value.orderSide}" },
        { "Order $value should not be BUY order" }
    )
}