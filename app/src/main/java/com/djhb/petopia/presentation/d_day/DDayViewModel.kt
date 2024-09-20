package com.djhb.petopia.presentation.d_day

import android.content.Context
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.djhb.petopia.data.AlarmLocalDataSource
import com.djhb.petopia.data.DDayModel
import com.djhb.petopia.data.LoginData
import com.djhb.petopia.data.remote.DDayRepository
import com.djhb.petopia.data.remote.DDayRepositoryImpl
import com.djhb.petopia.data.remote.GalleryRepositoryImpl
import java.time.LocalDate
import java.time.temporal.ChronoUnit

//알람 뷰모델
class DDayViewModel(private val dDayRepository: DDayRepository) :
    ViewModel() {

    private val user = LoginData.loginUser

    //사용자 지정 날짜
    private val _userDateLiveData = MutableLiveData("")
    val userDateLiveData: LiveData<String> = _userDateLiveData

    //디데이
    private val _dDayLiveData = MutableLiveData("")
    val dDayLiveData: LiveData<String> = _dDayLiveData
    private var dDayList = mutableListOf<Int>()

    //알림
    private val _alarmLiveData = MutableLiveData(false)
    val alarmLiveData: LiveData<Boolean> = _alarmLiveData
        private var alarmSwitch = false

    //디데이모델
    private val _dDayModelLiveData = MutableLiveData<DDayModel>()
    val dDayModelLiveData: LiveData<DDayModel> = _dDayModelLiveData

    //사용자 디데이를 불러오는 함수
    fun loadUserDate() {
        _dDayModelLiveData.value = user.dDay ?: DDayModel()
    }

    //선택된 날짜 담는 함수
    fun saveSelectedDate(selectedDate: String) {
        _dDayModelLiveData.value = _dDayModelLiveData.value?.copy(date = selectedDate)
        Log.d("뷰모델", "${_dDayModelLiveData.value}")
    }

    fun prepareDDay(y:Int, m:Int, d:Int) {
        dDayList = mutableListOf()
        dDayList.add(y)
        dDayList.add(m)
        dDayList.add(d)
    }

    //이름을 담는 함수
    fun saveDateName(dateName: String) {
        _dDayModelLiveData.value = _dDayModelLiveData.value?.copy(name = dateName)
    }


    //디데이 계산해주는 함수
    fun calculateDate() {
        _userDateLiveData.value = _dDayModelLiveData.value?.date
        val date = LocalDate.of(dDayList[0], dDayList[1], dDayList[2])
        val today = LocalDate.now()
        val calculator = ChronoUnit.DAYS.between(today, date)

        _dDayLiveData.value = calculator.toString()
        updateDDayModel()
    }

    //디데이 설정 저장하는 함수
    private fun updateDDayModel() {
        user.dDay = _dDayModelLiveData.value
    }

    fun updateAlarmSwitch(isChecked: Boolean) {
        alarmSwitch = isChecked
    }

    //디데이 알림 설정해주는 함수
    fun updateAlarm(context: Context) {
        _alarmLiveData.value = dDayRepository.updateAlarm(context, alarmSwitch)
        Log.d("알람","${_alarmLiveData.value}")
    }

    fun loadAlarm(context: Context) {
        _alarmLiveData.value = dDayRepository.loadAlarm(context)
        Log.d("불러오셈","${_alarmLiveData.value}")
    }

}


class DDayViewModelFactory : ViewModelProvider.Factory {
    private val dDayRepository = DDayRepositoryImpl(AlarmLocalDataSource)

    override fun <T : ViewModel> create(
        modelClass: Class<T>,
        extras: CreationExtras
    ): T {
        return DDayViewModel(
            dDayRepository
        ) as T
    }
}