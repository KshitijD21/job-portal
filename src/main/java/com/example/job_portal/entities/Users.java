package com.example.job_portal.entities;


import com.example.job_portal.entities.enums.Role;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;



@Document(collection = "users")
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Users {

    @Id
    private String id;

    private String userName;
    private String email;
    private String password;
    private Role role;
    @Override
    public String toString() {
        return "Users{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
