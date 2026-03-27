package com.anem.comboshop.domain;

public enum Purpose {
    HOSTEL_ESSENTIALS("Hostel Essentials"),
    GYM_PACK("Gym Pack"),
    BIRTHDAY_GIFT("Birthday Gift");

    private final String label;
    Purpose(String label){ this.label = label; }
    public String getLabel(){ return label; }
}
