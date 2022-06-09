import React from "react";
import styles from "./settingsPage.module.scss";
import { HiCamera } from "react-icons/hi";
import { IoSettingsSharp } from "react-icons/io5";
import { useLocation } from "react-router-dom";
import {
   BecomePhotographerSettings,
   StopBeingPhotographerSettings,
   MainSettings,
   ChangeEmailSettings,
   ChangePasswordSettings,
} from "components/settings";
import { useAppSelector } from "redux/hooks";
import { AccessLevel } from "types/AccessLevel";
import { BsKeyFill } from "react-icons/bs";
import { MdEmail } from "react-icons/md";
import { useTranslation } from "react-i18next";

export const SettingsPage: React.FC = () => {
   const { t } = useTranslation();
   const location = useLocation();

   const roles = useAppSelector((state) => state.auth.roles);

   const sections = [
      {
         icon: <IoSettingsSharp />,
         id: "main-settings",
         label: t("label.settings"),
      },
      {
         icon: <MdEmail />,
         id: "change-email",
         label: t("label.email"),
      },
      {
         icon: <BsKeyFill />,
         id: "change-password",
         label: t("label.password"),
      },
      {
         icon: <HiCamera />,
         id: "photographer-settings",
         label: t("label.photographer-settings"),
      },
   ];

   return (
      <section className={styles.settings_page_wrapper}>
         <div className={styles.nav}>
            <p className="category-title">{t("label.settings-short")}</p>
            <div className={styles.links}>
               {sections.map((section, index) => (
                  <a
                     key={index}
                     href={`#${section.id}`}
                     className={location.hash.includes(section.id) && styles.active}
                  >
                     {section.icon}
                     <p className="label-bold">{section.label}</p>
                  </a>
               ))}
            </div>
         </div>
         <div className={styles.content}>
            <MainSettings />
            <ChangeEmailSettings />
            <ChangePasswordSettings />
            {roles.includes(AccessLevel.PHOTOGRAPHER) ? (
               <StopBeingPhotographerSettings />
            ) : (
               <BecomePhotographerSettings />
            )}
         </div>
      </section>
   );
};
