package pl.lodz.p.it.ssbd2022.ssbd02.validation;


/**
 * Klasa zawierająca wyrażenia regularne ograniczeń pól.
 */
public final class REGEXP {

    public static final String PASSWORD_PATTERN
            = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()[{}]:;',?/*~$^+=<>-]).{8,64}$";

    public static final String LOGIN_PATTERN
            = "^[a-zA-Z0-9]([._-](?![._-])|[a-zA-Z0-9]){1,13}[a-zA-Z0-9]$";

    public static final String ACCESS_LEVEL_PATTERN
            = "^(?!ADMINISTRATOR).*$";

    public static final String ORDER_PATTERN
            = "^asc|desc$";

    public static final String EMAIL_PATTERN
            = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
            + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";

    public static final String TWO_FA_CODE_PATTERN = "[0-9]{6}";

    public static final String NAME_SURNAME_PATTERN = "[A-ZĄĆĘŃŚŁÓŻŹ][a-ząćęńśłóżź]{0,63}";

    public static final String DESCRIPTION_PATTERN = "[A-ZĄĆĘŃŚŁÓŻŹa-ząćęńśłóżź.,@%-*:\"'\\()!?/0-9 ]{1,4096}";

    public static final String PHOTO_DESCRIPTION_PATTERN = "[A-ZĄĆĘŃŚŁÓŻŹa-ząćęńśłóżź.,@%-*:\"'\\()!?/0-9 ]{0,1024}";

    public static final String PHOTO_TITLE_PATTERN = "[A-ZĄĆĘŃŚŁÓŻŹa-ząćęńśłóżź.,@%-*:\"'\\()!?/0-9 ]{1,64}";

    public static final String LOCALE_PATTERN = "^[a-zA-Z]{2}(?:-[a-zA-Z]{2}){0,2}$";

    public static final String CAUSE_PATTERN = "^[A-Z_]{0,128}$";

    public static final String NAME_SURNAME_QUERY_PATTERN = "[a-ząćęńśłóżźA-ZĄĆĘŃŚŁÓŻŹ]{0,63}";
}
