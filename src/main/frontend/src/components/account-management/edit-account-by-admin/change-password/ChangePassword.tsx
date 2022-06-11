import { Button, Card, TextInput } from "components/shared";
import { useStateWithComparison } from "hooks/useStateWithComparison";
import { useStateWithValidation } from "hooks/useStateWithValidation";
import React, { useEffect, useState } from "react";
import { useChangeSomeonesPasswordMutation } from "redux/service/userSettingsService";
import { passwordPattern } from "util/regex";
import styles from "./ChangePassword.module.scss";

interface Props {
   login: string;
}

export const ChangePassword: React.FC<Props> = ({ login }) => {
   const [data, setData] = useState({ password: "", password2: "" });
   const [passwordMutation, passwordMutationState] = useChangeSomeonesPasswordMutation();
   const [passwordCheck, setPasswordCheck] = useState(false);

   const save = () => {
      console.log(data);
      passwordCheck &&
         passwordMutation({ login: login, data: { password: data.password } });
   };

   const [password, setPassword, passwordValidation] = useStateWithValidation(
      [
         (password) => password.length >= 8,
         (password) => password.length <= 64,
         // TODO: fix password regex
         (password) => passwordPattern.test(password),
      ],
      ""
   );

   const [password2, setPassword2, password2Validation] = useStateWithComparison(
      "",
      password
   );

   useEffect(() => {
      setPasswordCheck(data.password == data.password2);
   }, [data]);

   return (
      <Card className={styles["changePassword-wrapper"]}>
         <p className={`category-title ${styles.category_title}`}>Hasło</p>

         <div>
            <TextInput
               value={password}
               label="Hasło"
               onChange={(e) => {
                  setPassword(e.target.value);
               }}
               type="text"
               required
               validation={passwordValidation}
               validationMessages={[
                  "Hasło musi mieć przynajmniej 8 znaków",
                  "Hasło może mieć maksymalnie 64 znaki",
                  "Hasło musi zawierać co najmniej jedną małą i wielką literę, cyfrę i znak specjalny",
               ]}
            />
            <TextInput
               value={password2}
               label="Powtórz hasło"
               onChange={(e) => {
                  setPassword2(e.target.value);
               }}
               type="password"
               required
               validation={password2Validation}
               validationMessages={["Hasła różnią się od siebie"]}
            />
         </div>
         <Button onClick={save} className={styles.save}>
            zApIsZ
         </Button>
      </Card>
   );
};
