package in.api_2;

import jakarta.persistence.*;

@Entity
public class Login_API {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;
    
    @Column(unique = true)
    private String userName;

    @Column(unique = true)
    private String email;

    @Column(unique = true)
    private String mobile;

    private String pass;

    // Constructors
    public Login_API() {}

    public Login_API(Integer id, String name, String userName, String email, String mobile, String pass) {
        this.id = id;
        this.name = name;
        this.userName = userName;
        this.email = email;
        this.mobile = mobile;
        this.pass = pass;
    }

    // Getters and Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getMobile() { return mobile; }
    public void setMobile(String mobile) { this.mobile = mobile; }

    public String getPass() { return pass; }
    public void setPass(String pass) { this.pass = pass; }
}