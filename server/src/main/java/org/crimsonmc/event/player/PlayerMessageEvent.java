package org.crimsonmc.event.player;

/**
 * Created on 2015/12/23 by xtypr. Package cn.crimsonmc.event.player in project crimsonmc .
 */
public abstract class PlayerMessageEvent extends PlayerEvent {

    protected String message;

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
