package com.example.bestofbehance.layout

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.bestofbehance.R
import kotlinx.android.synthetic.main.activity_main.*


class ProjectsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_projects, container, false)
    }

    override fun onResume() {
        super.onResume()
        activity?.navigation?.menu?.findItem(R.id.projects)?.isChecked = true
    }

}
