package com.speechpeach.arestmanager.ui.fragments.arests

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.speechpeach.arestmanager.R
import com.speechpeach.arestmanager.databinding.FragmentSelectUserBinding
import com.speechpeach.arestmanager.models.User
import com.speechpeach.arestmanager.utils.adapters.UsersAdapter
import com.speechpeach.arestmanager.viewmodels.arests.SelectUserViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SelectUserFragment: Fragment(R.layout.fragment_select_user) {

    private lateinit var binding: FragmentSelectUserBinding
    private val args: SelectUserFragmentArgs by navArgs()

    private val viewModel: SelectUserViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentSelectUserBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAdapter()
    }

    private fun initAdapter() {
        lateinit var userAdapter: UsersAdapter

        val onItemClickListener = object : UsersAdapter.ItemClickListener {
            override fun onItemClick(user: User) {
                val action = SelectUserFragmentDirections.actionSelectUserFragmentToLetArestFragment(
                        args.arest.copy(
                                userID = user.id,
                                userName = user.name,
                                userSecondName = user.secondName
                        ), true
                )
                findNavController().navigate(action)
            }
            override fun onItemLongClick(user: User): Boolean { return true }
        }

        userAdapter = UsersAdapter(onItemClickListener)

        binding.recyclerUsers.apply {
            adapter = userAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }

        viewModel.getUsers().observe(viewLifecycleOwner) {
            userAdapter.submitList(it)
        }
    }

}