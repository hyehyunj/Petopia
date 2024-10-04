package com.djhb.petopia.presentation.d_day

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.djhb.petopia.data.AlarmLocalDataSource
import com.djhb.petopia.data.DDayModel
import com.djhb.petopia.data.LoginData
import com.djhb.petopia.data.remote.DDayRepository
import com.djhb.petopia.data.remote.DDayRepositoryImpl
import com.djhb.petopia.data.remote.SignRepository
import com.djhb.petopia.data.remote.SignRepositoryImpl
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit


//디데이 뷰모델
class DDayViewModel(private val dDayRepository: DDayRepository,
    private val signRepository: SignRepository) :
    ViewModel() {

    private val user = LoginData.loginUser

    //사용자 지정 날짜
    private val _userDateLiveData = MutableLiveData<LocalDate>()
    val userDateLiveData: LiveData<LocalDate> = _userDateLiveData

    //디데이
    private val _dDayLiveData = MutableLiveData("")
    val dDayLiveData: LiveData<String> = _dDayLiveData
    private lateinit var dDayList : LocalDate

    //알림
    private val _alarmLiveData = MutableLiveData(false)
    val alarmLiveData: LiveData<Boolean> = _alarmLiveData
        private var alarmSwitch = false

    //디데이모델
    private val _dDayModelLiveData = MutableLiveData(DDayModel())
    val dDayModelLiveData: LiveData<DDayModel> = _dDayModelLiveData

    //사용자 날짜를 불러오는 함수
    fun loadUserDate() {
        _dDayModelLiveData.value = user.dday ?: DDayModel()
    }

    //선택된 날짜 담는 함수
    fun selectedDate(y:Int, m:Int, d:Int) {
        dDayList = LocalDate.of(y, m, d)
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val dateString = dDayList.format(formatter)
        _dDayModelLiveData.value = _dDayModelLiveData.value?.copy(date = dateString)
    }

    //이름을 담는 함수
    fun saveDateName(dateName: String) {
        _dDayModelLiveData.value = _dDayModelLiveData.value?.copy(name = dateName)
    }


    //디데이 계산해주는 함수
    fun calculateDate() {
        val dDayDate = _dDayModelLiveData.value?.date
        if (!dDayDate.isNullOrBlank()) {
            try {
                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                _userDateLiveData.value = LocalDate.parse(dDayDate, formatter)
                val date = _userDateLiveData.value
                val today = LocalDate.now()
                val calculateResult = ChronoUnit.DAYS.between(today, date)
                _dDayLiveData.value = if (calculateResult >= 0) "-${calculateResult}" else "+${-calculateResult}"
                updateDDayModel()
            } catch (e: Exception) {
                Log.e("DDayViewModel", "날짜 파싱 오류: ${e.message}")
            }
        } else {
            Log.e("DDayViewModel", "디데이 날짜가 비어있거나 잘못되었습니다.")
            // 추가적인 에러 처리를 여기에 구현할 수 있습니다.
        }
    }


    //디데이 설정 저장하는 함수
    private fun updateDDayModel() {
        user.dday = _dDayModelLiveData.value
        viewModelScope.launch {
            signRepository.updateUser(user)
        }
    }

    //알림설정 클릭상태를 담아주는 함수
    fun updateAlarmSwitch(isChecked: Boolean) {
        alarmSwitch = isChecked
    }

    //디데이 알림 설정해주는 함수
    fun updateAlarm(context: Context) {
        _alarmLiveData.value = dDayRepository.updateAlarm(context, alarmSwitch)
    }

    fun loadAlarm(context: Context) {
        _alarmLiveData.value = dDayRepository.loadAlarm(context)
    }

}


class DDayViewModelFactory : ViewModelProvider.Factory {
    private val dDayRepository = DDayRepositoryImpl(AlarmLocalDataSource)
    private val signRepository = SignRepositoryImpl()

    override fun <T : ViewModel> create(
        modelClass: Class<T>,
        extras: CreationExtras
    ): T {
        return DDayViewModel(
            dDayRepository,signRepository
        ) as T
    }
}