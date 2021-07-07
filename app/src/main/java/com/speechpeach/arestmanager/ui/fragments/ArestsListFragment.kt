package com.speechpeach.arestmanager.ui.fragments

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
import com.speechpeach.arestmanager.viewmodels.ArestViewModel
import com.speechpeach.arestmanager.viewmodels.MainActivityViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ArestsListFragment : Fragment(R.layout.fragment_arests_list) {

    private lateinit var binding: FragmentArestsListBinding

    private val viewModel: ArestViewModel by viewModels()
    private val activityViewModel: MainActivityViewModel by activityViewModels()

    private lateinit var arestAdapter: ArestsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentArestsListBinding.inflate(inflater, container, false)

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)
        hideKeyboard()

        val onItemClickListener = object : ArestsAdapter.ItemClickListener {
            override fun onItemClick(arest: Arest) {
                val action = ArestsListFragmentDirections.actionArestsListFragmentToEditArestFragment(arest)
                findNavController().navigate(action)
            }

            override fun onItemLongClick(arest: Arest): Boolean {
                AlertDialog.Builder(requireContext())
                        .setMessage("\nDo you want to delete ${arest.name}\n")
                        .setPositiveButton("Delete") { _, _ ->
                            viewModel.deleteArest(arest)
                            val arests = ArrayList(arestAdapter.currentList)
                            arests.remove(arest)
                            arestAdapter.submitList(arests)
                        }
                        .setNegativeButton("Cancel") { _, _ -> }
                        .create().show()
                return true
            }

        }

        arestAdapter = ArestsAdapter(onItemClickListener)

        binding.recyclerArests.apply {
            adapter = arestAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }

        binding.refreshLayoutArests.apply {

            setOnRefreshListener {
                viewModel.arests.observe(viewLifecycleOwner) {
                    arestAdapter.submitList(it)
                    isRefreshing = false
                }
            }

        }

    }

    override fun onResume() {
        super.onResume()
        activityViewModel.showBottomMenu()
        viewModel.arests.observe(viewLifecycleOwner) {
            arestAdapter.submitList(it)
        }
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

}