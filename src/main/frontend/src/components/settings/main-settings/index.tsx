import React, { useState } from "react";
import Card from "components/shared/Card";
import TextInput from "components/shared/TextInput";
import Button from "components/shared/Button";
import { useChangeOwnUserDataMutation } from "redux/service/api";

const MainSettings = () => {
   const [name, setName] = useState("");
   const [surname, setSurname] = useState("");

   const [mutation, mutationState] = useChangeOwnUserDataMutation();

   return (
      <Card id="main-settings">
         <p className="category-title">Główne ustawienia</p>
         <p>
            Poniższe zmiany będą widoczne dla fotografa po dokonaniu u niego rezerwacji.
         </p>
         <div className="row">
            <TextInput
               label="Imię"
               placeholder="Imię"
               required
               value={name}
               onChange={(e) => setName(e.target.value)}
            />
            <TextInput
               label="Nazwisko"
               placeholder="Nazwisko"
               required
               value={surname}
               onChange={(e) => setSurname(e.target.value)}
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

export default MainSettings;
