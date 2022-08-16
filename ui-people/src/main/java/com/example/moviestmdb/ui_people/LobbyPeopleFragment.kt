package com.example.moviestmdb.ui_people

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI.setupWithNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moviestmdb.core.TmdbImageManager
import com.example.moviestmdb.core.extensions.launchAndRepeatWithViewLifecycle
import com.example.moviestmdb.core_ui.util.SpaceItemDecoration
import com.example.moviestmdb.core_ui.util.showToast
import com.example.moviestmdb.ui_people.databinding.PeopleFragmentBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@AndroidEntryPoint
class LobbyPeopleFragment @Inject constructor() : Fragment() {

    lateinit var binding: PeopleFragmentBinding
    private val viewModel: PeopleLobbyViewModel by viewModels()

    private lateinit var popularActorsAdapter: PopularActorsAdapter

    @Inject
    lateinit var tmdbImageManager: TmdbImageManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = PeopleFragmentBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupWithNavController(binding.toolbar, findNavController())
        binding.toolbar.title = "people"

        initPopularActorsAdapter()

        launchAndRepeatWithViewLifecycle {
            viewModel.pagedList.collectLatest { pagingData ->
                popularActorsAdapter.submitData(pagingData)
            }
        }
    }


    private fun initPopularActorsAdapter() {
        popularActorsAdapter =
            PopularActorsAdapter(
                tmdbImageManager.getLatestImageProvider(),
                movieClickListener
            )

        binding.popularActorsRecycler.run {

            val numberOfColumns = 2
            layoutManager = GridLayoutManager(context, numberOfColumns)
            adapter = popularActorsAdapter

            val spacing =
                resources.getDimension(com.example.moviestmdb.core_ui.R.dimen.spacing_normal)
                    .toInt()
            val decoration = SpaceItemDecoration(
                spacing, -spacing
            )
            addItemDecoration(decoration)
        }
    }

    private val movieClickListener: (Int) -> Unit = { movieId ->
        context?.showToast(movieId.toString())
    }
}