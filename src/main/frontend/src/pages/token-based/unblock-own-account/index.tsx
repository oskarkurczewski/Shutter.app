import React, { useEffect } from "react";
import "./style.scss";
import Card from "components/shared/Card";
import { useParams } from "react-router-dom";
import { useUnblockOwnAccountMutation } from "redux/service/api";

const UnblockOwnAccountPage = () => {
   const { token } = useParams();

   const [unblockOwnAccountMutation, { isLoading, isSuccess, isError }] =
      useUnblockOwnAccountMutation();

   useEffect(() => {
      unblockOwnAccountMutation(token);
   }, []);

   return (
      <Card className="unblock-own-account-wrapper">
         <p className="category-title">Odblokowywanie konta</p>
         {(() => {
            if (isLoading) {
               return <p>Loading...</p>;
            }
            if (isError) {
               return <p className="error">Nie udało się odblokować twojego konta.</p>;
            }
            if (isSuccess) {
               return (
                  <p>
                     Twoje konto zostało pomyślnie odblokowane. Możesz się już zalogować.
                  </p>
               );
            }
         })()}
      </Card>
   );
};

export default UnblockOwnAccountPage;
