package com.speechpeach.arestmanager.ui.fragments.topLevel

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.speechpeach.arestmanager.R
import com.speechpeach.arestmanager.databinding.FragmentArestsListBinding
import com.speechpeach.arestmanager.models.Arest
import com.speechpeach.arestmanager.utils.adapters.ArestsAdapter
import com.speechpeach.arestmanager.utils.hideKeyboard
import com.speechpeach.arestmanager.viewmodels.topLevel.ArestViewModel
import com.speechpeach.arestmanager.viewmodels.MainActivityViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ArestsListFragment : Fragment(R.layout.fragment_arests_list) {

    private lateinit var binding: FragmentArestsListBinding

    private val viewModel: ArestViewModel by viewModels()
    private val activityViewModel: MainActivityViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentArestsListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)
        hideKeyboard()

        initAdapter()
    }

    override fun onResume() {
        super.onResume()
        activityViewModel.showBottomMenu()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_top_level, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.action_create -> {
                val action = ArestsListFragmentDirections.actionArestsListFragmentToCreateUserForArestFragment()
                findNavController().navigate(action)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun initAdapter() {

        lateinit var arestAdapter: ArestsAdapter

        val onItemClickListener = object : ArestsAdapter.ItemClickListener {
            override fun onItemClick(arest: Arest) {
                val action = ArestsListFragmentDirections.actionArestsListFragmentToEditArestFragment(arest, true)
                findNavController().navigate(action)
            }

            override fun onItemLongClick(arest: Arest): Boolean {
                AlertDialog.Builder(requireContext())
                        .setMessage("\nDo you want to delete ${arest.organizationID}\n")
                        .setPositiveButton("Delete") { _, _ ->
                            viewModel.deleteArest(arest).observe(viewLifecycleOwner) {
                                viewModel.getArests().observe(viewLifecycleOwner) {
                                    arestAdapter.submitList(it)
                                }
                            }
                        }
                        .setNegativeButton("Cancel") { _, _ -> }
                        .create()
                        .show()
                return true
            }

        }

        arestAdapter = ArestsAdapter(onItemClickListener)

        binding.recyclerArests.apply {
            adapter = arestAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }

        viewModel.getArests().observe(viewLifecycleOwner) {
            arestAdapter.submitList(it)
        }

        binding.refreshLayoutArests.apply {

            setOnRefreshListener {
                viewModel.getArests().observe(viewLifecycleOwner) {
                    arestAdapter.submitList(it)
                    isRefreshing = false
                }
            }

        }
    }

}