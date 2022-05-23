import "./style.scss";
import Button from "components/shared/Button";
import Card from "components/shared/Card";
import TextInput from "components/shared/TextInput";
import React, { useState } from "react";
import { useRequestResetPasswordMutation } from "redux/service/api";

const RequestResetPasswordPage = () => {
   const [login, setLogin] = useState<string>("");
   const [showMesage, setShowMessage] = useState<boolean>(false);
   const [requestResetPasswordMutation, requestResetPasswordMutationData] =
      useRequestResetPasswordMutation();

   const onSubmit = async (e?: React.FormEvent<HTMLFormElement>) => {
      e.preventDefault();
      try {
         await requestResetPasswordMutation({ login: login });
      } catch (err) {
         setShowMessage(true);
      }
   };

   return (
      <section className="reset-password-page-wrapper">
         <Card>
            <form>
               <p className="category-title">Nie pamiętasz ⁠hasła?</p>
               <p>Wyślemy do Ciebie wiadomość e-mail z instrukcją resetowania hasła.</p>
               <p> Podaj login:</p>
               <TextInput
                  label="Login"
                  type="login"
                  placeholder="Login"
                  value={login}
                  onChange={(e) => setLogin(e.target.value)}
               />
               <div className="footer">
                  <Button onClick={(e) => onSubmit(e)}>Wyślij wiadomość email</Button>
               </div>
               {showMesage && (
                  <p className="message">Przesłanie emaila nie powiodło się</p>
               )}
            </form>
         </Card>
      </section>
   );
};

export default RequestResetPasswordPage;
