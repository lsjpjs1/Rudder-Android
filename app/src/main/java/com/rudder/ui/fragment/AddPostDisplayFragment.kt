package com.rudder.ui.fragment

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.rudder.R
import com.rudder.databinding.FragmentAddPostDisplayBinding
import com.rudder.ui.activity.MainActivity
import com.rudder.ui.adapter.AddPostShowImagesAdapter
import com.rudder.util.AddPostImagesOnclickListener
import com.rudder.util.CustomOnclickListener
import com.rudder.viewModel.MainViewModel

class AddPostDisplayFragment : Fragment(),AddPostImagesOnclickListener {
    private val viewModel : MainViewModel by activityViewModels()
    private val lazyContext by lazy {
        requireContext()
    }
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        val display = DataBindingUtil.inflate<FragmentAddPostDisplayBinding>(inflater,
                R.layout.fragment_add_post_display,container,false)
        val spinnerAdapter = object : ArrayAdapter<String>(lazyContext,R.layout.support_simple_spinner_dropdown_item,viewModel.categoryNames.value!!){
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getView(position, convertView, parent) as TextView
                return view
            }
        }
        val addPostShowImagesAdapter = AddPostShowImagesAdapter(viewModel.selectedPhotoUriList.value!!,(activity as MainActivity).getDisplaySize(),this)
        display.mainVM=viewModel
        display.categorySpinner.adapter=spinnerAdapter

        display.showPhotoRV.also {
            it.layoutManager=LinearLayoutManager(lazyContext,LinearLayoutManager.HORIZONTAL,false)
            it.setHasFixedSize(false)
            it.adapter = addPostShowImagesAdapter
        }
        display.categorySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                viewModel.setSelectedCategoryNameInAddPost(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        viewModel.selectedPhotoUriList.observe(viewLifecycleOwner, Observer {

            it?.let {
                if (it.size>0){
                    display.showPhoto.visibility=View.VISIBLE
                    addPostShowImagesAdapter.notifyDataSetChanged()
                }
            }

        })

        viewModel.photoPickerClickSwitch.observe(viewLifecycleOwner, Observer {
            it?.let {
                Log.d("photopicker",it.toString())
                openPhotoPicker()
            }
        })

        return display.root
    }
    var photoPickerResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
        if (result.resultCode == Activity.RESULT_OK){
            val data : Intent? = result.data
            data?.let {
                var uriList = arrayListOf<Uri>()
                when{
                    it.data !=null-> uriList.add(it.data!!)
                    it.clipData != null ->{
                        for(i in 0 until it.clipData!!.itemCount){
                            uriList.add(it.clipData!!.getItemAt(i).uri)
                        }
                    }
                    else ->{
                    }
                }
                if (uriList!=arrayListOf<Uri>()){
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
}