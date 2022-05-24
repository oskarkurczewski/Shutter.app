import "./style.scss";
import Button from "components/shared/Button";
import Card from "components/shared/Card";
import TextInput from "components/shared/TextInput";
import React, { useState } from "react";
import { useResetPasswordMutation } from "redux/service/api";
import { useParams } from "react-router-dom";

const ResetPasswordPage = () => {
   const { token } = useParams();
   const [password, setPassword] = useState<string>("");
   const [showMesage, setShowMessage] = useState<boolean>(false);
   const [resetPasswordMutation, { isLoading, isSuccess, isError }] =
      useResetPasswordMutation();

   const onSubmit = async (e) => {
      e.preventDefault();
      await resetPasswordMutation({ token: token, newPassword: password });
   };

   return (
      <section className="reset-password-page-wrapper">
         <Card>
            <form>
               <p className="category-title">Resetowanie hasła</p>
               <p>
                  Aby dokończyć procedurę resetowania hasła wpisz nowe hasło dla twojego
                  konta:
               </p>
               <TextInput
                  label="Nowe hasło"
                  type="password"
                  placeholder="Hasło"
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
               />
               <div className="footer">
                  <Button onClick={(e) => onSubmit(e)}>Zresetuj hasło</Button>
               </div>
               {(() => {
                  if (isLoading) {
                     return <p>Loading...</p>;
                  }
                  if (isError) {
                     return (
                        <p className="error">Nie udało się wysłać wiadomości email</p>
                     );
                  }
                  if (isSuccess) {
                     return <p>Email został wysłany pomyślnie</p>;
                  }
               })()}{" "}
            </form>
         </Card>
      </section>
   );
};

export default ResetPasswordPage;
