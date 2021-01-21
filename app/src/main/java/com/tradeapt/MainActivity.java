package com.tradeapt;

import androidx.appcompat.app.AppCompatActivity;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;

import com.tradeapt.Key.PrivateInfo;
import com.tradeapt.DB.*;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private final String currentDate = "202101";
    private final String localNumber = "41117";
    private final String mDbName = "apt.db";


    private boolean mIsTable;


    private void initVariable(){

        mIsTable = false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initVariable();

        final AptDB aptDB;
        final SQLiteDatabase db;
        aptDB = new AptDB(MainActivity.this, mDbName, null, 1);
        db = aptDB.getWritableDatabase();
        aptDB.onCreate(db);

        ArrayList<String> aptPreNameList = aptDB.getTableList(db);
        Log.d("Hey", "AptPreList : " + aptPreNameList);

        if(aptPreNameList.size() > 0){
            mIsTable = true;
        }else{
            mIsTable = false;
        }

        if(mIsTable){
            HashMap<String,ArrayList<Apt>> aptMap = aptDB.getAptInfo(db, aptPreNameList);
            Log.d("Hey", "aptMap : " + aptMap);

        }else{

        }

        new Thread(new Runnable() {

            ArrayList<Apt> aptList = new ArrayList<>();
            ArrayList<String> aptNameList = new ArrayList<>();

            public void run() {
                aptList = GetAptData(localNumber, currentDate);

                for(int i = 0; i < aptList.size() ;i++){
                    aptList.get(i).getAptName();

                    if(!aptNameList.contains(aptList.get(i).getAptName())){
                        aptNameList.add(aptList.get(i).getAptName());
                        aptDB.createTable(db,aptList.get(i).getAptName());
                    }
                    aptDB.insertData(db, aptList.get(i).getAptName(), aptList.get(i));
                }
            }
        }).start();
    }

    ArrayList<Apt> GetAptData(String localNumber, String date){

        ArrayList<Apt> aptList = new ArrayList<>();

        String queryUrl="http://openapi.molit.go.kr:8081/OpenAPI_ToolInstallPackage/service/rest/RTMSOBJSvc/getRTMSDataSvcAptTrade"
                +"?"+"serviceKey"+"="+PrivateInfo.mPrivateKey
                +"&"+"LAWD_CD"+"="+ localNumber
                +"&"+"DEAL_YMD"+"="+date;

        try {
            URL url= new URL(queryUrl);//문자열로 된 요청 url을 URL 객체로 생성.
            InputStream is= url.openStream(); //url위치로 입력스트림 연결

            XmlPullParserFactory factory= XmlPullParserFactory.newInstance();
            XmlPullParser xpp= factory.newPullParser();
            xpp.setInput( new InputStreamReader(is, "UTF-8") ); //inputstream 으로부터 xml 입력받기

            String tag;

            xpp.next();
            int eventType= xpp.getEventType();
            Apt apt = new Apt();

            while( eventType != XmlPullParser.END_DOCUMENT ){

                switch( eventType ){
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        tag= xpp.getName();//테그 이름 얻어오기

                        if(tag.equals("item")) ;// 첫번째 검색결과

                        else if(tag.equals("거래금액")){
                            xpp.next();
                            apt.setAptPrice(xpp.getText());
                        }
                        else if(tag.equals("아파트")){
                            xpp.next();
                            apt.setAptName(xpp.getText());
                        }
                        else if(tag.equals("월")){
                            xpp.next();
                            apt.setAptDateMonth(xpp.getText());
                        }
                        else if(tag.equals("일")){
                            xpp.next();
                            apt.setAptDateDay(xpp.getText());
                        }
                        else if(tag.equals("전용면적")){
                            xpp.next();
                            apt.setAptExclusiveUse(xpp.getText());
                        }
                        else if(tag.equals("층")){
                            xpp.next();
                            apt.setAptFloor(xpp.getText());
                            aptList.add(apt);
                            apt = new Apt();
                        }
                        break;

                    case XmlPullParser.TEXT:
                        break;

                    case XmlPullParser.END_TAG:
                        break;
                }

                eventType= xpp.next();
            }

        } catch (Exception e) {
            // TODO Auto-generated catch blocke.printStackTrace();
        }
        return aptList;
    }

}