package in.api_2;

public class Login_Request {
	
    private String userName;
    private String email;
    private String pass;
    private String id;  

    public Login_Request() {}
    
    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPass() { return pass; }
    public void setPass(String pass) { this.pass = pass; }
    
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
}