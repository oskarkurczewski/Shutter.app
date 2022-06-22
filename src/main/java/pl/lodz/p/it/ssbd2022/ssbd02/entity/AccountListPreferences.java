package pl.lodz.p.it.ssbd2022.ssbd02.entity;

import lombok.*;
import pl.lodz.p.it.ssbd2022.ssbd02.util.ManagedEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@ToString

@Entity
@Table(name = "account_list_preferences")
@NamedQueries({
        @NamedQuery(name = "accountListPreferences.findByAccount",
                query = "SELECT p FROM AccountListPreferences p WHERE p.account = :account")
})
public class AccountListPreferences extends ManagedEntity {

    @Setter(value = AccessLevel.NONE)
    @Version
    @Column(name = "version")
    private Long version;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @OneToOne()
    @JoinColumn(name = "account_id")
    private Account account;

    @Column(name = "page")
    private Integer page;

    @Column(name = "records_per_page")
    private Integer recordsPerPage;

    @Column(name = "order_asc")
    private Boolean orderAsc;

    @Column(name = "order_by")
    private String orderBy;
}
