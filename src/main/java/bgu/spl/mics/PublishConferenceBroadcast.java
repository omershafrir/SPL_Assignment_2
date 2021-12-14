package bgu.spl.mics;

public class PublishConferenceBroadcast implements Broadcast {


    public PublishConferenceBroadcast(){
    }

    public Class<? extends PublishConferenceBroadcast> getType(){
        return this.getClass();
    }
}
