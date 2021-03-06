package com.rudder.ui.fragment.post

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.ArrayAdapter
import android.widget.SpinnerAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.rudder.R
import com.rudder.data.FileInfo
import com.rudder.data.remote.Category
import com.rudder.databinding.FragmentAddPostContentsBinding
import com.rudder.ui.activity.MainActivity
import com.rudder.ui.adapter.AddPostShowImagesAdapter
import com.rudder.util.AddPostImagesOnclickListener
import com.rudder.util.FileUtil
import com.rudder.viewModel.MainViewModel
import kotlinx.android.synthetic.main.fragment_add_post_contents.*
import kotlinx.android.synthetic.main.fragment_add_post_contents.view.*
import java.util.*
import java.util.stream.Collectors


class AddPostContentsFragment(val viewModel: MainViewModel, val isEdit: Boolean) : Fragment(),AddPostImagesOnclickListener {

    private val lazyContext by lazy {
        requireContext()
    }
    var categoryListForAddPost = viewModel.userSelectCategories.value!!


    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        val display = DataBindingUtil.inflate<FragmentAddPostContentsBinding>(inflater,
                R.layout.fragment_add_post_contents,container,false)

        display.mainVM = viewModel
        display.lifecycleOwner = this

        if (categoryListForAddPost[0].categoryName != "Select") {
            categoryListForAddPost.removeAt(0) /// "ALL" ??????
            categoryListForAddPost.add(0, Category(categoryName = "Select", isMember = null, categoryId = -1, categoryType = "dummy_select",categoryAbbreviation = "Select") )
        }

        val addPostShowImagesAdapter = AddPostShowImagesAdapter(viewModel.selectedPhotoUriList.value!!,(activity as MainActivity).getDisplaySize(),this)

        viewModel.clearAddPost()
        var spinnerAdapter:ArrayAdapter<Category>

        if (isEdit){ // Add??? ??????, Edit??? ??????
            display.root.categorySpinner.isEnabled=false
            display.root.showPhoto.visibility=View.GONE
            viewModel.clickPostEdit()
            spinnerAdapter = object : ArrayAdapter<Category>(lazyContext, R.layout.custom_spinner_layout,
                viewModel.userSelectCategories.value!!+
                viewModel.allClubCategories.value!!+
            viewModel.allCategories.value!!){
                override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                    val view = super.getView(position, convertView, parent) as TextView
                    return view
                }
            }
        } else {
            display.root.categorySpinner.isEnabled = true
            display.root.showPhoto.visibility=View.VISIBLE
            spinnerAdapter = object : ArrayAdapter<Category>(lazyContext, R.layout.custom_spinner_layout, categoryListForAddPost){
                override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                    val view = super.getView(position, convertView, parent) as TextView
                    return view
                }
            }
        }

        display.categorySpinner.adapter = spinnerAdapter
        display.showPhotoRV.also {
            it.layoutManager=LinearLayoutManager(lazyContext,LinearLayoutManager.HORIZONTAL,false)
            it.setHasFixedSize(false)
            it.adapter = addPostShowImagesAdapter
        }


        viewModel.selectedPhotoUriList.observe(viewLifecycleOwner, Observer {
            it?.let {
//                if (it.size>0){ // ????????? ?????????????????? ????????? ????????? ????????????
//                    display.showPhoto.visibility=View.VISIBLE
//                }
                Log.d("photos",it.toString())
                addPostShowImagesAdapter.imageUriList = it
                addPostShowImagesAdapter.notifyDataSetChanged()
            }
        })


        viewModel.photoPickerClickSwitch.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let { it ->
                if (it) {
                    openPhotoPicker()
                }
            }
        })

        display.addPostDisplayEntire.viewTreeObserver.addOnGlobalLayoutListener(
            object : ViewTreeObserver.OnGlobalLayoutListener{
                override fun onGlobalLayout() {
                    fixOtherViewHeight()
                    display.addPostDisplayEntire.viewTreeObserver.removeOnGlobalLayoutListener(this)
                }
            }
        )


        return display.root
    }
    var photoPickerResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
        if (result.resultCode == Activity.RESULT_OK){
            val data : Intent? = result.data
            data?.let {
                var uriList = arrayListOf<FileInfo>()
                when{
                    it.data !=null-> {
                        val uri = it.data!!
                        val path = FileUtil.createCopyAndReturnRealPath(lazyContext,uri).toString()
                        uriList.add(FileInfo(uri,lazyContext.contentResolver.getType(uri)!!,path))
                    }
                    it.clipData != null ->{
                        for(i in 0 until it.clipData!!.itemCount){
                            val uri = it.clipData!!.getItemAt(i).uri
                            val path = FileUtil.createCopyAndReturnRealPath(lazyContext,uri).toString()
                            uriList.add(FileInfo(uri,lazyContext.contentResolver.getType(uri)!!,path))
                        }
                    }
                    else ->{
                    }
                }
                if (uriList!=arrayListOf<FileInfo>()){
                    viewModel.setSelectedPhotoUriList(uriList)
                }
            }
        }
    }
    fun openPhotoPicker(){
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.setType("image/*")
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true)
        photoPickerResultLauncher.launch(Intent.createChooser(intent,"photoPicker"))
    }

    override fun onClickAddImage(view: View, position: Int) {
        viewModel.onPhotoPickerClick()
    }

    override fun onClickDeleteImage(view: View, position: Int) {
        viewModel.deletePhotoUriPosition(position)
    }

    fun fixOtherViewHeight(){
        val addPostDisplayEntireHeight = addPostDisplayEntire.height
        val chooseCategoryHeightRatio = 0.13
        val textTitleRatio = 0.05
        val lineRatio = 0.005

        //?????? ?????? ??????
        var lp = chooseCategoryConstraintLayout.layoutParams
        lp.height=(addPostDisplayEntireHeight*chooseCategoryHeightRatio).toInt()
        chooseCategoryConstraintLayout.layoutParams=lp

        lp=addPostDisplayTextTitle.layoutParams
        lp.height=(addPostDisplayEntireHeight*textTitleRatio).toInt()
        addPostDisplayTextTitle.layoutParams=lp

        lp=addPostDisplayImagesTitle.layoutParams
        lp.height=(addPostDisplayEntireHeight*textTitleRatio).toInt()
        addPostDisplayImagesTitle.layoutParams=lp

        lp=addPostDisplayTextLine.layoutParams
        lp.height=(addPostDisplayEntireHeight*lineRatio).toInt()
        addPostDisplayTextLine.layoutParams=lp

        lp=addPostDisplayImagesLine.layoutParams
        lp.height=(addPostDisplayEntireHeight*lineRatio).toInt()
        addPostDisplayImagesLine.layoutParams=lp

        editTextTextPersonName.maxHeight = (addPostDisplayEntireHeight * 0.3).toInt()
    }
}