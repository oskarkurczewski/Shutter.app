import React from "react";
import styles from "./ModalNotification.module.scss";
import { Card } from "components/shared/card";
import { MdInfo } from "react-icons/md";
import {
   RiCheckboxCircleFill,
   RiCloseCircleFill,
   RiErrorWarningFill,
} from "react-icons/ri";
import { Button } from "components/shared/button";
import { useTranslation } from "react-i18next";

interface Props {
   type?: "info" | "error" | "warning" | "success";
   content: string;
   className?: string;
   onSubmit?: () => void;
}

const options = {
   info: <MdInfo />,
   error: <RiCloseCircleFill />,
   warning: <RiErrorWarningFill />,
   success: <RiCheckboxCircleFill />,
};

export const ModalNotification: React.FC<Props> = ({
   content,
   type = "info",
   className,
   onSubmit,
}) => {
   const { t } = useTranslation();

   return (
      <Card
         className={`${styles.modal_notification_wrapper} ${className ? className : ""}`}
      >
         <div className={`${styles.content} ${styles[type]}`}>
            <div>
               {options[type]}
               <p>{content}</p>
            </div>
            {onSubmit && (
               <Button className={styles.button} onClick={onSubmit}>
                  {t("global.label.submit")}
               </Button>
            )}
         </div>
      </Card>
   );
};
