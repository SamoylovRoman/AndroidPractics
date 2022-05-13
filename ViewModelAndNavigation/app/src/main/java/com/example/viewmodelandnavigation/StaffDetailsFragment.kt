package com.example.viewmodelandnavigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.viewmodelandnavigation.databinding.FragmentStaffDetailsBinding

class StaffDetailsFragment : Fragment() {
    private var _binding: FragmentStaffDetailsBinding? = null
    private val binding get() = _binding!!

    private val args: StaffDetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStaffDetailsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.idTextView.text = args.staffid.toString()
        showDetailInfo(args.staffItem)
        initSkipButtonListener()
    }

    private fun initSkipButtonListener() {
        binding.skipButton.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun showDetailInfo(staffItem: Staff) {
        when (staffItem) {
            is Staff.Manager -> {
                with(binding) {
                    idTextView.text = staffItem.id.toString()
                    Glide.with(root)
                        .load(staffItem.photoLink)
                        .placeholder(R.drawable.ic_download)
                        .error(R.drawable.ic_error)
                        .into(avatarImageView)
                    fullNameViewText.text = staffItem.fullName
                    managementTeamTextView.visibility = View.VISIBLE
                    positionViewText.text = staffItem.position
                    phoneNumberViewText.text = staffItem.phoneNumber
                    emailViewText.text = staffItem.emailAddress
                }
            }
            is Staff.Employee -> {
                with(binding) {
                    idTextView.text = staffItem.id.toString()
                    Glide.with(root)
                        .load(staffItem.photoLink)
                        .placeholder(R.drawable.ic_download)
                        .error(R.drawable.ic_error)
                        .into(avatarImageView)
                    fullNameViewText.text = staffItem.fullName
                    managementTeamTextView.visibility = View.GONE
                    positionViewText.text = staffItem.position
                    phoneNumberViewText.text = staffItem.phoneNumber
                    emailViewText.visibility = View.GONE
                }
            }
        }
    }
}