import React, { useState } from "react";
import styles from "./PhotographerDescription.module.scss";
import { Card, IconText, MenuDropdown } from "components/shared";
import { getSpecializationProps } from "util/photographerUtil";
import { useTranslation } from "react-i18next";
import { MenuDropdownItem } from "components/shared/dropdown/menu-dropdown/menu-dropdown-item";
import { PhotographerReportModal } from "../photographer-report-modal";

interface Props {
   specializationList?: string[];
   description?: string;
}

export const PhotographerDescription: React.FC<Props> = ({
   specializationList,
   description,
}) => {
   const { t } = useTranslation();

   const [reportModalIsVisible, setReportModalIsVisible] = useState<boolean>(false);

   return (
      <>
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
               <div className={styles.description_column}>
                  <div>
                     <p className="section-title">{t("photographer_page.description")}</p>
                     <p>{description}</p>
                  </div>
                  <MenuDropdown>
                     <MenuDropdownItem
                        onClick={() => {
                           setReportModalIsVisible(true);
                        }}
                        value="Report"
                     />
                  </MenuDropdown>
               </div>
            </div>
         </Card>
         <PhotographerReportModal
            isOpen={reportModalIsVisible}
            onCancel={() => {
               setReportModalIsVisible(false);
            }}
         />
      </>
   );
};
