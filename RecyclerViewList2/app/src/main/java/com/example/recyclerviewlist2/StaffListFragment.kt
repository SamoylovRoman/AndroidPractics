package com.example.recyclerviewlist2

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import androidx.recyclerview.widget.*
import com.example.recyclerviewlist2.databinding.FragmentStaffListBinding
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator

class StaffListFragment : Fragment(), AddDialogListener {

    private var staff: List<Staff> = listOf(
        Staff.Manager(
            1,
            "Sergey Usoltcev",
            "Press-atashe",
            true,
            "8 (923) 342-21-21",
            "testmail@mail.ru",
            "https://www.nsktv.ru/upload/resize_cache/iblock/6df/450_450_0/6df7766c68f1c30d7cd674d58da8ba1c.png"
        ),
        Staff.Employee(
            2,
            "Darya Usoltceva",
            "Call-center manager",
            false,
            "8 (913) 342-41-11",
            "https://sun9-2.userapi.com/impf/K4bTKaWxLPFkw8wHwp0F7ZtH0CEeXXt54AwSxA/Kh554yEJ3ic.jpg?size=1439x2160&quality=95&sign=ab4288b7fcae7bb499c7f47558cf14f8&type=album"
        ),
        Staff.Manager(
            3,
            "Roman Samoilov",
            "Marketer",
            true,
            "8 (913) 342-41-118 (913) 342-41-118 (913) 342-41-11",
            "testmail@mail.ru",
            "https://sun9-16.userapi.com/impg/c858528/v858528522/152613/1Nmu87A22PM.jpg?size=1440x2160&quality=96&sign=5e9467fc6ac620bf5b388925fa0ab9ea&type=album"
        ),
        Staff.Employee(
            4,
            "Roman Strelnik",
            "Trainer",
            false,
            "8 (960) 106-06-07",
            "https://sun9-81.userapi.com/impf/c840527/v840527009/28db3/rCIb7tnnOcc.jpg?size=588x588&quality=96&sign=793ffcdee101edaa71b3b466f4caae60&type=album"
        ),
        Staff.Employee(
            5,
            "Aleksandr Vasiljev",
            "Trainer",
            false,
            "8 (913) 223-06-07",
            "https://sun9-40.userapi.com/impf/c834104/v834104213/1ff71/PvIv_eAEKxk.jpg?size=1944x1944&quality=96&sign=489afce38968a41060288ea4926dc4a1&type=album"
        ),
        Staff.Employee(
            6,
            "Nikolaj Drudginin",
            "Trainer",
            false,
            "8 (966) 223-23-54",
            "https://sun9-29.userapi.com/impf/DWWiINEiQ7uOcevlbTfnjIha8MSKAxpmfelP1A/SPYTZbHTKd0.jpg?size=960x1200&quality=96&sign=5d81648bb18cd4b64cbe833f6f393e2b&type=album"
        )
    )

    private var _binding: FragmentStaffListBinding? = null
    private val binding get() = _binding!!

    private var staffAdapter: StaffAdapterWithBinding? = null

    private lateinit var layoutToShow: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            layoutToShow = it.getString(LAYOUT_TO_SHOW)!!
        }
        if (savedInstanceState != null) {
            staff = savedInstanceState.getParcelableArrayList<Staff>(STAFF_LIST_TAG) as List<Staff>
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStaffListBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initList()
        initListeners()
    }

    private fun initList() {
        staffAdapter = StaffAdapterWithBinding { position -> deleteStaff(position) }
        staffAdapter?.items = staff
        with(binding.staffList) {
            adapter = staffAdapter
            when (layoutToShow) {
                LINEAR_LAYOUT_TO_SHOW -> {
                    initLinearLayout(this)
                }
                GRID_LAYOUT_TO_SHOW -> {
                    initGridLayout(this)
                }
                STAGGERED_GRID_LAYOUT_TO_SHOW -> {
                    initStaggeredGridLayout(this)
                }
                else -> {
                    initLinearLayout(this)
                }
            }
            setHasFixedSize(true)
            itemAnimator = SlideInLeftAnimator()
        }
        updateEmptyTextView()
    }

    private fun initStaggeredGridLayout(recyclerView: RecyclerView) {
        val staggeredGridLayout = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        with(recyclerView) {
            layoutManager = staggeredGridLayout
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    Log.d("onScrolled", "dx = $dx, dy = $dy")
                    Log.d("onScrolled", "Focused child = ${staggeredGridLayout.gapStrategy}")

//                    staggeredGridLayout.findFirstCompletelyVisibleItemPositions(IntArray(staff.size))
/*                    Log.d(
                        "onScrolled",
                        "first one = ${
                            staggeredGridLayout.findFirstCompletelyVisibleItemPositions(
                                IntArray(
                                    staff.size
                                )
                            )
                        }"
                    )*/
                }
            })
