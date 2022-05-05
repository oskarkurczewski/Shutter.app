package pl.lodz.p.it.ssbd2022.ssbd02.mok.dto;

import pl.lodz.p.it.ssbd2022.ssbd02.entity.User;

import javax.validation.constraints.NotNull;


/**
 * Klasa reprezentująca zmianę statusu użytkownika w systemie
 */
public class UserStatusChangeDto {

    @NotNull
    private Boolean active;

    public UserStatusChangeDto() {
    }

    public UserStatusChangeDto(User user) {
        this.active = user.getActive();
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return "UserStatusChangeDto{" +
                "active=" + active +
                '}';
    }
}
