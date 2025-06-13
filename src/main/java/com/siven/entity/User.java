package com.siven.entity;

import com.siven.utils.AESUtil;
import jakarta.persistence.*;

@Entity
@Table(name="R_SC_USER1")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String password; // Store hashed password with BCrypt ideally

    @Column(name = "email")
    private String encryptedEmail;

    public void setEmail(String email) {
        this.encryptedEmail = AESUtil.encrypt(email);
    }

    public String getEmail() {
        return AESUtil.decrypt(this.encryptedEmail);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
// getters, setters for other fields

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