/*            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    val totalItemCount = staggeredGridLayout.itemCount
                    val lastVisibleItem = staggeredGridLayout.childCount
                    if (lastVisibleItem == totalItemCount - 1 &&
                        totalItemCount < MAX_ITEMS_COUNT_TO_LOAD
                    ) {
                        loadNextRandomItems(ITEMS_COUNT_TO_LOAD)
                    }
                }
            })*/
        }
    }

    private fun initGridLayout(recyclerView: RecyclerView) {
        val gridLayout = GridLayoutManager(context, 2)
        with(recyclerView) {
            layoutManager = gridLayout
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    val totalItemCount = gridLayout.itemCount
                    val lastVisibleItem = gridLayout.findLastVisibleItemPosition()
                    if (lastVisibleItem == totalItemCount - 1 &&
                        totalItemCount < MAX_ITEMS_COUNT_TO_LOAD
                    ) {
                        loadNextRandomItems(ITEMS_COUNT_TO_LOAD)
                    }
                }
            })
            addItemDecoration(
                DividerItemDecoration(
                    requireContext(),
                    DividerItemDecoration.VERTICAL
                )
            )
            addItemDecoration(
                DividerItemDecoration(
                    requireContext(),
                    DividerItemDecoration.HORIZONTAL
                )
            )
            addItemDecoration(ItemOffsetDecoration(context))
        }
    }

    private fun initLinearLayout(recyclerView: RecyclerView) {
        val linearLayout = LinearLayoutManager(context)
        with(recyclerView) {
            layoutManager = linearLayout
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(
                    recyclerView: RecyclerView,
                    newState: Int
                ) {
                    super.onScrollStateChanged(recyclerView, newState)
                    val totalItemCount = linearLayout.itemCount
                    val lastVisibleItem = linearLayout.findLastVisibleItemPosition()
                    if (lastVisibleItem == totalItemCount - 1 &&
                        totalItemCount < MAX_ITEMS_COUNT_TO_LOAD
                    ) {
                        loadNextRandomItems(ITEMS_COUNT_TO_LOAD)
                    }
                }
            })
            addItemDecoration(
                DividerItemDecoration(
                    requireContext(),
                    DividerItemDecoration.VERTICAL
                )
            )
        }
    }

    private fun loadNextRandomItems(count: Int) {
        val newStaff =
            if (count <= (MAX_ITEMS_COUNT_TO_LOAD - staff.size)) List(count) { staff.random() }
            else List(MAX_ITEMS_COUNT_TO_LOAD - staff.size) { staff.random() }
        staff = staff + newStaff
        staffAdapter?.items = staff
    }

    private fun initListeners() {
        binding.addFloatButton.setOnClickListener {
            val dialog = AddStaffDialogFragment()
            dialog.show(childFragmentManager, AddStaffDialogFragment.DIALOG_TAG)
        }
    }

    private fun addNewStaff(newStaff: Staff) {
        staff = listOf(newStaff) + staff
        staffAdapter?.items = staff
        binding.staffList.post { binding.staffList.scrollToPosition(0) }
        updateEmptyTextView()
    }

    private fun deleteStaff(position: Int) {
        staff = staff.filterIndexed { index, _ -> index != position }
        staffAdapter?.items = staff
        updateEmptyTextView()
    }

    private fun updateEmptyTextView() {
        if (staff.isEmpty()) {
            binding.emptyListText.visibility = View.VISIBLE
        } else binding.emptyListText.visibility = View.GONE
    }

    override fun onAddButtonClicked(newStaff: Staff) {
        addNewStaff(newStaff)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        staffAdapter = null
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val toBundle = staff.toCollection(ArrayList())
        outState.putParcelableArrayList(STAFF_LIST_TAG, toBundle)
    }

    companion object {
        const val STAFF_LIST_TAG = "Staff list"
        const val LAYOUT_TO_SHOW = "Layout to show"
        const val LINEAR_LAYOUT_TO_SHOW = "LinearLayout"
        const val GRID_LAYOUT_TO_SHOW = "GridLayout"
        const val STAGGERED_GRID_LAYOUT_TO_SHOW = "StaggeredGridLayout"
        const val ITEMS_COUNT_TO_LOAD = 3
        const val MAX_ITEMS_COUNT_TO_LOAD = 12

        fun newInstance(layoutToShow: String) =
            StaffListFragment().apply {
                arguments = Bundle().apply {
                    putString(LAYOUT_TO_SHOW, layoutToShow)
                }
            }
    }
}