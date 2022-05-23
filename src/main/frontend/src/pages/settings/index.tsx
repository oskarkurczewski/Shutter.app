import React from "react";
import "./style.scss";
import Card from "components/shared/Card";
import Button from "components/shared/Button";
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
         id: "photographer-become",
         label: "Ustawienia fotografa",
      },
      {
         icon: <HiCamera />,
         id: "photographer-profile",
         label: "Ustawienia profilu fotografa",
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
            <Card id="photographer-become">
               <p className="category-title">Ustawienia Fotografa</p>
               <div className="content">
                  <p>
                     W każdym momencie możesz zostać fotografem! Dzięki temu zyskasz
                     możliwość tworzenia profilu, umieszczania w nim zdjęć, a także
                     zarządzania rezerwacjami.
                  </p>
                  <div className="buttons">
                     <Button
                        onClick={() => {
                           console.log("todo");
                        }}
                     >
                        Zostań fotografem
                     </Button>
                  </div>
               </div>
            </Card>
            <Card id="photographer-profile">
               <p className="category-title">Ustawienia Profilu Fotografa</p>
               <div className="content">
                  <p>
                     W każdej chwili możesz edytować widoczność swojego profilu fotografa.
                  </p>
                  <p>
                     Uwaga! Po dokonaniu tej operacji zostaną anulowane wszystkie Twoje
                     przyszłe rezerwacje, a klienci dostaną wiadomość o wyłączeniu
                     widoczności profilu fotografa.
                  </p>
                  <div className="buttons">
                     <Button
                        className="red-button"
                        onClick={() => {
                           console.log("todo");
                        }}
                     >
                        Wyłącz widoczność profilu
                     </Button>
                  </div>
               </div>
            </Card>
         </div>
      </section>
   );
};

export default SettingsPage;
