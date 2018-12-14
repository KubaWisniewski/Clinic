package com.app.model.entities;

import com.app.model.security.User;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.apache.commons.lang3.builder.HashCodeExclude;
import org.apache.commons.lang3.builder.ToStringExclude;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@DynamicUpdate(value = true)
public class Patient extends User {
    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "patient")
    @HashCodeExclude
    @ToStringExclude
    private Set<Appointment> appointments = new HashSet<>();
    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "patient")
    @ToStringExclude
    @HashCodeExclude
    private Set<Prescription> prescriptions = new HashSet<>();
}
