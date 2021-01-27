package com.tradeapt;

import androidx.appcompat.app.AppCompatActivity;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;

import com.tradeapt.Key.PrivateInfo;
import com.tradeapt.DB.*;
import com.tradeapt.Type.AptType;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class MainActivity extends AppCompatActivity {

    private final String currentDate = "202101";
    private final String localNumber = "41117";

    private void initVariable() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initVariable();

        Apt apt = new Apt();

        apt.setAptName("One");
        apt.setAptPrice("10000");

        ArrayList<Apt> aptList = new ArrayList<>();

        aptList.add(apt);
        aptList.add(apt);
        aptList.add(apt);
        aptList.add(apt);

        writeXml(aptList);
        new Thread(new Runnable() {


            public void run() {
                //   aptList = GetAptData(localNumber, currentDate);
            }
        }).start();

    }


    private void writeXml(ArrayList<Apt> aptList) {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            Document doc = docBuilder.newDocument();
            doc.setXmlStandalone(true);

            Element apt = doc.createElement("Apt");
            doc.appendChild(apt);

            for(int i = 0 ; i < aptList.size(); i++) {

                Element item = doc.createElement("item");
                apt.appendChild(item);
                item.setAttribute("item", null);

                Element name = doc.createElement(AptType.APT_NAME);
                name.appendChild(doc.createTextNode(aptList.get(i).getAptName()));
                item.appendChild(name);

                Element price = doc.createElement(AptType.APT_PRICE);
                price.appendChild(doc.createTextNode(aptList.get(i).getAptPrice()));
                item.appendChild(price);

                Element exclusive = doc.createElement(AptType.APT_EXCLUSIVE_USE );
                price.appendChild(doc.createTextNode(aptList.get(i).getAptExclusiveUse()));
                item.appendChild(exclusive);

                Element floor = doc.createElement(AptType.APT_FLOOR );
                price.appendChild(doc.createTextNode(aptList.get(i).getAptFloor()));
                item.appendChild(floor);

                Element month = doc.createElement(AptType.APT_DATE_MONTH );
                price.appendChild(doc.createTextNode(aptList.get(i).getAptDateMonth()));
                item.appendChild(month);

                Element day = doc.createElement(AptType.APT_DATE_DAY );
                price.appendChild(doc.createTextNode(aptList.get(i).getAptDateDay()));
                item.appendChild(day);
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4"); //정렬 스페이스4칸
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes"); //들여쓰기
            transformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, "yes"); //doc.setXmlStandalone(true); 했을때 붙어서 출력되는부분 개행

            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new FileOutputStream(new File("D://source.xml")));

            transformer.transform(source, result);

        } catch (Exception e) {
            e.printStackTrace();
        }
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