package com.example.calendar;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private TextView textView;
    private TextView main_edit_text;
    private Button del_btn;
    private Button save_btn;
    private Button mod_btn;
    private EditText editText;
    private CalendarView mCalendarView;
    private String name=null;
    private String string=null;
    private long backbtnTime=0;

    @Override //유튜브 참고하면서 만든 부분이다.
    public void onBackPressed() {
        long curTime = System.currentTimeMillis();
        long gapTime =curTime - backbtnTime;
        if(0<=gapTime && 2000 >= gapTime){
            super.onBackPressed();
        }
        else{
            backbtnTime=curTime;
            Toast.makeText(this, "한 번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mCalendarView=findViewById(R.id.calender); //(36~41)가 무슨 말이냐면 xml에서 id가 calendar인 레이어를 찾아달라고하는것이다.
        editText=findViewById(R.id.edit_text);
        del_btn=findViewById(R.id.del_btn);
        save_btn=findViewById(R.id.save_btn);
        mod_btn=findViewById(R.id.mod_btn);
        textView=findViewById(R.id.date);
        main_edit_text=findViewById(R.id.main_edit_text);




        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date()); //날짜 설정 해주는 거임.
        display(date); //함수(메소드)임.

        final CalendarView mCalendarView = (CalendarView) findViewById(R.id.calender); //캘린더 값이 바뀌지 않게 final을 씀

        mCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) { //캘린더에서 날짜를 바꾸었을시
                save_btn.setVisibility(View.VISIBLE); //각 변수(xml에서 보이는 레이어들을 보이게 할지 말지 결정함 52~57)
                mod_btn.setVisibility(View.INVISIBLE);
                del_btn.setVisibility(View.INVISIBLE);
                editText.setVisibility(View.VISIBLE);
                editText.setText("");
                main_edit_text.setVisibility(View.INVISIBLE);
                mCalendarView.setVisibility(View.VISIBLE);

                checkday(i,i1,i2); //얜 함수(메소드)임 밑코드 참고
                String date=null;


                if(i1>8){
                    date= i + "-"+ (i1+1) + "-" + i2;
                }
                else if(i1<=8){
                    date= i + "-"+"0"+ (i1+1) + "-" + i2;
                }
                display(date); //얘도 함수(메소드)
            }
        });




        save_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) { //이것도 마찬가지다. save_btn을 클릭했을시
                string=editText.getText().toString();
                main_edit_text.setText(string); //보이게 할 지 말지 결정
                mod_btn.setVisibility(View.VISIBLE);
                del_btn.setVisibility(View.VISIBLE);
                editText.setVisibility(View.INVISIBLE);
                save_btn.setVisibility(View.INVISIBLE);
                main_edit_text.setVisibility(View.VISIBLE);
                Toast.makeText(MainActivity.this, "해당 내용이 저장 되었습니다.", Toast.LENGTH_SHORT).show();
                //mCalendarView.setVisibility(View.VISIBLE);
                savetxt(name); //저장 메소드
            }

        });

       /*editText.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                save_btn.setVisibility(View.VISIBLE);
                mod_btn.setVisibility(View.INVISIBLE);
                del_btn.setVisibility(View.INVISIBLE);
                mCalendarView.setVisibility(View.VISIBLE);
            }
        });*/
    }
    public void display(String text){ //위에나온 display 함수(메소드)이다. 얘는 그냥 달력클릭했을 때 그에 맞는 날짜를 출력해주는것이다.
        textView = findViewById(R.id.date);
        textView.setText(text);
    }
    public void checkday(int year, int month, int day){ //여기에서부터 좀 어려운데 파일 입출력이라고 파일을 저장하고 그걸 읽는 등등 역할을 수행한다.
        //여기부분은 유튜브, 블로그에 있는 코드들을 참고했다.
        name=year+"-"+month+"-"+day+".txt"; //안드로이드 내부에 저장할 텍스트 네임을 설정해주는것이다.
        FileInputStream fileInputStream=null; //변수 설정
        try{
            fileInputStream = openFileInput(name);

            byte[] fileData=new byte[fileInputStream.available()]; //바이트 배열로 파일을 저장해주는거다.
            fileInputStream.read(fileData); //파일을 바이트 하나하나씩 읽어주는것이다.
            fileInputStream.close(); //닫는다 파일을 닫는다.

            string=new String(fileData); //변수설정

            editText.setVisibility(View.INVISIBLE);
            main_edit_text.setVisibility(View.VISIBLE);
            main_edit_text.setText(string);
            save_btn.setVisibility(View.INVISIBLE);
            del_btn.setVisibility(View.VISIBLE);
            mod_btn.setVisibility(View.VISIBLE);

            mod_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    editText.setVisibility(View.VISIBLE);
                    editText.setText(string);
                    save_btn.setVisibility(View.VISIBLE);
                    del_btn.setVisibility(View.INVISIBLE);
                    mod_btn.setVisibility(View.INVISIBLE);
                    main_edit_text.setVisibility(View.INVISIBLE);
                    main_edit_text.setText(editText.getText());
                }
            });

            del_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    editText.setVisibility(View.VISIBLE);
                    editText.setText("");
                    main_edit_text.setVisibility(View.INVISIBLE);
                    save_btn.setVisibility(View.VISIBLE);
                    mod_btn.setVisibility(View.INVISIBLE);
                    del_btn.setVisibility(View.INVISIBLE);
                    Toast.makeText(MainActivity.this, "해당 내용이 삭제 되었습니다.", Toast.LENGTH_SHORT).show();
                    removetxt(name); //지우는거임 메소드
                }
            });
            if(editText.getText()==null){ //editText창에 있는 값이 아무것도 없을시
                main_edit_text.setVisibility(View.INVISIBLE); //보이게 할지 말지 결정하는것
                save_btn.setVisibility(View.VISIBLE);
                del_btn.setVisibility(View.INVISIBLE);
                mod_btn.setVisibility(View.INVISIBLE);
                editText.setVisibility(View.VISIBLE);
            }
        }catch(Exception e){ //예외가 있을시 에러를 잡고잡아서 출력을 해주는거임
            e.printStackTrace();
        }

    }
    @SuppressLint("WrongConstant")
    public void removetxt(String readDay){
        FileOutputStream fileOutputStream=null;

        try{
            fileOutputStream=openFileOutput(readDay,MODE_ENABLE_WRITE_AHEAD_LOGGING);
            String content="";
            fileOutputStream.write((content).getBytes());
            fileOutputStream.close();

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @SuppressLint("WrongConstant")
    public void savetxt(String readDay){ //있는것들을 저장해주는 함수들임.
        FileOutputStream fileOutputStream=null;

        try{
            fileOutputStream=openFileOutput(readDay,MODE_NO_LOCALIZED_COLLATORS);
            String content=editText.getText().toString();
            fileOutputStream.write((content).getBytes());
            fileOutputStream.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}