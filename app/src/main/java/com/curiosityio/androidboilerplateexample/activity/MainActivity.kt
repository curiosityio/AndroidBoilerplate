package com.curiosityio.androidboilerplateexample.activity

import android.app.Fragment
import android.os.Bundle
import com.curiosityio.androidboilerplate.activity.BaseActivity
import com.curiosityio.androidboilerplate.activity.BaseFragmentActivity
import com.curiosityio.androidboilerplateexample.fragment.MainFragment

open class MainActivity() : BaseFragmentActivity() {

    override fun getInitialFragment(): Fragment? {
        return MainFragment.getInstance()
    }

}