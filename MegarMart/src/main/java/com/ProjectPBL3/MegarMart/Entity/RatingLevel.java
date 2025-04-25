package com.ProjectPBL3.MegarMart.Entity;

public enum RatingLevel {
    VERY_GOOD("Rất hài lòng"),
    GOOD("Hài lòng"),
    NORMAL("Bình thường"),
    BAD("Không hài lòng"),
    VERY_BAD("Rất tệ");

    private final String displayName;

    RatingLevel(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}

