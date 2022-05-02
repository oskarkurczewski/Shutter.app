package pl.lodz.p.it.ssbd2022.ssbd02.mok.dto;

import pl.lodz.p.it.ssbd2022.ssbd02.validation.constraint.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class UserRegisterDto {

    @NotNull(message = "validator.incorrect.login.null")
    @Login
    private String login;

    @NotNull(message = "validator.incorrect.password.null")
    @Password
    private String password;

    @NotNull(message = "validator.incorrect.email.null")
    @Email
    private String email;

    @NotNull(message = "validator.incorrect.name.null")
    @Name
    private String name;

    @NotNull(message = "validator.incorrect.surname.null")
    @Surname
    private String surname;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public UserRegisterDto(String login, String password, String email, String name, String surname) {
        this.login = login;
        this.password = password;
        this.email = email;
        this.name = name;
        this.surname = surname;
    }

    public UserRegisterDto() {
    }

    @Override
    public String toString() {
        return "UserRegisterDto{" +
                "login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                '}';
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }
}
