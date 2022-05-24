import Button from "components/shared/Button";
import Card from "components/shared/Card";
import TextInput from "components/shared/TextInput";
import React, { useState } from "react";
import { useChangeOwnPasswordMutation } from "redux/service/api";

const ChangePassword = () => {
   const [password, setPassword] = useState("");
   const [confirmPassword, setConfirmPassword] = useState("");

   const [mutation, mutationState] = useChangeOwnPasswordMutation();

   return (
      <Card id="change-password">
         <p className="category-title">Hasło</p>
         <p>Podaj nowe hasło w celu zmiany.</p>
         <div className="row">
            <TextInput
               label="Hasło"
               placeholder="Hasło"
               required
               value={password}
               onChange={(e) => setPassword(e.target.value)}
            />
            <TextInput
               label="Powtórz hasło"
               placeholder="Hasło"
               required
               value={confirmPassword}
               onChange={(e) => setConfirmPassword(e.target.value)}
            />
            <Button
               onClick={() => {
                  console.log("Change");
               }}
            >
               Zmień
            </Button>
         </div>
      </Card>
   );
};

export default ChangePassword;
