/* eslint-disable jsx-a11y/no-static-element-interactions */
/* eslint-disable jsx-a11y/click-events-have-key-events */
import React, { useState } from "react";
import "./style.scss";
import { Link, useLocation } from "react-router-dom";
import ClientButtonList from "../button-list/client";
import BarButton from "../bar-button";
import { RiLogoutBoxRLine, RiLoginBoxLine } from "react-icons/ri";
import { MdKeyboardArrowRight } from "react-icons/md";
import { AccessLevel } from "types/AccessLevel";
import GuestButtonList from "../button-list/guest";
import AdministratorButtonList from "../button-list/administrator";
import ModeratorButtonList from "../button-list/moderator";
import PhotographerButtonList from "../button-list/photographer";
import { useAppSelector } from "redux/hooks";

const Navbar = () => {
   const location = useLocation();
   const path = location.pathname;

   const [expanded, setExpanded] = useState(false);

   const { accessLevel } = useAppSelector((state) => state.auth);

   let btn_list;
   if (accessLevel) {
      switch (accessLevel) {
         case AccessLevel.ADMINISTRATOR:
            btn_list = <AdministratorButtonList path={path} expanded={expanded} />;
            break;
         case AccessLevel.MODERATOR:
            btn_list = <ModeratorButtonList path={path} expanded={expanded} />;
            break;
         case AccessLevel.PHOTOGRAPHER:
            btn_list = <PhotographerButtonList path={path} expanded={expanded} />;
            break;
         case AccessLevel.CLIENT:
            btn_list = <ClientButtonList path={path} expanded={expanded} />;
            break;
         default:
            btn_list = <GuestButtonList path={path} expanded={expanded} />;
            break;
      }
   }

   return (
      <div
         className={`layout-bar navbar ${
            expanded && "expanded"
         } ${accessLevel.toLowerCase()}`}
      >
         <div className="logo-container">
            <Link to="/" className="logo-wrapper">
               <img src="/icons/logo.svg" alt="shutter logo" className="navbar-logo" />
               {expanded && <p className="section-title">pipegz.app</p>}
            </Link>
         </div>
         <div className="bar-button-list-wrapper">
            <div className="bar-button-wrapper">{btn_list}</div>
            <div className="bar-button-wrapper">
               {accessLevel === AccessLevel.GUEST ? (
                  <BarButton
                     to="login"
                     icon={<RiLoginBoxLine />}
                     text="Zaloguj się"
                     expanded={expanded}
                     active={path === "/login"}
                  />
               ) : (
                  <BarButton
                     to="dashboard"
                     icon={<RiLogoutBoxRLine />}
                     text="Wyloguj się"
                     expanded={expanded}
                     active={path === "/dashboard"}
                  />
               )}
            </div>
         </div>
         <div className="expand-button-wrapper" onClick={() => setExpanded(!expanded)}>
            <MdKeyboardArrowRight />
         </div>
      </div>
   );
};

export default Navbar;
