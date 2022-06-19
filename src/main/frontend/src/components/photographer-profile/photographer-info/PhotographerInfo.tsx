import React from "react";
import styles from "./PhotographerInfo.module.scss";
import { Avatar, Card, IconText } from "components/shared";
import { RiFileList2Fill } from "react-icons/ri";
import { Stars } from "../../shared/stars";
import { useTranslation } from "react-i18next";

interface Props {
   name: string;
   surname: string;
   email: string;
   stars: number;
   reviewCount: number;
}

export const PhotographerInfo: React.FC<Props> = ({
   name,
   surname,
   email,
   stars,
   reviewCount,
}) => {
   const { t } = useTranslation();

   return (
      <Card className={styles.data_wrapper}>
         <Avatar email={email} className={styles.photographer_photo} size={512} />
         <div className={styles.labels_wrapper}>
            <p className="section-title">{`${name} ${surname}`}</p>
            <Stars
               stars={Math.round((stars / reviewCount) * 10) / 10}
               className={`label-bold ${styles.label}`}
               backgroundVariant="all"
            />
            <IconText
               text={`${reviewCount} ${t("photographer_page.photographer_review_count")}`}
               Icon={RiFileList2Fill}
               className={`label-bold ${styles.label}`}
            />
         </div>
      </Card>
   );
};
