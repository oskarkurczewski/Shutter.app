import React, { useEffect, useState } from "react";
import { useTranslation } from "react-i18next";
import { useAppSelector } from "redux/hooks";
import { useChangeAccountLocaleMutation } from "redux/service/userSettingsService";
import { Language } from "types/Language";
import { IconDropdown } from ".";

export const LanguageDropdown = () => {
   const { i18n } = useTranslation();
   const { exp } = useAppSelector((state) => state.auth);

   const [changeLocale] = useChangeAccountLocaleMutation();

   const lng = {
      pl: "Polski",
      en: "English",
      de: "Deutsch",
      dee: "A",
      deed: "B",
      deeee: "C",
      deeeee: "D",
      deeeeee: "E",
   };

   useEffect(() => {
      setSelected(i18n.language);
   }, [i18n.language]);

   const [selected, setSelected] = useState(i18n.language);

   return (
      <IconDropdown
         options={lng}
         value={selected}
         onChange={async (key: Language) => {
            setSelected(key);

            if (i18n.language !== key && exp > Date.now()) {
               await changeLocale(key);
            }
            i18n.changeLanguage(key);
         }}
      />
   );
};
