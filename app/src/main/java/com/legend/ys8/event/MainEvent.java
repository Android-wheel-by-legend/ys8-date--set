package com.legend.ys8.event;

/**
 *
 * Created by legend on 2017/8/24.
 */

public class MainEvent {
    private String item;

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public MainEvent(String item) {

        this.item = item;
    }
}
