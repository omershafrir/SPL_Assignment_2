package bgu.spl.mics.application.messages;

import bgu.spl.mics.Broadcast;

public class PublishConferenceBroadcast implements Broadcast {


    public PublishConferenceBroadcast(){
    }

    public Class<? extends Broadcast> getType(){
        return this.getClass();
    }
}