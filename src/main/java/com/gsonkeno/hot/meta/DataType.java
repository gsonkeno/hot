package com.gsonkeno.hot.meta;

public enum DataType {
    BINARY("binary"), KEYWORD("keyword"), TEXT("text"),
    STRING("string"), BYTE("byte"), INTEGER("integer"),
    FLOAT("float"), DOUBLE("double"), LONG("long"),
    DATE("date"),BOOLEAN("boolean");

    private String value;
    private DataType(String value) {
        this.value = value;
    }

    public String getValue(){
        return  this.value;
    }
}
