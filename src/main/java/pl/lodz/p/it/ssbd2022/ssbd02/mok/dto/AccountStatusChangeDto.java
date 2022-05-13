package pl.lodz.p.it.ssbd2022.ssbd02.mok.dto;

import pl.lodz.p.it.ssbd2022.ssbd02.entity.Account;

import javax.validation.constraints.NotNull;


/**
 * Klasa reprezentująca zmianę statusu użytkownika w systemie
 */
public class AccountStatusChangeDto {

    @NotNull
    private Boolean active;

    public AccountStatusChangeDto() {
    }

    public AccountStatusChangeDto(Account account) {
        this.active = account.getActive();
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return "AccountStatusChangeDto{" +
                "active=" + active +
                '}';
    }
}
