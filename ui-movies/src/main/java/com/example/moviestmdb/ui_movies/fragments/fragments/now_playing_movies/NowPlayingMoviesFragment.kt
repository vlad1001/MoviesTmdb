package com.example.moviestmdb.ui_movies.fragments.fragments.now_playing_movies

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moviestmdb.Genre
import com.example.moviestmdb.core.TmdbImageManager
import com.example.moviestmdb.core.extensions.launchAndRepeatWithViewLifecycle
import com.example.moviestmdb.core_ui.R.dimen
import com.example.moviestmdb.core_ui.util.SpaceItemDecoration
import com.example.moviestmdb.core_ui.util.showToast
import com.example.moviestmdb.ui_movies.R
import com.example.moviestmdb.ui_movies.databinding.FragmentNowPlayingMoviesBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import com.example.moviestmdb.core.constants.Constants.MOVIE_ID
import com.example.moviestmdb.ui_movies.databinding.ChipBinding
import com.google.android.material.chip.Chip
import kotlinx.coroutines.flow.first

@AndroidEntryPoint
class NowPlayingMoviesFragment : Fragment() {

    lateinit var binding: FragmentNowPlayingMoviesBinding
    private val viewModel: NowPlayingMoviesViewModel by viewModels()

    lateinit var pagingAdapter: NowPlayingMoviesAdapter

    @Inject
    lateinit var tmdbImageManager: TmdbImageManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNowPlayingMoviesBinding.inflate(inflater)

        initAdapter()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupWithNavController(binding.toolbar, findNavController())
        binding.toolbar.title = "Now Playing Movies"

        launchAndRepeatWithViewLifecycle {
                val data = viewModel.pageList.first()
                pagingAdapter.submitData(data)
        }

//            viewModel.state.collect{ viewState ->
//                pagingAdapter.submitData(viewState.nowPlayingPagingData)
//                addChips(binding.nowPlayingChipGroup, viewState.genreList)
//            }
//        }


        launchAndRepeatWithViewLifecycle {
            viewModel.filteredChips.collect { chips ->
                val list = mutableListOf<Chip>()
                chips.forEach { chip ->
                    val ch = createChip(chip)
                    ch.setOnCheckedChangeListener { compoundButton, isChecked ->
                        viewModel.toggleFilter(compoundButton.id, isChecked)
                    }
                    list.add(ch)
                }

                binding.nowPlayingChipGroup.removeAllViews()
                list.forEach { chip ->
                    binding.nowPlayingChipGroup.addView(chip)
                }
            }
        }
    }


    private fun createChip(chip: Genre): Chip {
        val chipView = ChipBinding.inflate(LayoutInflater.from(context)).root
        chipView.id = chip.id
        chipView.text = chip.name

        return chipView
    }


//    fun addChips(chipGroup: ChipGroup, chips: List<Genre>) {
//        chips.forEach { genre ->
//            val chip = ChipBinding.inflate(LayoutInflater.from(chipGroup.context)).root
//            chip.id = genre.id
//            chip.text = genre.name
//            chip.setOnCheckedChangeListener { compountBtn, ischecked ->
//
//            }
//            chipGroup.addView(chip)
//        }
//    }

    private val movieClickListener: (Int) -> Unit = { movieId ->
        context?.showToast(movieId.toString())

        val args = Bundle();
        args.putInt(MOVIE_ID, movieId)
        findNavController().navigate(R.id.movieDetailsFragment, args)
    }

    private fun initAdapter() {
        pagingAdapter =
            NowPlayingMoviesAdapter(
                tmdbImageManager.getLatestImageProvider(),
                movieClickListener
            )

        binding.nowPlayingRecycler.run {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = pagingAdapter

            val spacing = resources.getDimension(dimen.spacing_normal).toInt()
            val decoration = SpaceItemDecoration(
                spacing, -spacing
            )
            addItemDecoration(decoration)
        }
    }

}