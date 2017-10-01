package com.legend.ys8.event;

import com.legend.ys8.model.CharacterImpl;

/**
 *
 * Created by legend on 2017/9/16.
 */

public class CharacterInfoEvent {

    CharacterImpl character;
    String s;

    public String getS() {
        return s;
    }

    public void setS(String s) {
        this.s = s;
    }

    public CharacterInfoEvent(String s) {
        this.s = s;
    }

    public CharacterImpl getCharacter() {
        return character;
    }

    public void setCharacter(CharacterImpl character) {
        this.character = character;
    }

    public CharacterInfoEvent(CharacterImpl character) {

        this.character = character;
    }
}
