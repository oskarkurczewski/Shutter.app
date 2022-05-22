import React from "react";
import "./style.scss";
import Card from "components/shared/Card";
import { HiCamera } from "react-icons/hi";
import { IoSettingsSharp } from "react-icons/io5";
import { useLocation } from "react-router-dom";

const SettingsPage: React.FC = () => {
   const location = useLocation();
   const sections = [
      {
         icon: <IoSettingsSharp />,
         id: "main-settings",
         label: "Główne ustawienia",
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
                     className={location.hash.includes(section.id) && "active"}
                  >
                     {section.icon}
                     <p className="label-bold">{section.label}</p>
                  </a>
               ))}
            </div>
         </div>
         <div className="content">
            {/* poszczególne sekcje powinny być w osobnych komponentach (można podpiąć je potem pod tablicę `sections` aby później wywołać je tutaj iterując po `sections`) */}
            <Card id="main-settings">
               <p className="category-title">Główne ustawienia</p>
               <div className="content">
                  <p>
                     Lorem, ipsum dolor sit amet consectetur adipisicing elit. Molestias
                     laboriosam consequatur rerum nulla temporibus consequuntur, veritatis
                     distinctio numquam sed fugiat doloribus modi aut ullam.
                  </p>
               </div>
            </Card>
         </div>
      </section>
   );
};

export default SettingsPage;
