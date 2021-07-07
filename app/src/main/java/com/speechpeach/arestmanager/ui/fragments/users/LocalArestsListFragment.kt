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
import com.speechpeach.arestmanager.databinding.FragmentLocalArestsListBinding
import com.speechpeach.arestmanager.models.Arest
import com.speechpeach.arestmanager.utils.adapters.ArestsAdapter
import com.speechpeach.arestmanager.viewmodels.users.LocalArestsListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LocalArestsListFragment: Fragment(R.layout.fragment_local_arests_list) {

    private lateinit var binding: FragmentLocalArestsListBinding
    private val args: LocalArestsListFragmentArgs by navArgs()

    private val viewModel: LocalArestsListViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentLocalArestsListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val onItemClickListener = object : ArestsAdapter.ItemClickListener {
            override fun onItemClick(arest: Arest) {
                val action = LocalArestsListFragmentDirections.actionLocalArestsListFragmentToEditArestFragment(arest, false)
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