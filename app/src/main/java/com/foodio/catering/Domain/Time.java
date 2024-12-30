package com.foodio.catering.Domain;

public class Time {
    private int Id;
    private String Value;

    public Time(){

    }

    @Override
    public String toString() {
        return Value;
    }

    public int getId(){
        return Id;
    }

    public void setIid(int id){
        Id = id;
    }

    public String getValue(){
        return Value;
    }

    public void setValue(String value){
        Value = value;
    }
}
