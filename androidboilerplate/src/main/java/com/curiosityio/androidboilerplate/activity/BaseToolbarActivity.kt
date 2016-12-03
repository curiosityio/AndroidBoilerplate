package com.curiosityio.androidboilerplate.activity

import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import com.curiosityio.androidboilerplate.R

abstract class BaseToolbarActivity() : BaseActivity() {

    protected lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupViews()
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_base_toolbar
    }

    override fun getFragmentContainerId(): Int {
        return R.id.activity_base_toolbar_fragment_container
    }

    private fun setupViews() {
        toolbar = findViewById(R.id.toolbar) as Toolbar

        setSupportActionBar(toolbar)
    }

    override fun setContentView(layoutResID: Int) {
        super.setContentView(layoutResID)
    }

    fun setBackButtonOnToolbar() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    fun setToobarTitle(title: String) {
        setTitle(title)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            android.R.id.home -> {
                supportFinishAfterTransition()
                return true
            }
            else -> {
                return super.onOptionsItemSelected(item)
            }
        }
    }

}
