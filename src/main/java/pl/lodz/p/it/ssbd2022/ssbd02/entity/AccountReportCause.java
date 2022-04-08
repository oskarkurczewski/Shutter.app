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
@Table(name = "account_report_cause")
public class AccountReportCause {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @Column(name = "cause", nullable = false)
    private String cause;

    @Column(name = "version")
    private Long version;
}