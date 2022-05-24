import Button from "components/shared/Button";
import Card from "components/shared/Card";
import TextInput from "components/shared/TextInput";
import React, { useState } from "react";
import { useChangeOwnPasswordMutation } from "redux/service/api";

const ChangePassword = () => {
   const [oldPassword, setOldPassword] = useState("");
   const [password, setPassword] = useState("");
   const [confirmPassword, setConfirmPassword] = useState("");

   const [equalityError, setEqualityError] = useState(false);

   const [mutation, { isLoading, isError, isSuccess, error }] =
      useChangeOwnPasswordMutation();

   const onSubmit = () => {
      setEqualityError(false);
      if (password == confirmPassword) {
         return mutation({
            oldPassword,
            password,
         });
      }
      setEqualityError(true);
   };

   return (
      <Card id="change-password">
         <p className="category-title">Hasło</p>
         <p>Podaj nowe hasło w celu zmiany.</p>
         <div className="row">
            <TextInput
               label="Aktualne Hasło"
               placeholder="Hasło"
               type="password"
               required
               value={oldPassword}
               onChange={(e) => setOldPassword(e.target.value)}
            />
         </div>
         <div className="row">
            <TextInput
               label="Hasło"
               placeholder="Hasło"
               type="password"
               required
               value={password}
               onChange={(e) => setPassword(e.target.value)}
            />
            <TextInput
               label="Powtórz hasło"
               placeholder="Hasło"
               type="password"
               required
               value={confirmPassword}
               onChange={(e) => setConfirmPassword(e.target.value)}
            />
         </div>

         {(() => {
            if (isLoading) {
               return <p>Loading...</p>;
            }
            if (equalityError) {
               return <p className="error">Hasła różnią się od siebie</p>;
            }
            if (isError) {
               const err = error as any;
               if (
                  err.status === 400 &&
                  err.data.message === "exception.password.not_unique"
               ) {
                  return (
                     <p className="error">
                        Nie możesz użyć hasła, z którego korzystałeś/aś niedawno!
                     </p>
                  );
               }

               return <p className="error">Nie udało się zmienić hasła.</p>;
            }
            if (isSuccess) {
               return <p>Hasło zostało pomyślnie zmienione.</p>;
            }
         })()}

         <Button onClick={onSubmit}>Zmień</Button>
      </Card>
   );
};

export default ChangePassword;
