package com.rudder.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.rudder.R
import com.rudder.data.remote.Category
import com.rudder.databinding.CategorySelectorBinding
import com.rudder.util.CategoriesDiffCallback
import com.rudder.util.CustomOnclickListener

class CategorySelectorAdapter(val categoryList: ArrayList<Category>, val selectedCategoryNum: Int, val context : Context, val listener: CustomOnclickListener): RecyclerView.Adapter<CategorySelectorAdapter.CustomViewHolder>(){
    inner class CustomViewHolder(val categorySelectorBinding: CategorySelectorBinding) : RecyclerView.ViewHolder(categorySelectorBinding.root)
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CategorySelectorAdapter.CustomViewHolder {
        val bind = DataBindingUtil.inflate<CategorySelectorBinding>(
            LayoutInflater.from(parent.context),
            R.layout.category_selector,
            parent,
            false
        )

        return CustomViewHolder(bind)
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }

    override fun onBindViewHolder(holder: CategorySelectorAdapter.CustomViewHolder, position: Int) {
        holder.categorySelectorBinding.category = categoryList[position]
        if(selectedCategoryNum==position){
            listener.onClick(holder.categorySelectorBinding.root,position)
        }
        holder.categorySelectorBinding.categoryTextView.viewTreeObserver.addOnGlobalLayoutListener(
            object : ViewTreeObserver.OnGlobalLayoutListener{
                override fun onGlobalLayout() {
                    var lp = holder.categorySelectorBinding.categoryUnderBarView.layoutParams
                    lp.width = holder.categorySelectorBinding.categoryTextView.width
                    holder.categorySelectorBinding.categoryUnderBarView.layoutParams=lp
                    if(holder.categorySelectorBinding.categoryTextView.width>0){
                        var lp2 = holder.categorySelectorBinding.root.layoutParams
                        lp2.width = holder.categorySelectorBinding.categoryTextView.width+70
                        holder.categorySelectorBinding.root.layoutParams = lp2
                    }
                }

            }
        )
        holder.categorySelectorBinding.categorySelectorConstraintLayout.setOnClickListener {
            listener.onClick(holder.categorySelectorBinding.root,position)
        }
    }

    fun updateCategories(newCategories: ArrayList<Category>){
        if(newCategories.size>0) {
            val diffCallback: CategoriesDiffCallback = CategoriesDiffCallback(categoryList, newCategories)
            val diffResult: DiffUtil.DiffResult = DiffUtil.calculateDiff(diffCallback)

            categoryList.clear()
            categoryList.addAll(newCategories)
            diffResult.dispatchUpdatesTo(this)
        }
    }

}