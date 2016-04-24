package com.stronans.android.agenda.model;

/**
 * Created by S.King on 23/04/2016.
 */
public class Message extends Happening {

    public Message(String title, String description) {
        super(title, description);
    }

    @Override
    public ClassType classType() {
        return ClassType.Message;
    }
}
