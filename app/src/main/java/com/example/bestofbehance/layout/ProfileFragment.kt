package com.example.bestofbehance.layout

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.navArgs
import com.example.bestofbehance.BR
import com.example.bestofbehance.R
import com.example.bestofbehance.binding.ProfileBinding
import com.example.bestofbehance.databases.DBMain
import com.example.bestofbehance.databinding.FragmentDetailsBinding
import com.example.bestofbehance.databinding.FragmentProfileBinding
import com.example.bestofbehance.viewModels.VMForParse
import com.example.bestofbehance.viewModels.ViewModelFactory
import kotlinx.android.synthetic.main.fragment_best.*
import kotlinx.android.synthetic.main.profile_card.*


class ProfileFragment : Fragment() {

    private val args: ProfileFragmentArgs by navArgs()
    lateinit var jsonModel: VMForParse

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.two_buttons_toolbar, menu)

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {

            R.id.menu_bookmark -> {Toast.makeText(activity, "Work in progress", Toast.LENGTH_SHORT).show()}

            R.id.menu_share -> {Toast.makeText(activity, "Work in progress", Toast.LENGTH_SHORT).show() }
        }
        return super.onOptionsItemSelected(item)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentProfileBinding.inflate(inflater)
        val fragmentDetailsView: View = binding.root

        jsonModel = ViewModelProviders.of(this, ViewModelFactory()).get(VMForParse::class.java)

        jsonModel.setUser(args.cardBindingProfile)

        val observerGSON = Observer<MutableList<ProfileBinding>> { list ->

            binding.cardViewProfile = list[0]
            binding.notifyPropertyChanged(BR._all)
        }
        jsonModel.listForUser.observe(this, observerGSON)

        return fragmentDetailsView
    }
}
