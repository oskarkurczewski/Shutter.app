import React from "react";
import styles from "./style.module.scss";
import { HiCamera } from "react-icons/hi";
import { IoSettingsSharp } from "react-icons/io5";
import { useLocation } from "react-router-dom";
import BecomePhotographer from "components/settings/photographer-settings/become-photographer";
import StopBeingPhotographer from "components/settings/photographer-settings/stop-being-photographer";
import { useAppSelector } from "redux/hooks";
import { AccessLevel } from "types/AccessLevel";
import MainSettings from "components/settings/main-settings";
import ChangeEmail from "components/settings/change-email";
import ChangePassword from "components/settings/change-password";
import { BsKeyFill } from "react-icons/bs";
import { MdEmail } from "react-icons/md";
import { useTranslation } from "react-i18next";

const SettingsPage: React.FC = () => {
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
            <ChangeEmail />
            <ChangePassword />
            {roles.includes(AccessLevel.PHOTOGRAPHER) ? (
               <StopBeingPhotographer />
            ) : (
               <BecomePhotographer />
            )}
         </div>
      </section>
   );
};

export default SettingsPage;
