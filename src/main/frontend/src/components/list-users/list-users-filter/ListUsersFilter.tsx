import React, { useState } from "react";
import styles from "./ListUsersFilter.module.scss";
import { Card, TextInput, SquareButton, Button } from "components/shared";
import { FilterDropdownInput, FilterTextInput } from "components/list-users";
import { FaSearchMinus, FaSearchPlus } from "react-icons/fa";

interface Props {
   query: string;
   setQuery: (q: string) => void;
}

export const ListUsersFilter: React.FC<Props> = ({ query, setQuery }) => {
   const [isAdvancedSearch, setIsAdvancedSearch] = useState(false);

   // TODO: change to custom hooks
   const [login, setLogin] = useState("");
   const [email, setEmail] = useState("");
   const [name, setName] = useState("");
   const [surname, setSurname] = useState("");
   const [registered, setRegistered] = useState("");
   const [active, setActive] = useState("");

   const [loginIsActive, setLoginIsActive] = useState(false);
   const [emailIsActive, setEmailIsActive] = useState(false);
   const [nameIsActive, setNameIsActive] = useState(false);
   const [surnameIsActive, setSurnameIsActive] = useState(false);
   const [registeredIsActive, setRegisteredIsActive] = useState(false);
   const [activeIsActive, setActiveIsActive] = useState(false);

   /* 
   w przypadku gdy nie jest wyszukiwane po danym polu, do backendu 
   należy wysłać zapytanie BEZ danego parametru a nie pusty string
    */
   const submitAdvancedSearch = () => {
      return;
   };

   const submitBasicSearch = () => {
      return;
   };

   return (
      <div className={styles.user_filter_wrapper}>
         <Card className={styles.filters}>
            {isAdvancedSearch ? (
               <div className={styles.filters_wrapper_advanced}>
                  <FilterTextInput
                     label="login"
                     isActive={loginIsActive}
                     setIsActive={setLoginIsActive}
                     value={login}
                     setValue={setLogin}
                  />
                  <FilterTextInput
                     label="email"
                     isActive={emailIsActive}
                     setIsActive={setEmailIsActive}
                     value={email}
                     setValue={setEmail}
                  />
                  <FilterTextInput
                     label="name"
                     isActive={nameIsActive}
                     setIsActive={setNameIsActive}
                     value={name}
                     setValue={setName}
                  />
                  <FilterTextInput
                     label="surname"
                     isActive={surnameIsActive}
                     setIsActive={setSurnameIsActive}
                     value={surname}
                     setValue={setSurname}
                  />
                  <FilterDropdownInput
                     label="aktywacja"
                     isActive={activeIsActive}
                     setIsActive={setActiveIsActive}
                     value={active}
                     setValue={setActive}
                     possibleValues={["aktywny", "nieaktywny"]}
                  />
                  <FilterDropdownInput
                     label="rejestracja"
                     isActive={registeredIsActive}
                     setIsActive={setRegisteredIsActive}
                     value={registered}
                     setValue={setRegistered}
                     possibleValues={["zarejestrowany", "niezarejestrowany"]}
                  />
               </div>
            ) : (
               <div className={styles.filters_wrapper_basic}>
                  <TextInput
                     value={query}
                     placeholder="szukaj"
                     onChange={(e) => {
                        setQuery(e.target.value);
                     }}
                  />
               </div>
            )}
            <div className={styles.buttons}>
               <Button
                  onClick={isAdvancedSearch ? submitAdvancedSearch : submitBasicSearch}
                  className={styles.search_button}
                  title="Search"
               >
                  Szukaj
               </Button>
               <SquareButton
                  onClick={() => {
                     setIsAdvancedSearch(!isAdvancedSearch);
                  }}
                  title={isAdvancedSearch ? "Basic search" : "Advanced search"}
               >
                  {isAdvancedSearch ? <FaSearchMinus /> : <FaSearchPlus />}
               </SquareButton>
            </div>
         </Card>
      </div>
   );
};
