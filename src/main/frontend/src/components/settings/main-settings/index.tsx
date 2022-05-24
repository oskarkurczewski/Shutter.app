import React, { useState } from "react";
import Card from "components/shared/Card";
import TextInput from "components/shared/TextInput";
import Button from "components/shared/Button";
import { useChangeOwnUserDataMutation } from "redux/service/api";

const MainSettings = () => {
   const [name, setName] = useState("");
   const [surname, setSurname] = useState("");

   const [mutation, { isLoading, isError, isSuccess }] = useChangeOwnUserDataMutation();

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
         </div>

         {isLoading && <p>Loading...</p>}
         {isError && <p>Nie udało się zaktualizować danych</p>}
         {isSuccess && <p>Dane zaktualizowane</p>}

         <Button
            onClick={() => {
               mutation({ name, surname });
            }}
         >
            Zmień
         </Button>
      </Card>
   );
};

export default MainSettings;
