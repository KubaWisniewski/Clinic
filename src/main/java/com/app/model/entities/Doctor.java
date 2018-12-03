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
@DynamicUpdate
public class Doctor extends User {
    @Enumerated(EnumType.STRING)
    private Specialization specialization;
    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "doctor")
    @ToStringExclude
    @HashCodeExclude
    private Set<Appointment> appointments = new HashSet<>();
}

