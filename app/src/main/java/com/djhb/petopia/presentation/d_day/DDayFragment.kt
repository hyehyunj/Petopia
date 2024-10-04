package com.djhb.petopia.presentation.d_day

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.DatePickerDialog
import android.app.PendingIntent
import android.content.Context
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.djhb.petopia.R
import com.djhb.petopia.databinding.FragmentDDayBinding
import com.djhb.petopia.presentation.memory.MemoryFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.rm.rmswitch.RMSwitch
import io.github.muddz.styleabletoast.StyleableToast
import java.text.SimpleDateFormat
import java.util.Locale

//디데이 프래그먼트 : 생일, 기일 등 날짜를 입력하면 알려주는 프래그먼트
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
        loadDDayData()


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
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && !NotificationManagerCompat.from(
                        requireContext()
                    ).areNotificationsEnabled()
                )
                {binding.dDayBtnAlarm.isChecked = false
                    requireAlarmPermission()}
                else {
                    dDayViewModel.updateAlarmSwitch(isChecked)
                    StyleableToast.makeText(
                        requireContext(),
                        "${if (isChecked) "디데이 알림이 설정되었습니다." else "디데이 알림이 해제되었습니다."}",
                        R.style.toast_common
                    )
                        .show()
                }
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

    //알림권한 허가를 요청하는 함수
    private fun requireAlarmPermission() {
        DDayDialogFragment().show(childFragmentManager, "DDAY_DIALOG_FRAGMENT")
    }

    //디데이 데이터를 불러와주는 함수
    private fun loadDDayData(){
        val dDayModel = dDayViewModel.dDayModelLiveData.value
        if(dDayModel?.date != "") binding.dDayTvSelectedDate.text = dDayModel?.date
        if(dDayModel?.name != "") binding.dDayEtSelectedName.setText(dDayModel?.name)
        if(dDayViewModel.alarmLiveData.value == true) binding.dDayBtnAlarm.isChecked = true
    }

    //데이터 옵저버 함수 : 데이터 변화를 감지해 해당하는 동작을 진행해주는 함수
    private fun dDayDataObserver() {
        dDayViewModel.alarmLiveData.observe(viewLifecycleOwner) {
            if(it) dDayViewModel.dDayModelLiveData.value?.let { dDayModel -> setAlarm(dDayModel.date) }
        }

    }


    //사용자가 선택한 항목대로 알릴 시간을 설정하는 함수
    private fun setAlarm(selectedDate: String) {
        val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date = format.parse(selectedDate)
        val calendar = Calendar.getInstance().apply {
            time = date
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
                reserveAlarm(calendar)
            }

    //알람리시버에 알림을 예약하는 함수
    @SuppressLint("ScheduleExactAlarm")
    private fun reserveAlarm(calendar:Calendar) {
        val alarmManager = requireContext().getSystemService(ALARM_SERVICE) as AlarmManager
        val intent = Intent(requireContext(), DDayAlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            requireContext(),
            0,
            intent,
            PendingIntent.FLAG_MUTABLE
        )
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis, pendingIntent
        )
    }


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

    override fun onResume() {
        super.onResume()
        initDialog()
    }

}