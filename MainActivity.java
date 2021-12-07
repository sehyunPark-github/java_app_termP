package com.example.myfirstapp; //무슨뜻이지

import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Locale;

/**
 *@MainActivity
 *@brief 디데이 추가/수정/공유, 컬러설정, 사진/카메라 설정
 *@date 2016.02.18
 *@details
 */
public class MainActivity extends Activity {

    int dateEndY, dateEndM, dateEndD;
    int ddayValue = 0;

    // 현재 날짜를 알기 위해 사용
    Calendar calendar;
    int currentYear, currentMonth, currentDay;

    // 밀리세컨드형태의 하루(24 시간), 디데이계산하려면 밀리세컨드여야함
    private final int ONE_DAY = 24 * 60 * 60 * 1000; //변경불가

    TextView edit_endDateBtn, edit_result;
    Button datePicker; //날짜선택버튼

    @Override //오버라이드
    public void onCreate(Bundle savedInstanceState) { //액티비티간에 데이터 주고받을 때 번들클래스 사용하여 데이터 전송
        super.onCreate(savedInstanceState); //상위클래스의 생성자 호출, 이전상태 포함 번들
        setContentView(R.layout.activity_main); //사용자가 볼 레이아웃 메서드도 호출

        //시작일, 종료일 데이터 저장
        calendar = Calendar.getInstance(); //캘린더인스턴스 생성
        currentYear = calendar.get(Calendar.YEAR); //현재년도
        currentMonth = (calendar.get(Calendar.MONTH)); //현재월
        currentDay = calendar.get(Calendar.DAY_OF_MONTH); //현재일

        datePicker = (Button) findViewById(R.id.datePicker);
        edit_endDateBtn = (TextView) findViewById(R.id.edit_endDateBtn); //입력날짜표시하는곳
        edit_result = (TextView) findViewById(R.id.edit_result); //디데이결과표시하는 곳

        //한국어 설정 (ex: date picker)
        Locale.setDefault(Locale.KOREAN);

        // 디데이 날짜 입력(처음세팅 오늘날짜)
        edit_endDateBtn.setText(currentYear + "년 " + (currentMonth + 1) + "월 " + currentDay + "일");

        //datePicker : 디데이 날짜 입력하는 버튼, 클릭시 DatePickerDialog 띄우기
        datePicker.setOnClickListener(new View.OnClickListener() {
            @Override //이 메서드가 기반형식에 정의한 메서드를 덮어씀,이거만 실행
            public void onClick(View view) {
                new DatePickerDialog(MainActivity.this, endDateSetListener, (currentYear), (currentMonth), currentDay).show();
            }
        });

    }


    /** @brief endDateSetListener
     *  @date 2021-12-5
     *  @detail DatePickerDialog띄우기, 종료일 저장, 기존에 입력한 값이 있으면 해당 데이터 설정후 띄우기
     */
    private DatePickerDialog.OnDateSetListener endDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            edit_endDateBtn.setText(year + "년 " + (monthOfYear + 1) + "월 " + dayOfMonth + "일");

            ddayValue = ddayResult_int(dateEndY, dateEndM, dateEndD);

            edit_result.setText(getDday(year, monthOfYear, dayOfMonth));
        }
    };


    /** @brief getDday
     *  @date 2016-02-18
     *  @param mYear : 설정한 디데이 year, mMonthOfYear : 설정한 디데이 MonthOfYear, mDayOfMonth : 설정한 디데이 DayOfMonth
     *  @detail D-day 반환
     */
    private String getDday(int mYear, int mMonthOfYear, int mDayOfMonth) {

        // D-day 설정
        final Calendar ddayCalendar = Calendar.getInstance();
        ddayCalendar.set(mYear, mMonthOfYear, mDayOfMonth);

        // D-day 를 구하기 위해 millisecond 으로 환산하여 d-day 에서 today 의 차를 구한다.
        final long dday = ddayCalendar.getTimeInMillis() / ONE_DAY; //(하루)의 밀리초수
        final long today = Calendar.getInstance().getTimeInMillis() / ONE_DAY;
        long result = dday - today;

        // 출력 시 d-day 에 맞게 표시
        String strFormat;
        if (result > 0) {
            strFormat = "D-%d";
        } else if (result == 0) {
            strFormat = "오늘입니다";
        } else {
            result *= -1;
            strFormat = "D+%d";
        }

        final String strCount = (String.format(strFormat, result));

        return strCount;
    }


    /** @brief onPhotoDialog
     *  @date 2016-02-18
     *  @detail 디데이 값 계산
     *  */
    public int onCalculatorDate (int dateEndY, int dateEndM, int dateEndD) {
        try {
            Calendar today = Calendar.getInstance(); //현재 오늘 날짜
            Calendar dday = Calendar.getInstance();

            //시작일, 종료일 데이터 저장
            Calendar calendar = Calendar.getInstance();
            int cyear = calendar.get(Calendar.YEAR);
            int cmonth = (calendar.get(Calendar.MONTH) + 1);
            int cday = calendar.get(Calendar.DAY_OF_MONTH);

            today.set(cyear, cmonth, cday);
            dday.set(dateEndY, dateEndM, dateEndD);// D-day의 날짜를 입력합니다.

            long day = dday.getTimeInMillis() / 86400000;
            // 각각 날의 시간 값을 얻어온 다음
            //( 1일의 값(86400000 = 24시간 * 60분 * 60초 * 1000(1초값) ) )

            long tday = today.getTimeInMillis() / 86400000;
            long count = tday - day; // 오늘 날짜에서 dday 날짜를 빼주게 됩니다.
            return (int) count; // 날짜는 하루 + 시켜줘야합니다.
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }


    /** @brief ddayResult_int
     *  @date 2016-02-18
     *  @detail 디데이 값 계산한 값 결과값 출력
     *  Todo 함수 오류 수정
     *  */
    public int ddayResult_int(int dateEndY, int dateEndM, int dateEndD) {
        int result = 0;

        result = onCalculatorDate(dateEndY, dateEndM, dateEndD);

        return result;
    }
}