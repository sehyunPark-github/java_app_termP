package com.example.myfirstapp;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import java.util.Calendar;

public class MainActivity extends Activity { //simple starting point

    // 오늘 연,월,일
    Calendar calendar;
    int currentYear, currentMonth, currentDay;

    //이벤트 년, 월, 일
    int dateEndY, dateEndM, dateEndD;
    int ddayValue = 0;

    //디데이 계산하려면 밀리세컨드여야함
    private final int ONE_DAY = 24 * 60 * 60 * 1000; //변경불가(final)

    //쓸 레이아웃(?) 선언
    TextView edit_eventDate, edit_result, edit_Month;
    Button datePicker; //날짜선택버튼

    @Override //simple starting point
    public void onCreate(Bundle savedInstanceState) { //simple starting point
        super.onCreate(savedInstanceState); //simple starting point
        setContentView(R.layout.activity_main); //사용자가 볼 레이아웃 메서드도 호출

        //시작일, 종료일 저장
        calendar = Calendar.getInstance(); //캘린더인스턴스 생성
        currentYear = calendar.get(Calendar.YEAR); //현재년
        currentMonth = calendar.get(Calendar.MONTH); //현재월
        currentDay = calendar.get(Calendar.DAY_OF_MONTH); //현재일

        //setText할때 필요한 변수
        datePicker = (Button) findViewById(R.id.datePicker); //날짜선택버튼
        edit_eventDate = (TextView) findViewById(R.id.edit_endDateBtn); //이벤트날짜표시영역
        edit_result = (TextView) findViewById(R.id.edit_result); //D-?표시영역
        edit_Month = (TextView) findViewById(R.id.edit_Month); //몇달남았는지 표시영역

        // 디데이 날짜 입력(처음세팅:오늘날짜)
        edit_eventDate.setText(currentYear + "년 " + (currentMonth + 1) + "월 " + currentDay + "일");

        //클릭시 DatePickerDialog 띄우기
        datePicker.setOnClickListener(new View.OnClickListener() {
            @Override //오버라이드
            public void onClick(View view) {
                new DatePickerDialog(MainActivity.this, eventDateSetListener, (currentYear), (currentMonth), currentDay).show();
            }
        });

    }
    //유저가 날짜를 선택하면
    private DatePickerDialog.OnDateSetListener eventDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            edit_eventDate.setText(year + "년 " + (monthOfYear + 1) + "월 " + dayOfMonth + "일");

            //ddayValue = ddayResult_int(dateEndY, dateEndM, dateEndD);

            edit_result.setText(getDday(year, monthOfYear, dayOfMonth));
            edit_Month.setText(getDMonth(year, monthOfYear, dayOfMonth));

        }
    };

    private String getDday(int mYear, int mMonthOfYear, int mDayOfMonth) {

        // 디데이 설정
        final Calendar ddayCalendar = Calendar.getInstance();
        ddayCalendar.set(mYear, mMonthOfYear, mDayOfMonth);

        //밀리세컨드 단위로 바꾸고 d-day-today, result:일 단위
        final long dday = ddayCalendar.getTimeInMillis() / ONE_DAY; //이벤트날짜
        final long today = Calendar.getInstance().getTimeInMillis() / ONE_DAY; //현재날짜
        long result = dday - today;

        // result 출력 준비
        String strFormat;
        if (result > 0) { //미래
            strFormat = "D-%d";
        } else if (result == 0) {  //현재
            strFormat = "오늘입니다";
        } else { //과거
            result *= -1;
            strFormat = "D+%d";
        }
        final String strCount = (String.format(strFormat, result));
        return strCount;
    }

    public String getDMonth (int mYear, int mMonthOfYear, int mDayOfMonth){
        // 디먼스 설정
        final Calendar ddayCalendar = Calendar.getInstance();
        ddayCalendar.set(mYear, mMonthOfYear, mDayOfMonth);

        //밀리세컨드로 연산
        final long dday = ddayCalendar.getTimeInMillis() / ONE_DAY; //(하루)의 밀리초수
        final long today = Calendar.getInstance().getTimeInMillis() / ONE_DAY;
        long result = dday - today;

        //출력준비
        String monthFormat;
        final String monthCount;
        if (result > 0) { //미래
            result /= 30; //달수 계산
            if(result == 0){ //한 달이 안 되면
                monthCount = null;
            } else { //1달 이상이면
                monthCount = (String.format("약 %d 달 남았습니다!", result));
            }

        } else if (result == 0) { //오늘
            monthCount = (String.format("오늘입니다", result));
        }

        else { //과거
            result *= -1;
            result /= 30;
            if(result == 0){ //한달 이하
                monthCount = null;
            } else{ //한달 이상
                monthCount = (String.format("약 %d 달 지났습니다!", result));
            }

        }
        return monthCount;
    }
}