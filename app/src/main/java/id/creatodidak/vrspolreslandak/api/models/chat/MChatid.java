package id.creatodidak.vrspolreslandak.api.models.chat;

public class MChatid {
    private String chatid;
    private String activity;
    private String dataid;

    public MChatid(String chatid, String activity, String dataid) {
        this.chatid = chatid;
        this.activity = activity;
        this.dataid = dataid;
    }

    public String getChatid() {
        return chatid;
    }

    public void setChatid(String chatid) {
        this.chatid = chatid;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public String getDataid() {
        return dataid;
    }

    public void setDataid(String dataid) {
        this.dataid = dataid;
    }

    @Override
    public String toString() {
        return "MChatid{" +
                "chatid=" + chatid +
                ", activity='" + activity + '\'' +
                ", dataid='" + dataid + '\'' +
                '}';
    }
}
