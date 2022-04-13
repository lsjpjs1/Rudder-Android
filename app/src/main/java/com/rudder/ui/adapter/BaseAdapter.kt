package com.rudder.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView


// T : 반복될 객체의 타입, VB : ViewDataBinding의 하위 타입
abstract class BaseAdapter<T,VIEWBINDING : ViewDataBinding>(
    val diffUtil: DiffUtil.ItemCallback<T>,
    val layout: Int
) : ListAdapter<T,BaseAdapter<T,VIEWBINDING>.BaseViewHolder>(diffUtil){

    lateinit var binding: VIEWBINDING

    inner class BaseViewHolder(val viewBinding: VIEWBINDING) :
        RecyclerView.ViewHolder(viewBinding.root)

    //이 부분 커스터마이징 할 땐 super 사용
    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): BaseAdapter<T,VIEWBINDING>.BaseViewHolder {
        binding = DataBindingUtil.inflate<VIEWBINDING>(
            LayoutInflater.from(parent.context),
            layout,
            parent,
            false
        )

        return BaseViewHolder(binding)
    }
}