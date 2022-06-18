import { Button, Card } from "components/shared";
import { motion } from "framer-motion";
import React, { FC } from "react";
import { useTranslation } from "react-i18next";
import { useNavigate } from "react-router-dom";
import { DetailedPhotographerInfo } from "redux/types/api";
import { SpecializationTag } from ".";

interface ListElementProps {
   data?: DetailedPhotographerInfo;
   styles: { [className: string]: string };
   custom: number;
}

export const ListElement: FC<ListElementProps> = ({ data, styles, custom }) => {
   const { t } = useTranslation();
   const navigate = useNavigate();

   const { name, surname, specializations, score, reviewCount, login } = data;

   const variants = {
      initial: {
         y: "15vh",
         opacity: 0,
      },
      animate: (i) => ({
         y: 0,
         transition: {
            delay: i * 0.2,
            duration: 0.4,
            ease: "easeOut",
         },
      }),
      animateOpacity: (i) => ({
         opacity: 1,
         transition: {
            delay: i * 0.2,
            duration: 0.3,
            ease: "easeOut",
         },
      }),
      exit: {
         opacity: 0,
         transition: {
            duration: 0.5,
         },
      },
   };

   return (
      <motion.div
         variants={variants}
         initial="initial"
         animate={["animate", "animateOpacity"]}
         exit="exit"
         className={styles.card_wrapper}
         custom={custom}
      >
         <Card className={`${styles.card} ${styles["list_element"]}`}>
            <div className={styles["avatar_wrapper"]}>
               <div className={styles.avatar}>
                  <img
                     src="https://cdn.galleries.smcloud.net/t/galleries/gf-hTB5-Uktt-KEJc_norbert-dis-gierczak-664x442.jpg"
                     alt="fotograf"
                  />
               </div>
               <div>
                  <p className="section-title">
                     {name} {surname}
                  </p>
                  <p className="label">{t("photographer_list_page.photographer")}</p>
               </div>
            </div>
            <div className={styles.specialization_container}>
               {specializations.map((v) => (
                  <SpecializationTag
                     key={v}
                     text={t(`global.specialization.${v.toLocaleLowerCase()}`)}
                     specialization={v}
                     className={styles.specialization}
                  />
               ))}
            </div>

            <div className={styles.score_container}>
               <p className="label">
                  {score.toPrecision(2)} (
                  {t("photographer_list_page.reviews", { count: reviewCount })})
               </p>
            </div>

            <div className={styles.button_container}>
               <Button
                  className={styles.gallery_button}
                  onClick={() => navigate(`/profile/${login}`)}
               >
                  {t("photographer_list_page.profile")}
               </Button>
               <Button onClick={() => navigate(`/profile/${login}/#callendar`)}>
                  {t("photographer_list_page.hire")}
               </Button>
            </div>
         </Card>
      </motion.div>
   );
};
