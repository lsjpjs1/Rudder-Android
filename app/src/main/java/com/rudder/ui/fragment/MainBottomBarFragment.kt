package com.rudder.ui.fragment

import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.gms.common.util.CollectionUtils.listOf
import com.rudder.R
import com.rudder.databinding.FragmentMainBottomBarBinding
import com.rudder.ui.activity.ActivityInterface
import com.rudder.ui.activity.MainActivityInterface
import com.rudder.viewModel.MainViewModel
import kotlinx.android.synthetic.main.fragment_main_bottom_bar.*
import kotlinx.android.synthetic.main.fragment_main_bottom_bar.view.*

class MainBottomBarFragment(val mainActivityInterface : MainActivityInterface): Fragment() {

    private val viewModel :MainViewModel by activityViewModels()
    private lateinit var bottomBar: FragmentMainBottomBarBinding
    private val lazyContext by lazy {
        context
    }
    private val ICONS by lazy { listOf<ImageView>(bottomBar.root.communityIcon,
        bottomBar.root.postMessagePageIcon,bottomBar.root.myPageIcon) }
    private val purpleRudder by lazy { ContextCompat.getColor(lazyContext!!, R.color.purple_rudder) }
    private val grey by lazy { ContextCompat.getColor(lazyContext!!, R.color.grey) }
    private val black by lazy { ContextCompat.getColor(lazyContext!!, R.color.black) }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bottomBar=DataBindingUtil.inflate<FragmentMainBottomBarBinding>(inflater,R.layout.fragment_main_bottom_bar,container,false)
        bottomBar.mainVM = viewModel
        bottomBar.mainBottomBarFragment = this
        return bottomBar.root
    }

    fun openNotificationPage() {
        val root = bottomBar.root
        swapIconColor(root.postMessagePageIcon)
        mainActivityInterface.showNotificationFragment()
    }

    fun openPostMessagePage() {
        val root = bottomBar.root
        swapIconColor(root.postMessagePageIcon)
        mainActivityInterface.showNotificationFragment()
    }

    private fun swapIconColor(imageView: ImageView){
        for (icon in ICONS){
            if (icon == imageView){
                icon.setColorFilter(purpleRudder, PorterDuff.Mode.SRC_IN)
            }else {
                icon.setColorFilter(black, PorterDuff.Mode.SRC_IN)
            }
        }
    }
}