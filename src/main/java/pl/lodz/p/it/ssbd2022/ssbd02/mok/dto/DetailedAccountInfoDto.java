package pl.lodz.p.it.ssbd2022.ssbd02.mok.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import pl.lodz.p.it.ssbd2022.ssbd02.entity.AccessLevelAssignment;
import pl.lodz.p.it.ssbd2022.ssbd02.entity.Account;
import pl.lodz.p.it.ssbd2022.ssbd02.security.etag.SignableEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Klasa DTO wykorzystywana przy zwracaniu informacji o użytkowniku w punkcie końcowym typu GET
 * <code>/api/account/{login}/info</code>
 */
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Data
public class DetailedAccountInfoDto extends TracableDto implements SignableEntity {

    private Long version;

    private String login;

    private String email;

    private String name;

    private String surname;

    private Boolean twoFAEnabled;

    private Boolean active;

    private Boolean registered;

    private List<AccessLevelDto> accessLevelList = new ArrayList();

    private LocalDateTime lastLogIn;

    private LocalDateTime lastFailedLogInAttempt;

    private String lastFailedLoginIp;

    /**
     * Konstruktor obiektu DTO użytkownika
     *
     * @param account encja użytkownika
     */
    public DetailedAccountInfoDto(Account account) {
        super(
                account.getModifiedAt(),
                Optional.ofNullable(account.getModifiedBy()).map(Account::getLogin).orElse(null),
                account.getCreatedAt(),
                Optional.ofNullable(account.getCreatedBy()).map(Account::getLogin).orElse(null)
        );

        account
                .getAccessLevelAssignmentList()
                .stream()
                .filter(AccessLevelAssignment::getActive)
                .forEach(level -> accessLevelList.add(new AccessLevelDto(level)));

        version = account.getVersion();
        login = account.getLogin();
        email = account.getEmail();
        name = account.getName();
        surname = account.getSurname();
        twoFAEnabled = account.getTwoFAEnabled();
        active = account.getActive();
        registered = account.getRegistered();
        lastLogIn = account.getLastLogIn();
        lastFailedLogInAttempt = account.getLastFailedLogInAttempt();
        lastFailedLoginIp = account.getLastFailedLoginIp();
    }

    @Override
    public String getSignablePayload() {
        return login + version.toString();
    }
}
