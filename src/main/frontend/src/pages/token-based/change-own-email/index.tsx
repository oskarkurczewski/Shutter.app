import React, { useState } from "react";
import "./style.scss";
import Button from "components/shared/Button";
import Card from "components/shared/Card";
import TextInput from "components/shared/TextInput";
import { useParams } from "react-router-dom";
import { useChangeOwnEmailMutation } from "redux/service/api";

const ChangeOwnEmailPage = () => {
   const [newEmail, setNewEmail] = useState("");
   const [confirmEmail, setConfirmEmail] = useState("");
   const [equalityError, setEqualityError] = useState(false);

   const { token } = useParams();

   const [unblockOwnAccountMutation, { isLoading, isSuccess, isError }] =
      useChangeOwnEmailMutation();

   const onSubmit = () => {
      setEqualityError(false);
      if (newEmail == confirmEmail) {
         return unblockOwnAccountMutation({
            newEmail,
            token,
         });
      }
      setEqualityError(true);
   };

   return (
      <Card className="change-own-email-wrapper">
         <p className="category-title">Zmiana adresu email</p>

         <TextInput
            label="New Email"
            placeholder="Email"
            required
            value={newEmail}
            onChange={(e) => setNewEmail(e.target.value)}
         />
         <TextInput
            label="Confirm Email"
            placeholder="Email"
            required
            value={confirmEmail}
            onChange={(e) => setConfirmEmail(e.target.value)}
         />

         <Button onClick={onSubmit}>Submit</Button>

         {(() => {
            if (isLoading) {
               return <p>Loading...</p>;
            }
            if (equalityError) {
               return <p className="error">Adresy email różnią się od siebie</p>;
            }
            if (isError) {
               return <p className="error">Nie udało się zmienić adresu email.</p>;
            }
            if (isSuccess) {
               return <p>Twój adres email został pomyślnie zmieniony.</p>;
            }
         })()}
      </Card>
   );
};

export default ChangeOwnEmailPage;
