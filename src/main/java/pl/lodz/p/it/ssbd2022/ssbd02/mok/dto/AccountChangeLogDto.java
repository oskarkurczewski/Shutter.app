package pl.lodz.p.it.ssbd2022.ssbd02.mok.dto;

import lombok.Data;
import pl.lodz.p.it.ssbd2022.ssbd02.entity.AccountChangeLog;
import pl.lodz.p.it.ssbd2022.ssbd02.entity.ChangeType;

import java.time.LocalDateTime;

@Data
public class AccountChangeLogDto {
    private Long id;
    private Boolean registered;
    private Boolean active;
    private String email;
    private String login;
    private String name;
    private String surname;
    private LocalDateTime lastLogIn;
    private LocalDateTime changedAt;
    private ChangeType changeType;
    private String changedBy;

    public AccountChangeLogDto(AccountChangeLog accountChangeLog) {
        this.id = accountChangeLog.getId();
        this.registered = accountChangeLog.getRegistered();
        this.active = accountChangeLog.getActive();
        this.email = accountChangeLog.getEmail();
        this.login = accountChangeLog.getLogin();
        this.name = accountChangeLog.getLogin();
        this.surname = accountChangeLog.getLogin();
        this.lastLogIn = accountChangeLog.getLastLogIn();
        this.changedAt = accountChangeLog.getChangedAt();
        this.changeType = accountChangeLog.getChangeType();
        this.changedBy = accountChangeLog.getChangedBy().getLogin();
    }

}
