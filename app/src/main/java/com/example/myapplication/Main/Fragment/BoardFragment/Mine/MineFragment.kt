package com.example.myapplication.Main.Fragment.BoardFragment.Mine

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.myapplication.Main.Fragment.BoardFragment.BoardFragment
import com.example.myapplication.Main.Fragment.BoardFragment.Recent.repo.Repo
import com.example.myapplication.R

class MineFragment : Fragment(){
    val repo = Repo.StaticFunction.getInstance()
    companion object {
        fun newInstance() : MineFragment = MineFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_mine,container,false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
    override fun onPause() {
        super.onPause()
        repo.upDateOnlineState("offline")

    }

    override fun onResume() {
        super.onResume()
        repo.upDateOnlineState("online")

    }
}