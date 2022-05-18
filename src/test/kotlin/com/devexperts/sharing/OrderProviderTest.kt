package com.devexperts.sharing

interface OrderProviderTest {
    fun `simple assert pass`()
    fun `simple assert fail`()
    fun `nullable fluent assert pass`()
    fun `nullable fluent assert fail`()
    fun `multiple fields on the same object pass`()
    fun `multiple fields on the same object fail`()
    fun `collection elements pass`()
    fun `collection elements fail`()
    fun `multiple subjects softly fail`()
    fun `custom assertion fail`()
    fun `exception thrown`()
    fun `map entry pass`()
    fun `map entry fail`()
    fun `compare by fields fail`()
    fun `check is instance and starts with`()
}