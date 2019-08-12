package com.pear2pear.car2car

interface BasePresenter<T> {
    var view: T?

    fun attach(view: T) {
        this.view = view
    }

    fun detach() {
        this.view = null
    }
}