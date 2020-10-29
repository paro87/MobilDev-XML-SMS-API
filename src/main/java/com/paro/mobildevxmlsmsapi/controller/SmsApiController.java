package com.paro.mobildevxmlsmsapi.controller;

import com.paro.mobildevxmlsmsapi.model.*;
import com.paro.mobildevxmlsmsapi.service.SmsApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping(value = "")
public class SmsApiController {

    private final SmsApiService smsApiService;

    @Autowired
    public SmsApiController(SmsApiService smsApiService) {
        this.smsApiService = smsApiService;
    }

    @GetMapping(value = "/")
    public String main(){
        return "index";
    }


    @GetMapping(value = "/smsToMany")
    public String getSmsToMany(Model model) {
        Sms smsObj =new Sms();
        model.addAttribute("smsToManyObj", smsObj);

        return "smsToMany";
    }

    @PostMapping(value = "/postSmsToMany")
    public String postSmsToMany(@ModelAttribute("smsToManyObj") Sms smsObj, Model model){
        String result = smsApiService.postSmsToMany(smsObj);
        if (result.length()==2) {
            String errorExplanation=errorController(result);
            model.addAttribute("error", errorExplanation);
            return "error";
        }
        return "successful";
    }

    @GetMapping(value = "/smsMultiSenders")
    public String getSmsMultiSenders(Model model){
        //List<SmsMultiSenders> multiSendersList=new ArrayList<>();
        MultiSenderWrapper multiSenderWrapper=new MultiSenderWrapper();
        model.addAttribute("multiSenderWrapper", multiSenderWrapper);
        return "smsMultiSenders";
    }

    @PostMapping(value = "/smsMultiSenders")
    public String postMultiSenders(@ModelAttribute("multiSendersObj") MultiSenderWrapper multiSenderWrapper, Model model){

        String result = smsApiService.postSmsMultiSenders(multiSenderWrapper.getMultiSendersList());
        if (result.length()==2) {
            String errorExplanation=errorController(result);
            model.addAttribute("error", errorExplanation);
            return "error";
        }
        return "successful";
    }

    @GetMapping(value="/reportByTimerId")
    public String getReportByTimerId(Model model){
        TimerIdWrapper timerIdWrapper=new TimerIdWrapper();
        model.addAttribute("timerIdWrapper", timerIdWrapper);
        return "reportByTimerId";
    }

    @PostMapping(value = "/postReportByTimerId")
    public String postReportByTimerId(@ModelAttribute("reportByTimerId") TimerIdWrapper timerIdWrapper, Model model){
        String result = smsApiService.getReportByTimerId(timerIdWrapper.getTimerId());
        if (result.length()==2) {
            String errorExplanation=errorController(result);
            model.addAttribute("error", errorExplanation);
            return "error";
        }

        String[] splitResult = result.split("\\s+");
        Report report=new Report();
        report.setTimerId(splitResult[0]);
        report.setPhoneNumber(splitResult[1]);
        report.setStatus(splitResult[2]);
        model.addAttribute("report", report);
        return "reportByTimerId";
    }

    @GetMapping(value="/reportByDate")
    public String getReportByDate(Model model){
        ReportDateRange reportDateRange=new ReportDateRange();
        model.addAttribute("reportDateRange", reportDateRange);
        return "reportByDate";
    }
    @PostMapping(value = "/postReportByDate")
    public String postReportByDate(@ModelAttribute("reportDateRange") ReportDateRange reportDateRange, Model model){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date FDate=reportDateRange.getFDate();
        Date LDate=reportDateRange.getLDate();
        String strFDate = dateFormat.format(FDate);
        String strLDate = dateFormat.format(LDate);

        String result = smsApiService.getReportByDate(strFDate, strLDate);
        if (result.length()==2) {
            String errorExplanation=errorController(result);
            model.addAttribute("error", errorExplanation);
            return "error";
        }
        List<Report> reportList=new ArrayList<>();
        String[] splitResult = result.split("\\n+");
        for (String spl : splitResult) {
            String[] split = spl.split("\\s+");
            Report report=new Report();
            report.setTimerId(split[0]);
            report.setPhoneNumber(split[1]);
            report.setStatus(split[2]);
            reportList.add(report);
        }

        ReportWrapper reportWrapper=new ReportWrapper();
        reportWrapper.setReportList(reportList);
        model.addAttribute("reportWrapper", reportWrapper);
        return "reportByDate";
    }


    public String errorController(String error) {
        switch (error) {
            case "01": return "Hatalı kullanıcı adı – şifre – bayi kodu";
            case "02": return "Mesaj Gönderimi: Yetersiz kredi Geçersiz ID: Böyle bir mesaj (ID) kodu yok Raporlama: Paket işlenmemiş ya da Gateway tarafında beklemede";
            case "03": return "Tanımsız Action parametresi";
            case "04": return "Gelen XML yok";
            case "05": return "XML düğümü eksik ya da hatalı";
            case "06": return "Tanımsız Originator bilgisi";
            case "07": return "Mesaj kodu (ID) yok";
            case "08": return "Verilen tarihler arasında SMS gönderimi yok";
            case "09": return "Tarih alanları boş - hatalı";
            case "10": return "SMS gönderilemedi";
            case "11": return "Tanımlanamayan hata";
            case "12": return "Admin yetkisiyle ulaşılabilecek bir alana Admin yetkisi olmayan biri ulaşmaya çalıştı.";
            case "13": return "Rapor istenen kullanıcı yok";
            default: return "Başka bir hata";
        }
    }


}
