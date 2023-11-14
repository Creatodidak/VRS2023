package id.creatodidak.vrspolreslandak.api.models.chat;

public class MChatdata {
    private int id;
    private String chatId;
    private String type;
    private String data;
    private String status;
    private String createdAt;
    public  MChatdata(){

    }
    public MChatdata(String chatId, String type, String data, String status, String createdAt) {
        this.chatId = chatId;
        this.type = type;
        this.data = data;
        this.status = status;
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

}
