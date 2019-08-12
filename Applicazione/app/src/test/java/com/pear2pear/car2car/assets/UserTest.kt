package com.pear2pear.car2car.assets

import org.junit.Test

import org.junit.Assert.*

class UserTest {

    val user = User("paolo", 1250)
    @Test
    fun getLevel() {
        assertEquals(12,user.getLevel())
    }

    @Test
    fun getcurrentExp() {
        assertEquals(50,user.getCurrentExp())
    }
}