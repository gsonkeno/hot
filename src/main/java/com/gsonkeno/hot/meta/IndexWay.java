package com.gsonkeno.hot.meta;

public enum IndexWay {
    TRUE("true"), FALSE("false"), ANALYZE("analyzed"),
    NOT_ANALYZE("not_analyzed"), NO("no");

    private String value;
    private IndexWay(String value){
        this.value = value;
    }
    public String getValue(){
        return  value;
    }
}
