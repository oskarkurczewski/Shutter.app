import React, { useState } from "react";
import Card from "../Card";
import "./style.scss";
import TextInput from "../TextInput";
import Checkbox from "../Checkbox";
import Dropdown from "../Dropdown";
import Button from "../Button";

const UserFilter = () => {
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

   const submitBasicSearch = () => {
      return;
   };

   return (
      <div className="user-filter-wrapper">
         <p className="category-title">Filtry</p>
         <Card className="filters">
            <div className="filters-wrapper">
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

            <div className="buttons">
               <Button
                  onClick={submitBasicSearch}
                  className="search-button"
                  title="Search"
               >
                  Szukaj
               </Button>
            </div>
         </Card>
      </div>
   );
};

interface TextInputProps {
   label: string;
   isActive: boolean;
   setIsActive: (value: boolean) => void;
   value: string;
   setValue: (value: string) => void;
}

const FilterTextInput: React.FC<TextInputProps> = ({
   label,
   isActive,
   setIsActive,
   value,
   setValue,
}) => {
   return (
      <div className={`filter text ${label}`}>
         <Checkbox
            onChange={(e) => {
               setIsActive(e.target.checked);
            }}
            value={isActive}
         />

         <TextInput
            value={value}
            onChange={(e) => {
               setValue(e.target.value);
            }}
            label={label}
         />
      </div>
   );
};

interface DropdownInputProps {
   label: string;
   isActive: boolean;
   setIsActive: (value: boolean) => void;
   value: string;
   setValue: (value: string) => void;
   possibleValues: string[];
}

const FilterDropdownInput: React.FC<DropdownInputProps> = ({
   label,
   isActive,
   setIsActive,
   value,
   setValue,
   possibleValues,
}) => {
   return (
      <div className={`filter dropdown ${label}`}>
         <Checkbox
            onChange={(e) => {
               setIsActive(e.target.checked);
            }}
            value={isActive}
         />
         <Dropdown
            values={possibleValues}
            selectedValue={value}
            onChange={(e) => {
               setValue(e.target.value);
            }}
            name={label}
            id={label}
         >
            {label}
         </Dropdown>
      </div>
   );
};

export default UserFilter;
