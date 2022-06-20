package com.skillbox.github.ui.detail_repo

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.skillbox.github.R
import com.skillbox.github.databinding.FragmentDetailRepoBinding

class DetailRepoFragment : Fragment() {
    private var _binding: FragmentDetailRepoBinding? = null
    private val binding get() = _binding!!

    private val args: DetailRepoFragmentArgs by navArgs()

    private val userModel: DetailRepoViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailRepoBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setRepoInfo()
        bindViewModel()
        initStarListener()
        checkIsStarredRepo()
    }

    @SuppressLint("SetTextI18n")
    private fun setRepoInfo() {
        with(binding) {
            repoDetailMainText.text = "${repoDetailMainText.text} \n(id: ${args.repo.id})"
            repoDetailName.text = "${repoDetailName.text} ${args.repo.name}"
            repoDetailFullName.text = args.repo.fullName
            Glide.with(root)
                .load(args.repo.owner.avatar)
                .placeholder(R.drawable.ic_download)
                .error(R.drawable.ic_error)
                .into(repoDetailOwnerImage)
            repoDetailOwnerName.text = "${repoDetailOwnerName.text} ${args.repo.owner.ownerName}"
        }
    }

    private fun bindViewModel() {
        userModel.repo.observe(viewLifecycleOwner) {
        }
        userModel.repoIsStarred.observe(viewLifecycleOwner) { isStarred ->
            updateStarState(isStarred)
        }
        userModel.errorString.observe(viewLifecycleOwner) { errorString ->
            setErrorString(errorString)
        }
    }

    private fun initStarListener() {
        binding.repoIsStarredImage.setOnClickListener {
            if (userModel.repoIsStarred.value!!) {
                userModel.unsetRepoIsStarred(args.repo.owner.ownerName, args.repo.name)
            } else {
                userModel.setRepoIsStarred(args.repo.owner.ownerName, args.repo.name)
            }
        }
    }

    private fun checkIsStarredRepo() {
        userModel.checkRepoIsStarred(args.repo.owner.ownerName, args.repo.name)
    }

    private fun setErrorString(errorString: String) {
        binding.errorStarText.text = errorString
        binding.errorStarText.isVisible = true
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun updateStarState(isStarred: Boolean) {
        binding.errorStarText.isVisible = false
        if (isStarred) {
            binding.repoIsStarredImage.setImageDrawable(
                resources.getDrawable(R.drawable.ic_filled_star, context?.theme)
            )
            binding.starredText.text = getString(R.string.starred)
        } else {
            binding.repoIsStarredImage.setImageDrawable(
                resources.getDrawable(R.drawable.ic_clear_star, context?.theme)
            )
            binding.starredText.text = getString(R.string.not_starred)
        }
    }
}