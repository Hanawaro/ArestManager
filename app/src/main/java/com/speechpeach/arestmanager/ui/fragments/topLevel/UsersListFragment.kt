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
import com.speechpeach.arestmanager.databinding.FragmentUsersListBinding
import com.speechpeach.arestmanager.models.User
import com.speechpeach.arestmanager.utils.adapters.UsersAdapter
import com.speechpeach.arestmanager.utils.hideKeyboard
import com.speechpeach.arestmanager.viewmodels.MainActivityViewModel
import com.speechpeach.arestmanager.viewmodels.topLevel.UsersListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UsersListFragment : Fragment(R.layout.fragment_users_list) {

    private lateinit var binding: FragmentUsersListBinding

    private val viewModel: UsersListViewModel by viewModels()
    private val activityViewModel: MainActivityViewModel by activityViewModels()

    private lateinit var userAdapter: UsersAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentUsersListBinding.inflate(inflater, container, false)
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
        viewModel.users.observe(viewLifecycleOwner) {
            userAdapter.submitList(it)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_top_level, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.action_create -> {
                val action = UsersListFragmentDirections.actionUsersListFragmentToCreateUserFragment(null)
                findNavController().navigate(action)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun initAdapter() {
        val onItemClickListener = object : UsersAdapter.ItemClickListener {
            override fun onItemClick(user: User) {
                val action = UsersListFragmentDirections.actionUsersListFragmentToCreateUserFragment(user)
                findNavController().navigate(action)
            }

            override fun onItemLongClick(user: User): Boolean {
                AlertDialog.Builder(requireContext())
                        .setMessage("\nDo you want to delete ${user.secondName} ${user.name}\n")
                        .setPositiveButton("Delete") { _, _ ->
                            viewModel.deleteUser(user).observe(viewLifecycleOwner) {
                                viewModel.users.observe(viewLifecycleOwner) {
                                    userAdapter.submitList(it)
                                }
                            }
                        }
                        .setNegativeButton("Cancel") { _, _ -> }
                        .create()
                        .show()
                return true
            }
        }

        userAdapter = UsersAdapter(onItemClickListener)

        binding.recyclerUsers.apply {
            adapter = userAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }

        binding.refreshLayoutUsers.apply {

            setOnRefreshListener {
                viewModel.users.observe(viewLifecycleOwner) {
                    userAdapter.submitList(it)
                    isRefreshing = false
                }
            }

        }
    }

}