package com.example.servicenewuser;

public class Constants {
    public static final String API_SECRET_KEY = "expensetrackerapikey";
    public static final long TOKEN_VALIDITY = 2 * 60 * 60 * 1000;
    public enum StateUser{
        untracker, ok, alert, danger
    }

}
