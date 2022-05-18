package com.devexperts.sharing

import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.TestMethodOrder

@TestMethodOrder(MethodOrderer.Alphanumeric::class)
interface OrderProviderTest {
    fun `01 - simple assert pass`()
    fun `02 - simple assert fail`()
    fun `03 - nullable fluent assert pass`()
    fun `04 - nullable fluent assert fail`()
    fun `05 - multiple fields on the same object pass`()
    fun `06 - multiple fields on the same object fail`()
    fun `07 - collection elements pass`()
    fun `08 - collection elements fail`()
    fun `09 - multiple subjects softly fail`()
    fun `10 - custom assertion fail`()
    fun `11 - exception thrown`()
    fun `12 - map entry pass`()
    fun `13 - map entry fail`()
    fun `14 - compare by fields fail`()
    fun `15 - check is instance and starts with`()
}