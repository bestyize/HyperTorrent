package com.thewind.community.feed.page

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnScrollChangeListener
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.thewind.community.feed.model.RecommendFeetCard
import com.thewind.community.post.page.PostActivity
import com.thewind.hypertorrent.R
import com.thewind.hypertorrent.databinding.FragmentRecommendFeedBinding


private const val KEY_CHANNEL = "channel"
private const val KEY_COLUMN = "column"

class RecommendFeedFragment : Fragment() {

    private var channel:String? = null
    private var page: Int = 1
    private var isLoading = false
    private var column: Int = 1

    private val cardList:MutableList<RecommendFeetCard> = mutableListOf()


    private lateinit var binding:FragmentRecommendFeedBinding
    private lateinit var vm: RecommendFeedViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vm = ViewModelProvider(this)[RecommendFeedViewModel::class.java]
        channel = if (savedInstanceState != null) {
            savedInstanceState.getString(KEY_CHANNEL)
        } else {
            arguments?.getString(KEY_CHANNEL)
        }
        column = savedInstanceState?.getInt(KEY_COLUMN) ?: (arguments?.getInt(KEY_COLUMN) ?:1)

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(KEY_CHANNEL, channel)
        outState.putInt(KEY_COLUMN, column)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRecommendFeedBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvItems.layoutManager = StaggeredGridLayoutManager(column, StaggeredGridLayoutManager.VERTICAL).apply {
            gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_NONE
        }
        binding.rvItems.adapter = RecommendCardAdapter(cardList) { id ->
            val intent = Intent(activity, PostActivity::class.java)
            intent.putExtra("post_id", id)
            startActivity(intent)
        }
        binding.rvItems.addOnScrollListener(object :RecyclerView.OnScrollListener() {
            private var lastPos: Int = 0
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (lastPos == cardList.size - 1 && !isLoading) {
                        isLoading = true
                        vm.loadCardList(channel?: "0", ++page)
                    }
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                lastPos =
                    (recyclerView.layoutManager as? LinearLayoutManager)?.findLastCompletelyVisibleItemPosition()
                        ?: 0
            }
        })
        binding.srfRefresh.setOnRefreshListener {
            if (isLoading) return@setOnRefreshListener
            isLoading = true
            binding.srfRefresh.isRefreshing = true
            page = 1
            vm.loadCardList(channel ?: "0", page)
        }

        vm.cardListLiveData.observe(viewLifecycleOwner) {
            isLoading = false
            binding.srfRefresh.isRefreshing = false
            cardList.addAll(it)
            binding.rvItems.adapter?.notifyDataSetChanged()
        }
        vm.loadCardList(channel ?: "0", page)



    }

    companion object {

        @JvmStatic
        fun newInstance(channel: String, column: Int) =
            RecommendFeedFragment().apply {
                arguments = Bundle().apply {
                    putString(KEY_CHANNEL, channel)
                    putInt(KEY_COLUMN, column)
                }
            }
    }
}