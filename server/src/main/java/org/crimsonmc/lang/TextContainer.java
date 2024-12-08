package org.crimsonmc.lang;

import org.crimsonmc.server.Server;

/**
 * author: MagicDroidX crimsonmc Project
 */
public class TextContainer implements Cloneable {

    protected String text;

    public TextContainer(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return this.getText();
    }

    @Override
    public TextContainer clone() {
        try {
            return (TextContainer) super.clone();
        } catch (CloneNotSupportedException e) {
            Server.getInstance().getLogger().exception(e);
        }
        return null;
    }
}
