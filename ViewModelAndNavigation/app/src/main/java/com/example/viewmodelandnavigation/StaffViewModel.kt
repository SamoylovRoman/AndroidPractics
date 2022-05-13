package com.example.viewmodelandnavigation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class StaffViewModel : ViewModel() {

    private val repository = StaffRepository()

    private val staffLiveData = MutableLiveData(repository.generateStaff(5))

    private val showToastLiveData = SingleLiveEvent<Unit>()

    val staff: LiveData<List<Staff>>
        get() = staffLiveData

    val showToast: LiveData<Unit>
        get() = showToastLiveData

    fun addStaff() {
        val newStaff = repository.createStaff()
        val updatedList = listOf(newStaff) + staffLiveData.value.orEmpty()
        staffLiveData.postValue(updatedList)
        showToastLiveData.postValue(Unit)
    }

    fun deleteStaff(position: Int) {
        staffLiveData.postValue(repository.deleteStaff(staffLiveData.value.orEmpty(), position))
    }

    fun isEmptyLiveData(): Boolean = staff.value!!.isEmpty()

    fun getStaff(idItem: Long): Staff = repository.getStaff(staffLiveData.value.orEmpty(), idItem)
}