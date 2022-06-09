export const validateFields = (formData: {
   login: string;
   email: string;
   password: string;
   confirmPassword: string;
   name: string;
   surname: string;
   userDataChecked: boolean | null;
   termsOfUseChecked: boolean | null;
}) => [
   [
      // Login section
      {
         label: "Login musi mieć przynajmniej 3 znaki",
         valid: formData.login.length == 0 ? null : formData.login.length >= 3,
      },
      {
         label: "Login nie może mieć więcej niż 15 znaków",
         valid: formData.login.length == 0 ? null : formData.login.length <= 15,
      },
   ],
   [
      // Email section
      {
         label: "Adres email musi mieć poprawny format",
         valid:
            formData.email.length == 0
               ? null
               : /^\w+([.-]?\w+)*@\w+([.-]?\w+)*(\.\w{2,3})+$/.test(formData.email),
      },
   ],
   [
      // Password section
      {
         label: "Hasło musi mieć przynajmniej 8 znaków",
         valid: formData.password.length == 0 ? null : formData.password.length >= 8,
      },
      {
         label: "Hasło nie może mieć więcej niż 64 znaki",
         valid: formData.password.length == 0 ? null : formData.password.length <= 64,
      },
      {
         label: "Hasła muszą być ze sobą zgodne",
         valid:
            formData.password.length == 0
               ? null
               : formData.password === formData.confirmPassword,
      },
   ],
   [
      // Name section
      {
         label: "Pole imię nie może być puste",
         valid: formData.name.length == 0 ? null : formData.name.length > 0,
      },
      {
         label: "Pole nazwisko nie może być puste",
         valid: formData.surname.length == 0 ? null : formData.surname.length > 0,
      },
   ],
   [
      {
         label: "Należy zaakceptować zgodę na przetwarzanie danych osobowych",
         valid: formData.userDataChecked === null ? null : formData.userDataChecked,
      },
      {
         label: "Należy zaakceptować regulamin",
         valid: formData.termsOfUseChecked === null ? null : formData.termsOfUseChecked,
      },
   ],
];
