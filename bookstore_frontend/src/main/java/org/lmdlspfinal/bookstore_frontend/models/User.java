package org.lmdlspfinal.bookstore_frontend.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class User {
    private Long userid;
    private String username;
    private String email;
    private String password;

    public User() {}

    public Long getUserid() { return userid; }
    public void setUserid(Long userid) { this.userid = userid; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}