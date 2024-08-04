package com.choiminseon.nbggapp.model;

public class TranslateReq {
    public String language;
    public String answer;

    public TranslateReq(String language, String answer) {
        this.language = language;
        this.answer = answer;
    }
}
