import React, { useState } from "react";
import styles from "./ListUsersFilter.module.scss";
import { Card, TextInput, SquareButton, Button } from "components/shared";
import {
   FilterDropdownInput,
   FilterTextInput,
} from "components/account-management/list-account";
import { FaSearchMinus, FaSearchPlus } from "react-icons/fa";
import { useTranslation } from "react-i18next";

interface Props {
   query: string;
   setQuery: (q: string) => void;
}

export const ListUsersFilter: React.FC<Props> = ({ query, setQuery }) => {
   const { t } = useTranslation();

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
                     label={t("user_account_list_page.login")}
                     isActive={loginIsActive}
                     setIsActive={setLoginIsActive}
                     value={login}
                     setValue={setLogin}
                  />
                  <FilterTextInput
                     label={t("user_account_list_page.email")}
                     isActive={emailIsActive}
                     setIsActive={setEmailIsActive}
                     value={email}
                     setValue={setEmail}
                  />
                  <FilterTextInput
                     label={t("user_account_list_page.first_name")}
                     isActive={nameIsActive}
                     setIsActive={setNameIsActive}
                     value={name}
                     setValue={setName}
                  />
                  <FilterTextInput
                     label={t("user_account_list_page.second_name")}
                     isActive={surnameIsActive}
                     setIsActive={setSurnameIsActive}
                     value={surname}
                     setValue={setSurname}
                  />
                  <FilterDropdownInput
                     label={t("user_account_list_page.active")}
                     isActive={activeIsActive}
                     setIsActive={setActiveIsActive}
                     value={active}
                     setValue={setActive}
                     possibleValues={[
                        t("user_account_list_page.filter.active"),
                        t("user_account_list_page.filter.not_active"),
                     ]}
                  />
                  <FilterDropdownInput
                     label={t("user_account_list_page.registered")}
                     isActive={registeredIsActive}
                     setIsActive={setRegisteredIsActive}
                     value={registered}
                     setValue={setRegistered}
                     possibleValues={[
                        t("user_account_list_page.filter.registered"),
                        t("user_account_list_page.filter.not_registered"),
                     ]}
                  />
               </div>
            ) : (
               <div className={styles.filters_wrapper_basic}>
                  <TextInput
                     value={query}
                     placeholder={t("user_account_list_page.filter.placeholder")}
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
                  title={t("user_account_list_page.filter.search")}
               >
                  {t("user_account_list_page.filter.search")}
               </Button>
               <SquareButton
                  onClick={() => {
                     setIsAdvancedSearch(!isAdvancedSearch);
                  }}
                  title={
                     isAdvancedSearch
                        ? t("user_account_list_page.filter.basic_search")
                        : t("user_account_list_page.filter.advanced_search")
                  }
               >
                  {isAdvancedSearch ? <FaSearchMinus /> : <FaSearchPlus />}
               </SquareButton>
            </div>
         </Card>
      </div>
   );
};
