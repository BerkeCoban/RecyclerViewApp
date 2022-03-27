package com.berkecoban.casestudy.ui


import android.graphics.Canvas
import android.graphics.Paint
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.berkecoban.R
import com.berkecoban.casestudy.adapters.RecycleAdapter
import com.berkecoban.casestudy.model.Post
import com.berkecoban.casestudy.util.Utils
import com.berkecoban.databinding.ActivityMainBinding
import com.berkecoban.databinding.BottomSheetBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {


    private lateinit var binding: ActivityMainBinding
    private lateinit var bottomSheetBinding: BottomSheetBinding
    private lateinit var recycleAdapter: RecycleAdapter
    private lateinit var mainViewModel: MainViewModel


    private var currentPostList: MutableList<Post> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        bottomSheetBinding = BottomSheetBinding.inflate(layoutInflater)
        val itemDecorator = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        itemDecorator.setDrawable(ContextCompat.getDrawable(this, R.drawable.divider)!!)
        binding.recyclerView.addItemDecoration(itemDecorator)


        setContentView(binding.root)
        recycleAdapter = RecycleAdapter(this)
        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]
        initRw()
        initLiveData()
    }

    override fun onResume() {
        mainViewModel.fetchPosts()
        super.onResume()
    }

    override fun onDestroy() {
        mainViewModel.cancelDisposable()
        super.onDestroy()
    }


    private fun initLiveData() {
        mainViewModel.mData.observe(this) { data ->
            data?.let {

                val postList: MutableList<Post> = mutableListOf()
                for (element in data) {
                    postList.add(
                        Post(
                            element.title,
                            element.body,
                            Utils.generateImageUrl(element.id)
                        )
                    )
                }
                currentPostList = postList
                recycleAdapter.differ.submitList(postList)
            }

        }
    }

    private fun initRw() {

        val simpleItemTouchCallback: ItemTouchHelper.SimpleCallback = object :
            ItemTouchHelper.SimpleCallback(
                0,
                ItemTouchHelper.LEFT
            ) {

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {

                if (actionState === ItemTouchHelper.ACTION_STATE_SWIPE) {
                    val itemView: View = viewHolder.itemView
                    val p = Paint()
                    if (dX > 0) {

                        p.setARGB(255, 255, 255, 255)
                        c.drawRect(
                            itemView.left.toFloat(), itemView.top.toFloat(), dX,
                            itemView.bottom.toFloat(), p
                        )
                    } else {
                        p.setARGB(255, 255, 255, 255)
                        c.drawRect(
                            itemView.right.toFloat() + dX, itemView.top.toFloat(),
                            itemView.right.toFloat(), itemView.bottom.toFloat(), p
                        )
                    }
                }

                super.onChildDraw(
                    c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive
                )
            }

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDir: Int) {
                //Remove swiped item
                val position = viewHolder.adapterPosition

                currentPostList.removeAt(position)

                recycleAdapter.differ.submitList(currentPostList)

                recycleAdapter.notifyDataSetChanged()


            }
        }

        binding.recyclerView.apply {

            binding.recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = recycleAdapter
            val itemTouchHelper = ItemTouchHelper(simpleItemTouchCallback)
            itemTouchHelper.attachToRecyclerView(binding.recyclerView)


        }
    }
}