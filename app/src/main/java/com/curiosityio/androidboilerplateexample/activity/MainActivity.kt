package com.curiosityio.androidboilerplateexample.activity

import android.app.Fragment
import com.curiosityio.androidboilerplate.activity.BaseActivity
import com.curiosityio.androidboilerplateexample.fragment.MainFragment

open class MainActivity() : BaseActivity() {

    override fun getInitialFragment(): Fragment? {
        return MainFragment.getInstance()
    }

}