package com.app.model.security;

import com.app.model.entities.Gender;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.apache.commons.lang3.builder.HashCodeExclude;
import org.apache.commons.lang3.builder.ToStringExclude;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Entity
@Indexed
@Inheritance(strategy = InheritanceType.JOINED)
@OnDelete(action = OnDeleteAction.CASCADE)
@DynamicUpdate(value = true)
public class User {
    @Id
    @GeneratedValue
    private Long id;
    @Field
    private String firstName;
    private String lastName;
    private String email;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    private String phone;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthdayDate;
    private String password;
    @Transient
    private String passwordConfirmation;
    private boolean enabled;
    @Enumerated(EnumType.STRING)
    private Role role;
    private String photo;
    @Transient
    private MultipartFile multipartFile;
}
