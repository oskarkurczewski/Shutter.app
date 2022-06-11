import { Button, Card, Checkbox, TextInput } from "components/shared";
import { useStateWithComparison } from "hooks/useStateWithComparison";
import { useStateWithValidation } from "hooks/useStateWithValidation";
import React, { useEffect, useState } from "react";
import { useChangeAccountInfoMutation } from "redux/service/usersManagementService";
import { advancedUserInfoResponse } from "redux/types/api";
import { EtagData } from "redux/types/api/dataTypes";
import {
   emailPattern,
   nameSurnameFirstLetterPattern,
   nameSurnamePattern,
} from "util/regex";
import styles from "./ChangeBaseInfo.module.scss";

interface Props {
   userInfoData: EtagData<advancedUserInfoResponse>;
}

export const ChangeBaseInfo: React.FC<Props> = ({ userInfoData }) => {
   const [login, setLogin] = useState<string>("");
   const [active, setActive] = useState<boolean>(false);
   const [registered, setRegistered] = useState<boolean>(false);

   const [name, setName, nameIsValid] = useStateWithValidation<string>(
      [
         (name) => name.length <= 63,
         (name) => nameSurnamePattern.test(name),
         (name) => nameSurnameFirstLetterPattern.test(name),
      ],
      ""
   );
   const [surname, setSurname, surnameIsValid] = useStateWithValidation<string>(
      [
         (surname) => surname.length <= 63,
         (surname) => nameSurnamePattern.test(surname),
         (surname) => nameSurnameFirstLetterPattern.test(surname),
      ],
      ""
   );

   const [email, setEmail, emailIsValid] = useStateWithValidation<string>(
      [
         (email) => email.length >= 1,
         (email) => email.length <= 64,
         (email) => emailPattern.test(email),
      ],
      ""
   );

   const [email2, setEmail2, email2IsValid] = useStateWithComparison<string>("", email);

   const [canSubmit, setCanSubmit] = useState(
      nameIsValid && surnameIsValid && emailIsValid && email2IsValid
   );

   useEffect(() => {
      setCanSubmit(
         nameIsValid === null &&
            surnameIsValid === null &&
            emailIsValid === null &&
            email2IsValid
            ? true
            : false
      );
   }, [nameIsValid, surnameIsValid, emailIsValid, email2IsValid]);

   useEffect(() => {
      setLogin(userInfoData?.data.login || "");
      setActive(userInfoData?.data.active || false);
      setRegistered(userInfoData?.data.registered || false);
      setName(userInfoData?.data.name || "");
      setSurname(userInfoData?.data.surname || "");
      setEmail(userInfoData?.data.email || "");
      setEmail2(userInfoData?.data.email || "");
   }, [userInfoData]);

   const [infoMutation, infoMutationState] = useChangeAccountInfoMutation();

   useEffect(() => {
      // TODO: add toast
   }, [infoMutationState.isSuccess]);

   return (
      <Card className={styles.base_info_wrapper}>
         <p className={`category-title ${styles.category_title}`}>
            Podstawowe informacje
         </p>
         <div className={styles.content}>
            <div>
               <TextInput
                  value={login}
                  onChange={(e) => {
                     setLogin(e.target.value);
                  }}
                  label="Login"
                  className="text"
                  disabled
               />
            </div>
            <div className={styles.active_registered}>
               <Checkbox
                  id="registered"
                  value={registered}
                  onChange={() => {
                     null;
                  }}
                  disabled
               >
                  Zarejestrowane
               </Checkbox>
               <Checkbox
                  id="active"
                  value={active}
                  onChange={(e) => {
                     setActive(e.target.checked);
                  }}
               >
                  Aktywne
               </Checkbox>
            </div>
            <div>
               <TextInput
                  value={name}
                  onChange={(e) => {
                     setName(e.target.value);
                  }}
                  label="Imię"
                  required
                  className="text"
                  validation={nameIsValid}
                  validationMessages={[
                     "Imię musi być krótsze niż 63 znaków",
                     "Imię może zawierać tylko wielkie i małe litery",
                     "Imię musi się zaczynać od wielkiej litery",
                  ]}
               />
            </div>
            <div>
               <TextInput
                  value={surname}
                  onChange={(e) => {
                     setSurname(e.target.value);
                  }}
                  label="Nazwisko"
                  required
                  className="text"
                  validation={surnameIsValid}
                  validationMessages={[
                     "Nazwisko musi być krótsze niż 63 znaków",
                     "Nazwisko może zawierać tylko wielkie i małe litery",
                     "Nazwisko musi się zaczynać od wielkiej litery",
                  ]}
               />
            </div>
            <div className={styles.email}>
               <div>
                  <TextInput
                     value={email}
                     onChange={(e) => {
                        setEmail(e.target.value);
                     }}
                     label="Email"
                     required
                     className="text"
                     type="email"
                     validation={emailIsValid}
                     validationMessages={[
                        "Email musi być dłuższy niż 1 znak",
                        "Email musi być krótszy niż 64 znaków",
                        "Email musi być w formacie example@domain.com",
                     ]}
                  />
               </div>
               <div>
                  <TextInput
                     value={email2}
                     onChange={(e) => {
                        setEmail2(e.target.value);
                     }}
                     label="Powtórz email"
                     required
                     className="text"
                     type="email"
                     validation={email2IsValid}
                     validationMessages={["Adresy email nie mogą się różnić"]}
                  />
               </div>
            </div>
         </div>

         <div className={styles.save}>
            <Button
               onClick={() => {
                  canSubmit &&
                     infoMutation({
                        body: {
                           login: login,
                           email: email,
                           name: name,
                           surname: surname,
                           active: active,
                        },
                        etag: userInfoData.etag,
                     });
               }}
               disabled={!canSubmit}
               className={styles.btn}
            >
               Zapisz
            </Button>
            {infoMutationState.isError && (
               <p className={styles.error_message}>Nie można zapisać edycji</p>
            )}
         </div>
      </Card>
   );
};
