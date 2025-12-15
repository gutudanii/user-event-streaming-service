package com.user.event.streaming.service.model;

import com.user.event.streaming.service.model.enums.ROLE;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * User Entity Model Class
 */
@Entity
@Table(name = "_user")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private String username;
    private String password;
    private ROLE role;
}
