package pl.lodz.p.it.ssbd2022.ssbd02.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Entity
@Table(name = "access_level")
public class AccessLevelValue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column
    private Long version;

    @NotNull
    @Column(name = "name", nullable = false, unique = true)
    private String name;
}