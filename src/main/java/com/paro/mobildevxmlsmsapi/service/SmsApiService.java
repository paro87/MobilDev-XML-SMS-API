package com.paro.mobildevxmlsmsapi.service;

import com.paro.mobildevxmlsmsapi.model.Sms;
import com.paro.mobildevxmlsmsapi.model.SmsMultiSenders;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class SmsApiService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SmsApiService.class);

    @Value("${mobildev.username}")
    private String username;
    @Value("${mobildev.password}")
    private String password;
    @Value("${mobildev.company_code}")
    private String company_code;
    @Value("${mobildev.originator}")
    private String originator;
    private String action;
    private final String serviceUrl="http://gateway.mobilus.net/com.mobilus";

    private final HttpClient client = HttpClientBuilder.create().build();

    public String postSmsToMany(Sms smsObj) {
        String result =null;
        if (smsObj.getMessage().length()<160)
            action="0";
        else
            action="40";

        String message = smsObj.getMessage();
        String numbers = smsObj.getNumbers();
        //In case if we will be receiving multiple separate String objects - numbers, inserted to the separated fields
        //Make sure that you uncommented related section in the Sms model
        /*StringBuilder numbers=new StringBuilder();
        for (String num : smsObj.getNumbers()) {
            numbers.append(num).append(",");
        }
        numbers.deleteCharAt(numbers.lastIndexOf(","));
        String numbers =numbers.toString();*/

        String xml = "<xml><MainReportRoot>\n" +
                "    <UserName>"+username+"-"+company_code+"</UserName>\n" +
                "    <PassWord>"+password+"</PassWord>\n" +
                "    <Action>"+action+"</Action>\n" +
                "    <Mesgbody>"+message+"</Mesgbody>\n" +
                "    <Numbers>"+numbers+"</Numbers>\n" +
                "    <Originator>"+originator+"</Originator>\n" +
                "    <SDate></SDate>\n" +
                "</MainReportRoot></xml>";
        HttpPost post = new HttpPost(serviceUrl);
        post.addHeader("content-type", "application/xml");
        HttpResponse response;

        try {
            HttpEntity entity = new ByteArrayEntity(xml.getBytes("UTF-8"));
            post.setEntity(entity);
            response = client.execute(post);
            result = EntityUtils.toString(response.getEntity());
            LOGGER.info("Response: {}", result);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public String postSmsMultiSenders(List<SmsMultiSenders> multiSendersList){
        String result =null;
        if (multiSendersList.get(0).getMessage().length()<160)
            action="1";
        else
            action="41";

        StringBuilder messageBody=new StringBuilder();
        multiSendersList.forEach(multisender->{
            String message="<Message>\n" +
                    "            <Mesgbody>"+multisender.getMessage()+"</Mesgbody>\n" +
                    "            <Number>"+multisender.getNumber()+"</Number>\n" +
                    "        </Message>";
            messageBody.append(message);

        });
        String xml ="<xml><MainmsgBody>\n" +
                "    <UserName>"+username+"-"+company_code+"</UserName>\n" +
                "    <PassWord>"+password+"</PassWord>\n" +
                "    <Action>"+action+"</Action>\n" +
                "    <Messages>\n" +
                     messageBody +
                "    </Messages>\n" +
                "    <Originator>"+originator+"</Originator>\n" +
                "    <SDate></SDate>\n" +
                "</MainmsgBody></xml>";
        HttpPost post = new HttpPost(serviceUrl);
        post.addHeader("content-type", "application/xml");
        HttpResponse response;

        try {
            HttpEntity entity = new ByteArrayEntity(xml.getBytes("UTF-8"));
            post.setEntity(entity);
            response = client.execute(post);
            result = EntityUtils.toString(response.getEntity());
            LOGGER.info("Response: {}", result);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    public String getReportByTimerId(String timerID){
        String result =null;
        action = "3";
        String xml="<MainReportRoot>\n" +
                "    <UserName>"+username+"-"+company_code+"</UserName>\n" +
                "    <PassWord>"+password+"</PassWord>\n" +
                "    <Action>"+action+"</Action>\n" +
                "    <MsgID>"+timerID+"</MsgID>\n" +
                "</MainReportRoot>";
        HttpPost post = new HttpPost(serviceUrl);
        post.addHeader("content-type", "application/xml");
        HttpResponse response;

        try {
            HttpEntity entity = new ByteArrayEntity(xml.getBytes("UTF-8"));
            post.setEntity(entity);
            response = client.execute(post);
            result = EntityUtils.toString(response.getEntity());
            LOGGER.info("Response: {}", result);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public String getReportByDate(String FDate, String LDate){
        String result =null;
        action = "2";
        String xml="<MainReportRoot>\n" +
                "    <UserName>"+username+"-"+company_code+"</UserName>\n" +
                "    <PassWord>"+password+"</PassWord>\n" +
                "    <Action>"+action+"</Action>\n" +
                "    <FDate>"+FDate+"</FDate>\n" +
                "    <LDate>"+LDate+"</LDate>\n" +
                "</MainReportRoot>";
        HttpPost post = new HttpPost(serviceUrl);
        post.addHeader("content-type", "application/xml");
        HttpResponse response;

        try {
            HttpEntity entity = new ByteArrayEntity(xml.getBytes("UTF-8"));
            post.setEntity(entity);
            response = client.execute(post);
            result = EntityUtils.toString(response.getEntity());
            LOGGER.info("Response: {}", result);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }
}
