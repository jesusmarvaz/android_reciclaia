package com.ingencode.reciclaia.ui.navigation

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.ingencode.reciclaia.ui.components.FragmentBase

/**
Created with ❤ by Jesús Martín (jesusmarvaz@gmail.com) on 2025-01-26.
 */

interface IBackPressedListener {
    fun handleBackPressed(a: AppCompatActivity, idHostFragment: Int)
}

object BackPressedListener : IBackPressedListener {
    override fun handleBackPressed(a: AppCompatActivity, idHostFragment: Int) {
        val fm = a.supportFragmentManager.findFragmentById(idHostFragment)?.childFragmentManager
        Log.d(::handleBackPressed.name, "For idNavHostFragment: ${idHostFragment}")
        getFragments(fm).forEach { Log.d("Fragments", "Fragment: ${it.javaClass}, isAdded: ${it.isAdded}") }
        val leaves = getLeaves(fm)
        leaves.forEach {
            Log.d("Leaves", "Fragment: ${it.javaClass}, isAdded: ${it.isAdded}")
            if (it is FragmentBase) {
                if(it.isAdded) it.goBack()
            }
        }
    }

    private fun getFragments(childFragmentManager: FragmentManager?) : List<Fragment> {
        val childFragments = childFragmentManager?.fragments
        if (childFragments.isNullOrEmpty()) return listOf()
        for(fragment in childFragments) {
            childFragments.addAll(getFragments(fragment.childFragmentManager))
        }
        return childFragments
    }

    private fun getLeaves(childFragmentManager: FragmentManager?): List<Fragment> {
        val childFragments = childFragmentManager?.fragments
        if (childFragments.isNullOrEmpty()) return listOf()
        val leaves = arrayListOf<Fragment>()
        for(fragment in childFragments) {
            val innerFragments = fragment.childFragmentManager.fragments
            if (innerFragments.isEmpty()) {
                leaves.add(fragment)
            } else {
                leaves.addAll(getLeaves(fragment.childFragmentManager))
            }
        }
        return leaves
    }
}

