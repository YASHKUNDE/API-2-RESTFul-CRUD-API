package in.api_2;

import java.util.List;

public class Login_Response {

    private String status;
    private String message;
    private List<Login_API> users;

    public Login_Response() {}

    public Login_Response(String status, String message, List<Login_API> users) {
        this.status = status;
        this.message = message;
        this.users = users;
    }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public List<Login_API> getUsers() { return users; }
    public void setUsers(List<Login_API> users) { this.users = users; }
}