import { IconDropdown } from "components/shared";
import React, { useEffect, useState } from "react";
import { useTranslation } from "react-i18next";
import { IoLanguage } from "react-icons/io5";
import { useAppSelector } from "redux/hooks";
import { useChangeAccountLocaleMutation } from "redux/service/userSettingsService";
import { Language } from "types/Language";
import styles from "./LanguageDropdown.module.scss";

export const LanguageDropdown = () => {
   const { i18n } = useTranslation();
   const { exp } = useAppSelector((state) => state.auth);

   const [changeLocale] = useChangeAccountLocaleMutation();

   const lng = {
      pl: "Polski",
      en: "English",
   };

   useEffect(() => {
      if (Object.keys(lng).includes(i18n.language)) {
         setSelected(i18n.language);
      } else {
         i18n.changeLanguage("en");
      }
   }, [i18n.language]);

   const [selected, setSelected] = useState(i18n.language);

   return (
      <IconDropdown
         className={styles.container}
         options={lng}
         value={selected}
         onChange={async (key: Language) => {
            setSelected(key);

            if (i18n.language !== key && exp > Date.now()) {
               await changeLocale(key);
            }
            i18n.changeLanguage(key);
         }}
         icon={<IoLanguage />}
      />
   );
};
