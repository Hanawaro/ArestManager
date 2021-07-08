package com.speechpeach.arestmanager.ui.fragments.users

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.speechpeach.arestmanager.R
import com.speechpeach.arestmanager.databinding.FragmentUserArestsListBinding
import com.speechpeach.arestmanager.models.Arest
import com.speechpeach.arestmanager.utils.adapters.ArestsAdapter
import com.speechpeach.arestmanager.viewmodels.users.UserArestsListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserArestsListFragment: Fragment(R.layout.fragment_user_arests_list) {

    private lateinit var binding: FragmentUserArestsListBinding
    private val args: UserArestsListFragmentArgs by navArgs()

    private val viewModel: UserArestsListViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentUserArestsListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAdapter()
    }

    private fun initAdapter() {
        val onItemClickListener = object : ArestsAdapter.ItemClickListener {
            override fun onItemClick(arest: Arest) {
                val action = UserArestsListFragmentDirections.actionUserArestsListFragmentToLetArestFragment(arest, false)
                findNavController().navigate(action)
            }

            override fun onItemLongClick(arest: Arest): Boolean { return true }

        }

        val arestAdapter = ArestsAdapter(onItemClickListener)

        binding.recyclerArests.apply {
            adapter = arestAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }

        viewModel.getArests(args.id).observe(viewLifecycleOwner) {
            arestAdapter.submitList(it)
        }
    }

}