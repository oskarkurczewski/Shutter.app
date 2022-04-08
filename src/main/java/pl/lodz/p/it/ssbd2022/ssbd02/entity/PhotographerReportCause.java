package pl.lodz.p.it.ssbd2022.ssbd02.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
@Entity
@Table(name = "photographer_report_cause")
public class PhotographerReportCause {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column
    private Long version;

    @NotNull
    @Column(nullable = false)
    private String cause;
}