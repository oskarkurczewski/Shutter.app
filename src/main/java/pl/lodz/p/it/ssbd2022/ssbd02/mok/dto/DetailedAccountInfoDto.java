package pl.lodz.p.it.ssbd2022.ssbd02.mok.dto;

import lombok.Getter;
import lombok.Setter;
import pl.lodz.p.it.ssbd2022.ssbd02.entity.Account;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Klasa DTO wykorzystywana przy zwracaniu informacji o użytkowniku w punkcie końcowym typu GET
 * <code>/api/account/{login}/info</code>
 */
@Getter
@Setter
public class DetailedAccountInfoDto extends TracableDto {

    private String login;

    private String email;

    private String name;

    private String surname;

    private Boolean active;

    private Boolean registered;

    private List<AccessLevelDto> accessLevelList = new ArrayList();

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

        account.getAccessLevelAssignmentList().forEach(level -> accessLevelList.add(new AccessLevelDto(level)));

        login = account.getLogin();
        email = account.getEmail();
        name = account.getName();
        surname = account.getSurname();
        active = account.getActive();
        registered = account.getRegistered();
    }

}
