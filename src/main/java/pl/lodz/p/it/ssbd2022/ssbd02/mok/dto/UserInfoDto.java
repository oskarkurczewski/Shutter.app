package pl.lodz.p.it.ssbd2022.ssbd02.mok.dto;

import lombok.Getter;
import lombok.Setter;
import pl.lodz.p.it.ssbd2022.ssbd02.entity.User;

import javax.validation.constraints.NotNull;

/**
 * Klasa DTO wykorzystywana przy zwracaniu informacji o użytkowniku w punkcie końcowym typu GET
 * <code>/api/account/{login}/info</code>
 */
@Getter
@Setter
public class UserInfoDto {
    
    @NotNull
    private String login;
    
    @NotNull
    private String email;
    
    @NotNull
    private String name;
    
    @NotNull
    private String surname;

    /**
     * Konstruktor obiektu DTO użytkownika
     *
     * @param user encja użytkownika
     */
    public UserInfoDto(User user) {
        login = user.getLogin();
        email = user.getEmail();
        name = user.getName();
        surname = user.getSurname();
    }

}
