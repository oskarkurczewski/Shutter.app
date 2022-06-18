import React from "react";
import styles from "./PhotographerDescription.module.scss";
import { Card, IconText } from "components/shared";
import { getSpecializationProps } from "util/photographerUtil";
import { useTranslation } from "react-i18next";

interface Props {
   specializationList?: string[];
   description?: string;
}

export const PhotographerDescription: React.FC<Props> = ({
   specializationList,
   description,
}) => {
   const { t } = useTranslation();

   return (
      <div className={styles.photographer_description_wrapper}>
         <Card className={styles.data_wrapper}>
            <div className={styles.specialization_wrapper}>
               <div className={styles.specialization_column}>
                  <p className="section-title">
                     {t("photographer_page.specializations")}
                  </p>
                  <ul>
                     {specializationList.map((spec, index) => {
                        const specProps = getSpecializationProps(spec);
                        return (
                           <li key={index}>
                              <IconText
                                 icon={specProps.icon}
                                 color={specProps.color}
                                 text={t(
                                    `photographer_specialization.${spec.toLowerCase()}`
                                 )}
                                 className={`${styles.text_wrapper}`}
                              />
                           </li>
                        );
                     })}
                  </ul>
               </div>
               <div>
                  <p className="section-title">{t("photographer_page.description")}</p>
                  <p>{description}</p>
               </div>
            </div>
         </Card>
      </div>
   );
};
