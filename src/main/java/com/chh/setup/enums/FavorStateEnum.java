package com.chh.setup.enums;

public enum FavorStateEnum {
    FAVOR(1),
    CANCEL_FAVOR(0);

    private int state;

    public int getState() {
        return state;
    }

    FavorStateEnum(int state) {
        this.state = state;
    }
}
