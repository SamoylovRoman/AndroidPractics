package com.example.viewmodelandnavigation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class StaffViewModel : ViewModel() {

    private val repository = StaffRepository()

    private val _staff = MutableLiveData(repository.generateStaff(5))

    private val _showToast = SingleLiveEvent<Unit>()

    val staff: LiveData<List<Staff>>
        get() = _staff

    val showToast: LiveData<Unit>
        get() = _showToast

    fun addStaff() {
        val newStaff = repository.createStaff()
        val updatedList = listOf(newStaff) + _staff.value.orEmpty()
        _staff.postValue(updatedList)
        _showToast.postValue(Unit)
    }

    fun deleteStaff(position: Int) {
        _staff.postValue(repository.deleteStaff(_staff.value.orEmpty(), position))
    }

    fun isEmptyLiveData(): Boolean = staff.value!!.isEmpty()

    fun getStaff(idItem: Long): Staff = repository.getStaff(_staff.value.orEmpty(), idItem)
}