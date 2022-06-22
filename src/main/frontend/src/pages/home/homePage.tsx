import React from "react";
import styles from "./homePage.module.scss";
import { Button } from "components/shared";
import { useAppSelector } from "redux/hooks";
import { useTranslation } from "react-i18next";
import { Link } from "react-router-dom";
import { AccessLevel } from "types";
import { Player } from "@lottiefiles/react-lottie-player";
import Animation from "assets/animations/particles.json";

export const HomePage = () => {
   const { t } = useTranslation();
   const { accessLevel, username } = useAppSelector((state) => state.auth);
   const buttons = {
      GUEST: [
         { url: "/login", label: "main_page.sign_in" },
         { url: "/register", label: "main_page.sign_up" },
      ],
      CLIENT: [
         { url: "/photographers", label: "main_page.photographers" },
         { url: "/settings", label: "main_page.settings" },
      ],
      PHOTOGRAPHER: [
         { url: "/settings", label: "main_page.settings" },
         { url: "/profile/gallery", label: "main_page.gallery" },
         { url: `/profile/${username}`, label: "main_page.photographer_profile" },
      ],
      ADMINISTRATOR: [
         { url: "/settings", label: "main_page.settings" },
         { url: "/users", label: "main_page.users" },
         { url: "/photographers", label: "main_page.photographers" },
      ],
      MODERATOR: [
         { url: "/users", label: "main_page.users" },
         { url: "/photographers", label: "main_page.photographers" },
         { url: "/settings", label: "main_page.settings" },
      ],
   };

   const getButtons = (accessLevel) => {
      return (
         <>
            {buttons[accessLevel].map((button, index) => (
               <Link key={index} to={button.url}>
                  <Button className={styles.button} onClick={null}>
                     {t(button.label)}
                  </Button>
               </Link>
            ))}
         </>
      );
   };

   return (
      <div className={styles.home_page}>
         <Player
            src={Animation}
            background="transparent"
            className={styles.particles}
            autoplay
            loop
         />
         <img src="/icons/logo.svg" alt="shutter logo" className={styles.app_logo} />
         <h1 className={styles.home_page_title}>SHUTTER.APP</h1>
         {accessLevel === AccessLevel.GUEST && (
            <p className="category-title">{t("main_page.description")}</p>
         )}
         {accessLevel !== AccessLevel.GUEST && (
            <p className="category-title">
               {t("main_page.welcome")},{" "}
               <span className={styles.username}>{username}</span>!
            </p>
         )}

         <div className={styles.buttons_container}>{getButtons(accessLevel)}</div>
      </div>
   );
};
