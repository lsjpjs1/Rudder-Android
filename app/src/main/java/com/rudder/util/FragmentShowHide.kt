package com.rudder.util

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

class FragmentShowHide(val supportedFragmentManager: FragmentManager) {

    private val transaction = supportedFragmentManager.beginTransaction()

    fun showFragment(fragment: Fragment, id: Int){
        hideAllFragmentsInId(id)
        transaction.show(fragment).commit()
    }


    fun addFragment(fragment: Fragment, id: Int, tag: String){
        if(supportedFragmentManager.findFragmentByTag(tag)==null ){
            transaction.add(id,fragment,tag)
        }else if(supportedFragmentManager.findFragmentByTag(tag)!=fragment){
            transaction.remove(supportedFragmentManager.findFragmentByTag(tag)!!)
            transaction.add(id,fragment,tag)
        }
    }


    fun removeFragment(fragment: Fragment){
        transaction.remove(fragment)
    }


    fun addToBackStack(){
        transaction.addToBackStack(null)
    }

    fun hideFragment(fragment: Fragment){
        transaction.hide(fragment)
    }

    fun hideAllFragment(id: Int){
        val otherFragments = findFragmentsInId(id)
        for(frag in otherFragments){
            if(frag.isVisible) transaction.hide(frag)
        }
        transaction.commit()
    }

    private fun hideAllFragmentsInId(id: Int){
        val otherFragments = findFragmentsInId(id)
        for(frag in otherFragments){
            if(frag.isVisible) transaction.hide(frag)
        }
    }

    private fun findFragmentsInId(id: Int) : ArrayList<Fragment>{
        var resFragments: ArrayList<Fragment> = arrayListOf()
        for(fragment in supportedFragmentManager.fragments){
            if(fragment.id == id){
                resFragments.add(fragment)
            }
        }
        return resFragments
    }

}