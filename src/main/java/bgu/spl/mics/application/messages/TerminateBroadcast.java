package bgu.spl.mics.application.messages;

import bgu.spl.mics.Broadcast;

public class TerminateBroadcast implements Broadcast {
    int i;
    public TerminateBroadcast(){
        i=0;
    }
    public Class<? extends Broadcast> getType(){
        return this.getClass();
    }
}
