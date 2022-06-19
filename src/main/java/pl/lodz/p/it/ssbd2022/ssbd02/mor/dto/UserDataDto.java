package pl.lodz.p.it.ssbd2022.ssbd02.mor.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import pl.lodz.p.it.ssbd2022.ssbd02.entity.Account;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class UserDataDto {
    @NotNull
    private String login;
    @NotNull
    private String name;
    @NotNull
    private String surname;
    @NotNull
    private String email;

    public UserDataDto(Account account) {
        this.login = account.getLogin();
        this.name = account.getName();
        this.surname = account.getSurname();
        this.email = account.getEmail();
    }
}
