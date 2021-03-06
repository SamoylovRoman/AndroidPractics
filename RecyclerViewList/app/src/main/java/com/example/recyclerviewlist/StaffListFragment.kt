package com.example.recyclerviewlist

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recyclerviewlist.databinding.FragmentStaffListBinding

class StaffListFragment : Fragment(), AddDialogListener {

    private var staff: List<Staff> = listOf(
        Staff.Manager(
            "Sergey Usoltcev",
            "Press-atashe",
            true,
            "8 (923) 342-21-21",
            "testmail@mail.ru",
            "https://www.nsktv.ru/upload/resize_cache/iblock/6df/450_450_0/6df7766c68f1c30d7cd674d58da8ba1c.png"
        ),
        Staff.Employee(
            "Darya Usoltceva",
            "Call-center manager",
            false,
            "8 (913) 342-41-11",
            "https://sun9-2.userapi.com/impf/K4bTKaWxLPFkw8wHwp0F7ZtH0CEeXXt54AwSxA/Kh554yEJ3ic.jpg?size=1439x2160&quality=95&sign=ab4288b7fcae7bb499c7f47558cf14f8&type=album"
        ),
        Staff.Manager(
            "Roman Samoilov",
            "Marketer",
            true,
            "8 (923) 701-01-07",
            "testmail@mail.ru",
            "https://sun9-16.userapi.com/impg/c858528/v858528522/152613/1Nmu87A22PM.jpg?size=1440x2160&quality=96&sign=5e9467fc6ac620bf5b388925fa0ab9ea&type=album"
        ),
        Staff.Employee(
            "Roman Strelnik",
            "Trainer",
            false,
            "8 (960) 106-06-07",
            "https://sun9-81.userapi.com/impf/c840527/v840527009/28db3/rCIb7tnnOcc.jpg?size=588x588&quality=96&sign=793ffcdee101edaa71b3b466f4caae60&type=album"
        ),
        Staff.Employee(
            "Aleksandr Vasiljev",
            "Trainer",
            false,
            "8 (913) 223-06-07",
            "https://sun9-40.userapi.com/impf/c834104/v834104213/1ff71/PvIv_eAEKxk.jpg?size=1944x1944&quality=96&sign=489afce38968a41060288ea4926dc4a1&type=album"
        ),
        Staff.Employee(
            "Nikolaj Drudginin",
            "Trainer",
            false,
            "8 (966) 223-23-54",
            "https://sun9-29.userapi.com/impf/DWWiINEiQ7uOcevlbTfnjIha8MSKAxpmfelP1A/SPYTZbHTKd0.jpg?size=960x1200&quality=96&sign=5d81648bb18cd4b64cbe833f6f393e2b&type=album"
        ),
    )

    private var _binding: FragmentStaffListBinding? = null
    private val binding get() = _binding!!

    private var staffAdapter: StaffAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

    @SuppressLint("NotifyDataSetChanged")
    private fun initList() {
        staffAdapter = StaffAdapter { position -> deleteStaff(position) }
        staffAdapter?.updateStaff(staff)
        staffAdapter?.notifyDataSetChanged()
        with(binding.staffList) {
            adapter = staffAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }
        updateEmptyTextView()
    }

    private fun initListeners() {
        binding.addFloatButton.setOnClickListener {
            val dialog = AddStaffDialogFragment()
            dialog.show(childFragmentManager, AddStaffDialogFragment.DIALOG_TAG)
        }
    }

    private fun addNewStaff(newStaff: Staff) {
        staff = listOf(newStaff) + staff
        staffAdapter?.updateStaff(staff)
        staffAdapter?.notifyItemInserted(0)
        binding.staffList.scrollToPosition(0)
        updateEmptyTextView()
    }

    private fun deleteStaff(position: Int) {
        staff = staff.filterIndexed { index, _ -> index != position }
        staffAdapter?.updateStaff(staff)
        staffAdapter?.notifyItemRemoved(position)
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
        fun newInstance() = StaffListFragment()
    }
}