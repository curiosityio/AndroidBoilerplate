package com.curiosityio.androidboilerplate.activity

import android.app.Fragment
import android.app.FragmentTransaction
import android.os.Bundle
import com.curiosityio.androidboilerplate.R

abstract class BaseFragmentActivity() : BaseActivity() {

    protected val FRAGMENT_CONTAINER_ID: Int = R.id.fragment_container

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_base_fragment)

        if (savedInstanceState == null) {
            addFragment(getInitialFragment(), FRAGMENT_CONTAINER_ID)
        }
    }

    abstract fun getInitialFragment(): Fragment?

}