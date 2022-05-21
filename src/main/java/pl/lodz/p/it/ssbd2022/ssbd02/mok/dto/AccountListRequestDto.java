package pl.lodz.p.it.ssbd2022.ssbd02.mok.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AccountListRequestDto {
    private int page;
    private int recordsPerPage;
    private String orderBy;
    private String order;
    private String login;
    private String email;
    private String name;
    private String surname;
    private Boolean registered;
    private Boolean active;
}
