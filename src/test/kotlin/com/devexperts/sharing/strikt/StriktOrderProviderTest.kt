package com.devexperts.sharing.strikt

import com.devexperts.sharing.*
import org.junit.jupiter.api.Test
import strikt.api.DescribeableBuilder
import strikt.api.expect
import strikt.api.expectThat
import strikt.api.expectThrows
import strikt.assertions.*

class StriktOrderProviderTest : OrderProviderTest {

    @Test
    override fun `01 - simple assert pass`() {
        expectThat(OrderProvider.provideOrder1())
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
    override fun `02 - simple assert fail`() {
        expectThat(OrderProvider.provideOrder1())
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
    override fun `03 - nullable fluent assert pass`() {
        expectThat(OrderProvider.provideOrder2())
            .isNotNull()
            .get { orderType }
            .isEqualTo(OrderType.REGULAR)
    }

    @Test
    override fun `04 - nullable fluent assert fail`() {
        expectThat(OrderProvider.provideOrder2())
            .isNotNull()
            .get { orderType }
            .isEqualTo(OrderType.EXERCISE)
    }

    @Test
    override fun `05 - multiple fields on the same object pass`() {
        expectThat(OrderProvider.provideOrder1()) {
            get { orderType }.isEqualTo(OrderType.REGULAR)
            get { orderSide }.isEqualTo(OrderSide.BUY)
            get { quantity }.isEqualTo(10)
        }
    }

    @Test
    override fun `06 - multiple fields on the same object fail`() {
        expectThat(OrderProvider.provideOrder1()) {
            get { orderType }.isEqualTo(OrderType.EXERCISE)
            get { orderSide }.isEqualTo(OrderSide.SELL)
            get { quantity }.isEqualTo(-10)
        }
    }

    @Test
    override fun `07 - collection elements pass`() {
        expectThat(OrderProvider.providerOrderList())
            .containsExactlyInAnyOrder(OrderProvider.provideOrder1(), OrderProvider.provideOrder2())
    }

    @Test
    override fun `08 - collection elements fail`() {
        expectThat(OrderProvider.providerOrderList())
            .containsExactlyInAnyOrder(OrderProvider.provideOrder2(), Order(quantity = 30))
    }

    @Test
    override fun `09 - multiple subjects softly fail`() {
        expect {
            that(OrderProvider.provideOrder1())
                .isEqualTo(OrderProvider.provideOrder2())
            that(OrderProvider.provideOrder2())
                .isEqualTo(OrderProvider.provideOrder1())
        }
    }

    @Test
    override fun `10 - custom assertion fail`() {
        expectThat(OrderProvider.provideOrder1().copy(orderSide = OrderSide.SELL))
            .isBuy()
    }

    @Test
    override fun `11 - exception thrown`() {
        expectThrows<IllegalStateException> {
            error("Fail in test?")
        }.message.isEqualTo("Fail in test")
    }

    @Test
    override fun `12 - map entry pass`() {
        expectThat(OrderProvider.providerOrderList().groupBy { it.orderSide })
            .hasEntry(OrderSide.BUY, listOf(OrderProvider.provideOrder1()))
    }

    @Test
    override fun `13 - map entry fail`() {
        expectThat(OrderProvider.providerOrderList().groupBy { it.orderSide })
            .hasEntry(OrderSide.BUY, listOf(OrderProvider.provideOrder2()))
    }


    @Test
    override fun `14 - compare by fields fail`() {
        TODO("Not available in strikt")
    }

    @Test
    override fun `15 - check is instance and starts with`() {
        expectThat("" as Any)
            .isA<String>()
            .startsWith("abc")
    }

}

private fun DescribeableBuilder<Order>.isBuy() = apply {
    get { orderSide }.isEqualTo(OrderSide.BUY)
}
