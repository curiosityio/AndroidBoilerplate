package com.curiosityio.androidboilerplate.activity

import android.app.Fragment
import android.app.FragmentTransaction
import android.content.ComponentName
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.support.v4.app.ActivityOptionsCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.support.v4.util.Pair

abstract class BaseActivity : AppCompatActivity() {

    override fun onBackPressed() {
        if (fragmentManager.backStackEntryCount > 0) {
            fragmentManager.popBackStack()
        } else {
            super.onBackPressed()
        }
    }

    private fun setFragmentInContainer(fragment: Fragment?, fragmentContainer: Int, replace: Boolean = true, addToBackstack: Boolean = false) {
        fragment?.let { fragment ->
            val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()

            if (replace) fragmentTransaction.replace(fragmentContainer, fragment) else fragmentTransaction.add(fragmentContainer, fragment)
            if (addToBackstack) fragmentTransaction.addToBackStack(null)

            fragmentTransaction.commit()
        }
    }

    protected fun replaceFragment(fragment: Fragment?, fragmentContainer: Int, addToBackstack: Boolean = false) {
        setFragmentInContainer(fragment, fragmentContainer, true, addToBackstack)
    }

    protected fun addFragment(fragment: Fragment?, fragmentContainer: Int) {
        setFragmentInContainer(fragment, fragmentContainer, false)
    }

    // view transition animations with lollipop and above.
    // `startActivityWithTransition(DestActivity.getIntent(), srcViewToTransition, R.string.view_transition_name)`
    protected fun startActivityWithTransition(intent: Intent, transitionView: View, transitionName: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val options: ActivityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(this, transitionView, getString(transitionName))
            startActivity(intent, options.toBundle())
        } else {
            startActivity(intent)
        }
    }

    protected fun startActivityWithTransition(intent: Intent, vararg transitions: Pair<View, String>) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val options: ActivityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(this, *transitions)
            startActivity(intent, options.toBundle())
        } else {
            startActivity(intent)
        }
    }

}