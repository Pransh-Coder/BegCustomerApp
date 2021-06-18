package com.example.begcustomerapp.networkingStructure;

public class BegURL {

    private String BASE_URL="http://128.199.17.214/";

    //private String BASE_URL="http://134.209.157.253/";

    private String LOGIN = BASE_URL+"patient/api/otp-login/";

    private String SIGNUP = BASE_URL+"accounts/api/signup/";

    private String VACCINE_LIST = BASE_URL+"patient/api/vaccine-list/";

    private String VACCINATION = BASE_URL+"patient/api/vaccination/";

    private String ARTICLE_LIST = BASE_URL+"patient/api/article/";

    private String DOCTOR_DETAILS = BASE_URL+"doctor/api/doctor/";

    private String CLINIC_DETAILS = BASE_URL+"doctor/api/hospital/";

    private String PATIENT_FAMILY = BASE_URL+"patient/api/patient-family/";     //consultation history API

    private String PATIENT_FAMILY_CONSULTATION = BASE_URL+"patient/api/consultation/?family=";

    private String ARTICLE_DETAIL = BASE_URL+"patient/api/article-detail/?article=";

    private String PATIENT_CONSULTATION_DETAIL = BASE_URL+"patient/api/consultation/";

    private String UPCOMING_APPOINTMENT = BASE_URL+"patient/api/upcoming-consultations/";

    private String UPLOAD_FAMILY_REPORTS = BASE_URL+"patient/api/family-reports/";

    private String CONSULTATION_DETAIL =BASE_URL+"patient/api/consultation-detail/?consultation=";

    private String SEND_AND_DOWNLOAD = BASE_URL+"patient/api/send-mail/";

    private String ADD_FAMILY_MEMB = BASE_URL+"patient/api/patient-family/";

    private String FILL_QUESTIONNAIRE = BASE_URL+"patient/api/questionnaire/";

    private String TRANSACTION_DETAIL = BASE_URL+"patient/api/transaction/";

    private String UPCOMING_VACCINATION = BASE_URL+"patient/api/upcoming-vaccination";

    private String PREVIOUS_VACCINATION = BASE_URL+"patient/api/previous-vaccination";

    private String FORGOT_PASSWORD = BASE_URL+"accounts/api/forgot-password";

    public  String CORRECT_TIME = BASE_URL + "doctor/api/all-time/" ;

    public String RESET_PASSWORD = BASE_URL+ "reset_password/";

    public String getCORRECT_TIME() {

        return CORRECT_TIME;
    }

    public String getUPCOMING_VACCINATION() {
        return UPCOMING_VACCINATION;
    }

    public String getPREVIOUS_VACCINATION() {
        return PREVIOUS_VACCINATION;
    }

    public String getPAYMENT_CASHFREE() {
        return PAYMENT_CASHFREE;
    }

    private String PAYMENT_CASHFREE = "https://api.cashfree.com/api/v2/cftoken/order";

    public String getPATIENT_CONSULTATION_DETAIL() {    
        return PATIENT_CONSULTATION_DETAIL;
    }

    public String getBASE_URL() {
        return BASE_URL;
    }

    public String getLOGIN() {
        return LOGIN;
    }

    public String getSIGNUP() {
        return SIGNUP;
    }

    public String getARTICLE_LIST() {
        return ARTICLE_LIST;
    }

    public String getDOCTOR_DETAILS() {
        return DOCTOR_DETAILS;
    }

    public String getCLINIC_DETAILS() {
        return CLINIC_DETAILS;
    }

    public String getPATIENT_FAMILY() {
        return PATIENT_FAMILY;
    }

    public String getARTICLE_DETAIL() {
        return ARTICLE_DETAIL;
    }

    public String getCONSULTATION_DETAIL() {
        return CONSULTATION_DETAIL;
    }

    public String getPATIENT_FAMILY_CONSULTATION() {
        return PATIENT_FAMILY_CONSULTATION;
    }

    public String getUPCOMING_APPOINTMENT() {
        return UPCOMING_APPOINTMENT;
    }

    public String getUPLOAD_FAMILY_REPORTS() {
        return UPLOAD_FAMILY_REPORTS;
    }

    public String getSEND_AND_DOWNLOAD() {
        return SEND_AND_DOWNLOAD;
    }

    public String getADD_FAMILY_MEMB() {
        return ADD_FAMILY_MEMB;
    }

    public String getFILL_QUESTIONNAIRE() {
        return FILL_QUESTIONNAIRE;
    }

    public String getTRANSACTION_DETAIL() {
        return TRANSACTION_DETAIL;
    }

    public String getFORGOT_PASSWORD() {
        return FORGOT_PASSWORD;
    }

    public String getRESET_PASSWORD() {
        return RESET_PASSWORD;
    }
}
