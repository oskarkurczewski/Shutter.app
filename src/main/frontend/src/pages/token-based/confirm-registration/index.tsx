import React, { useEffect } from "react";
import "./style.scss";
import Card from "components/shared/Card";
import { useParams } from "react-router-dom";
import { useConfirmRegistrationMutation } from "redux/service/api";

const ConfirmRegistrationPage = () => {
   const { token } = useParams();

   const [confirmRegistrationMutation, { isLoading, isSuccess, isError }] =
      useConfirmRegistrationMutation();

   useEffect(() => {
      confirmRegistrationMutation(token);
   }, []);

   return (
      <section className="confirm-registration-page-wrapper">
         <Card>
            <p className="category-title">Rejestracja konta</p>
            {(() => {
               if (isLoading) {
                  return <p>Loading...</p>;
               }
               if (isError) {
                  return (
                     <p className="error">
                        Nie udało się potwierdzić rejestracji twojego konta.
                     </p>
                  );
               }
               if (isSuccess) {
                  return (
                     <p>
                        Twoje konto zostało aktywowane pomyślnie. Możesz się już
                        zalogować.
                     </p>
                  );
               }
            })()}
         </Card>
      </section>
   );
};

export default ConfirmRegistrationPage;
