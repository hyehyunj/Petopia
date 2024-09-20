package com.djhb.petopia.presentation.d_day

import android.app.DatePickerDialog
import android.content.Context
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.icu.util.Calendar
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.djhb.petopia.R
import com.djhb.petopia.databinding.FragmentDDayBinding
import com.rm.rmswitch.RMSwitch
import io.github.muddz.styleabletoast.StyleableToast


class DDayFragment : DialogFragment() {


    private val _binding: FragmentDDayBinding by lazy {
        FragmentDDayBinding.inflate(layoutInflater)
    }
    private val binding get() = _binding
   private lateinit var dDayViewModel: DDayViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dDayViewModel =
            ViewModelProvider(requireParentFragment())[DDayViewModel::class.java]

        //버튼 클릭이벤트
        dDayButtonClickListener()
        //데이터 변화감지
        dDayDataObserver()
        initDialog()
        loadDDayData()
//        setAlarm()


    }


    //버튼 클릭이벤트 함수 : 눌린 버튼에 따라 동작해주는 함수
    private fun dDayButtonClickListener() {

        binding.dDayIvCalendar.setOnClickListener {

            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)
            val listener = DatePickerDialog.OnDateSetListener { datePicker, yy, mm, dd ->
                binding.dDayTvSelectedDate.text = "${yy}. ${mm + 1}. ${dd}"
                dDayViewModel.selectedDate(yy, mm + 1, dd)
            }
            val picker = DatePickerDialog(requireContext(), listener, year, month, day)
            picker.show()
        }

        binding.dDayBtnAlarm.addSwitchObserver(object : RMSwitch.RMSwitchObserver {
            override fun onCheckStateChange(switchView: RMSwitch, isChecked: Boolean) {
                dDayViewModel.updateAlarmSwitch(isChecked)
                StyleableToast.makeText(
                    requireContext(),
                    "${if (isChecked) "디데이 알림이 설정되었습니다." else "디데이 알림이 해제되었습니다."}",
                    R.style.toast_common
                )
                    .show()
            }
        })

        //완료버튼 클릭 이벤트
        binding.dDayBtnComplete.setOnClickListener {
            dDayViewModel.saveDateName(binding.dDayEtSelectedName.text.toString())
            dDayViewModel.updateAlarm(requireActivity())
            dDayViewModel.calculateDate()
            dismiss()
        }

        //
        binding.dDayBtnBack.setOnClickListener {
            dismiss()
        }

    }

    private fun loadDDayData(){
        val dDayModel = dDayViewModel.dDayModelLiveData.value
        if(dDayModel?.date != "") binding.dDayTvSelectedDate.text = dDayModel?.date
        if(dDayModel?.name != "") binding.dDayEtSelectedName.setText(dDayModel?.name)
        if(dDayViewModel.alarmLiveData.value == true) binding.dDayBtnAlarm.isChecked = true
    }

    //데이터 옵저버 함수 : 데이터 변화를 감지해 해당하는 동작을 진행해주는 함수
    private fun dDayDataObserver() {
        dDayViewModel.alarmLiveData.observe(viewLifecycleOwner) {

        }

    }


//    //사용자가 선택한 항목대로 알릴 시간을 설정하는 함수
//    private fun setAlarm() {
//
//                Toast.makeText(
//                    requireContext(),
//                    getString(R.string.alarm_today) + getString(R.string.alarm_selected),
//                    Toast.LENGTH_SHORT
//                ).show()
//                calendar.apply {
//                    timeInMillis = System.currentTimeMillis()
//                    set(Calendar.YEAR, eventDayYear ?: 0)
//                    set(Calendar.MONTH, (eventDayMonth ?: 0) -1) //0부터 시작한다
//                    set(Calendar.DAY_OF_MONTH, eventDayDay ?: 0)
//                    set(Calendar.HOUR_OF_DAY, 0) //24시간으로 지정한다
//                    set(Calendar.MINUTE, 0)
//                    set(Calendar.SECOND, 0)
//                }
//                reserveAlarm()
//                selectedAlarm = 4
//            }
//
//
//
//
//    //알람리시버에 알림을 예약하는 함수
//    @SuppressLint("ScheduleExactAlarm")
//    private fun reserveAlarm() {
//        val alarmManager = requireContext().getSystemService(ALARM_SERVICE) as AlarmManager
//        val intent = Intent(requireContext(), AlarmReceiver::class.java)
//        val pendingIntent = PendingIntent.getBroadcast(
//            requireContext(),
//            0,
//            intent,
//            PendingIntent.FLAG_MUTABLE
//        )
//        alarmManager.setExactAndAllowWhileIdle(
//            AlarmManager.RTC_WAKEUP,
//            calendar.timeInMillis, pendingIntent
//        )
//    }


    //다이얼로그 초기화 함수 : 화면에 맞춰 갤러리 표현
    private fun initDialog() {
        val windowManager =
            requireContext().getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = windowManager.defaultDisplay

        val size = Point()
        display.getSize(size)
        size.x
        size.y
        val params: ViewGroup.LayoutParams? = dialog?.window?.attributes
        val deviceWidth = size.x
        params?.width = (deviceWidth * 0.9).toInt()
        params?.height = (deviceWidth * 1.1).toInt()
        dialog?.window?.attributes = params as WindowManager.LayoutParams
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

}