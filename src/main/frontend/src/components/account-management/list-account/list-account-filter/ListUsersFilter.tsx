import React from "react";
import styles from "./ListUsersFilter.module.scss";
import { Card, TextInput } from "components/shared";
import { useTranslation } from "react-i18next";

interface Props {
   query: string;
   setQuery: (q: string) => void;
}

export const ListUsersFilter: React.FC<Props> = ({ query, setQuery }) => {
   const { t } = useTranslation();

   return (
      <div className={styles.user_filter_wrapper}>
         <Card className={styles.filters}>
            <div className={styles.filters_wrapper_basic}>
               <TextInput
                  value={query}
                  placeholder={t("user_account_list_page.filter.placeholder")}
                  onChange={(e) => {
                     setQuery(e.target.value);
                  }}
               />
            </div>
         </Card>
      </div>
   );
};
