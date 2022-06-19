import React from "react";
import styles from "./settingsPage.module.scss";
import { HiCamera } from "react-icons/hi";
import { BsChatSquareTextFill } from "react-icons/bs";
import { IoSettingsSharp } from "react-icons/io5";
import { useLocation } from "react-router-dom";
import {
   BecomePhotographerSettings,
   StopBeingPhotographerSettings,
   MainSettings,
   ChangeEmailSettings,
   ChangePasswordSettings,
   ChangeDescriptionSettings,
   ChangeSpecializationsSettings,
} from "components/settings";
import { useAppSelector } from "redux/hooks";
import { AccessLevel } from "types/AccessLevel";
import { BsKeyFill } from "react-icons/bs";
import { MdEmail } from "react-icons/md";
import { useTranslation } from "react-i18next";
import { FaStar } from "react-icons/fa";

type Section = {
   icon: JSX.Element;
   id: string;
   label: string;
   roles?: AccessLevel[];
};

export const SettingsPage: React.FC = () => {
   const { t } = useTranslation();
   const location = useLocation();

   const roles = useAppSelector((state) => state.auth.roles);

   const sections: Section[] = [
      {
         icon: <IoSettingsSharp />,
         id: "main-settings",
         label: t("settings_page.main_settings.title"),
      },
      {
         icon: <MdEmail />,
         id: "change-email",
         label: t("settings_page.email_settings.title"),
      },
      {
         icon: <BsKeyFill />,
         id: "change-password",
         label: t("settings_page.password_settings.title"),
      },
      {
         icon: <HiCamera />,
         id: "photographer-settings",
         label: t("settings_page.photographer_settings.title"),
      },
      {
         icon: <BsChatSquareTextFill />,
         id: "change-description",
         label: t("settings_page.change_description.title"),
         roles: [AccessLevel.PHOTOGRAPHER],
      },
      {
         icon: <FaStar />,
         id: "change-specializations",
         label: t("settings_page.change_specializations.title"),
         roles: [AccessLevel.PHOTOGRAPHER],
      },
   ];

   return (
      <section className={styles.settings_page_wrapper}>
         <div className={styles.nav}>
            <p className="category-title">{t("settings_page.title")}</p>
            <div className={styles.links}>
               {sections.map((section, index) => {
                  if (
                     section.roles &&
                     section.roles.some((role) => roles.indexOf(role))
                  ) {
                     return;
                  }
                  return (
                     <a
                        key={index}
                        href={`#${section.id}`}
                        className={
                           location.hash.includes(section.id) ? styles.active : ""
                        }
                     >
                        {section.icon}
                        <p className="label-bold">{section.label}</p>
                     </a>
                  );
               })}
            </div>
         </div>
         <div className={styles.content}>
            <MainSettings />
            <ChangeEmailSettings />
            <ChangePasswordSettings />
            {roles.includes(AccessLevel.PHOTOGRAPHER) ? (
               <>
                  <StopBeingPhotographerSettings />
                  <ChangeSpecializationsSettings />
                  <ChangeDescriptionSettings />
               </>
            ) : (
               <BecomePhotographerSettings />
            )}
         </div>
      </section>
   );
};
