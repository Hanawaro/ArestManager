package com.speechpeach.arestmanager.ui.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat.invalidateOptionsMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.speechpeach.arestmanager.R
import com.speechpeach.arestmanager.databinding.FragmentUsersListBinding
import com.speechpeach.arestmanager.models.User
import com.speechpeach.arestmanager.repository.UserRepository
import com.speechpeach.arestmanager.utils.adapters.UsersAdapter
import com.speechpeach.arestmanager.utils.hideKeyboard
import com.speechpeach.arestmanager.viewmodels.MainActivityViewModel
import com.speechpeach.arestmanager.viewmodels.UsersViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

@AndroidEntryPoint
class UsersListFragment : Fragment(R.layout.fragment_users_list) {

    private lateinit var binding: FragmentUsersListBinding

    private val viewModel: UsersViewModel by viewModels()
    private val activityViewModel: MainActivityViewModel by activityViewModels()

    private lateinit var userAdapter: UsersAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUsersListBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)
        hideKeyboard()

        val onItemClickListener = object : UsersAdapter.ItemClickListener {
            override fun onItemClick(user: User) {
                val action = UsersListFragmentDirections.actionUsersListFragmentToCreateUserFragment(user)
                findNavController().navigate(action)
            }
            override fun onItemLongClick(user: User): Boolean {
                AlertDialog.Builder(requireContext())
                    .setMessage("\nDo you want to delete ${user.secondName} ${user.name}\n")
                    .setPositiveButton("Delete") { _, _ ->
                        viewModel.deleteUser(user)
                        val users = ArrayList(userAdapter.currentList)
                        users.remove(user)
                        userAdapter.submitList(users)
                    }
                    .setNegativeButton("Cancel") { _, _ -> }
                    .create().show()
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


}