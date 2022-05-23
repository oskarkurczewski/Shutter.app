import React, { useState } from "react";
import "./style.scss";
import Card from "../../shared/Card";
import TextInput from "../../shared/TextInput";
import Button from "../../shared/Button";
import SquareButton from "components/shared/SquareButton";
import { FaSearchMinus, FaSearchPlus } from "react-icons/fa";
import FilterDropdownInput from "../FilterDropdownInput";
import FilterTextInput from "../FilterTextInput";

const ListUsersFilter: React.FC = () => {
   const [isAdvancedSearch, setIsAdvancedSearch] = useState(false);
   const [basicSearchValue, setBasicSearchValue] = useState("");

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
      <div className="user-filter-wrapper">
         <Card className="filters">
            {isAdvancedSearch ? (
               <div className="filters-wrapper-advanced">
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
               <div className="filters-wrapper-basic">
                  <TextInput
                     value={basicSearchValue}
                     placeholder="szukaj"
                     onChange={(e) => {
                        setBasicSearchValue(e.target.value);
                     }}
                  />
               </div>
            )}
            <div className="buttons">
               <Button
                  onClick={isAdvancedSearch ? submitAdvancedSearch : submitBasicSearch}
                  className="search-button"
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

export default ListUsersFilter;
