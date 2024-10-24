package com.atomicaccountability.atomic_accountability.enums;
public enum NotificationPref {
    EMAIL("email"),
    TEXT("text");

    private final String value;

    NotificationPref(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

