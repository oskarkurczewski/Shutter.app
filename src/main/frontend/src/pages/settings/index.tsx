import React from "react";
import "./style.scss";
import Card from "components/shared/Card";
import { HiCamera } from "react-icons/hi";
import { IoSettingsSharp } from "react-icons/io5";
import { useLocation } from "react-router-dom";
import MainSettings from "components/settings/main-settings";
import ChangeEmail from "components/settings/change-email";
import ChangePassword from "components/settings/change-password";
import { BsKeyFill } from "react-icons/bs";
import { MdEmail } from "react-icons/md";

const SettingsPage: React.FC = () => {
   const location = useLocation();
   const sections = [
      {
         icon: <IoSettingsSharp />,
         id: "main-settings",
         label: "Główne ustawienia",
      },
      {
         icon: <MdEmail />,
         id: "change-email",
         label: "Adres email",
      },
      {
         icon: <BsKeyFill />,
         id: "change-password",
         label: "Hasło",
      },
      {
         icon: <HiCamera />,
         id: "photographer",
         label: "Ustawienia fotografa",
      },
   ];

   return (
      <section className="settings-page-wrapper">
         <div className="nav">
            <p className="category-title">Ustawienia</p>
            <div className="links">
               {sections.map((section, index) => (
                  <a
                     key={index}
                     href={`#${section.id}`}
                     className={location.hash.includes(section.id) ? "active" : ""}
                  >
                     {section.icon}
                     <p className="label-bold">{section.label}</p>
                  </a>
               ))}
            </div>
         </div>
         <div className="content">
            {/* poszczególne sekcje powinny być w osobnych komponentach (można podpiąć je potem pod tablicę `sections` aby później wywołać je tutaj iterując po `sections`) */}
            <MainSettings />
            <ChangeEmail />
            <ChangePassword />
         </div>
      </section>
   );
};

export default SettingsPage;
