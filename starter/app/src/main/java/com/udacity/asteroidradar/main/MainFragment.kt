package com.udacity.asteroidradar.main

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.api.AsteroidApiFilter
import com.udacity.asteroidradar.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this,MainViewModel.Factory(requireActivity().application)).get(MainViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val binding = FragmentMainBinding.inflate(inflater)

        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        setHasOptionsMenu(true)

        val adapter = AsteroidAdapter(AsteroidListener {
            asteroidId -> viewModel.onAsteroidClicked(asteroidId)
        })
        binding.asteroidRecycler.adapter = adapter

        viewModel.asteroids.observe(viewLifecycleOwner, Observer {
            it?.let(adapter::submitList)
        })

        viewModel.navigateToDetail.observe(viewLifecycleOwner, Observer { asteroid ->
            asteroid?.let {
                findNavController().navigate(MainFragmentDirections.actionShowDetail(asteroid))
                viewModel.onDetailNavigated()
            }
        })

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        viewModel.updateFilter(
                when(item.itemId){
                    R.id.show_week_menu -> AsteroidApiFilter.SHOW_WEEK_ASTEROIDS
                    R.id.show_today_menu -> AsteroidApiFilter.SHOW_TODAY_ASTEROIDS
                    else -> AsteroidApiFilter.SHOW_SAVED_ASTEROIDS
                }
        )
        return true
    }
}
