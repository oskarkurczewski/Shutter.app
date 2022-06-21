package pl.lodz.p.it.ssbd2022.ssbd02.util;

import lombok.Getter;
import lombok.Setter;
import pl.lodz.p.it.ssbd2022.ssbd02.entity.Account;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * Abstrakcyjna klasa reprezentująca encję JPA, która może ulegać modyfikacjom w trakcie funkcjonowania
 * aplikacji (w przeciwieństwie do np. danych z tabeli słownikowych)
 */
@Getter
@Setter
@MappedSuperclass
public class ManagedEntity {

    @NotNull
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "modified_at")
    private LocalDateTime modifiedAt;

    @OneToOne
    @JoinColumn(name = "created_by", updatable = false)
    private Account createdBy;

    @OneToOne
    @JoinColumn(name = "modified_by")
    private Account modifiedBy;

    @PrePersist
    private void prePersist() {
        createdAt = LocalDateTime.now();
    }

    @PreUpdate
    private void preUpdate() {
        modifiedAt = LocalDateTime.now();
    }
}
