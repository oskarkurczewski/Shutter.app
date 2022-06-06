import React from "react";
import styles from "./style.module.scss";
import Button from "../button";
import Card from "../card";
import ModalRoot from "./ModalRoot";
import { useTranslation } from "react-i18next";

type Props =
   | {
        className?: string;
        type?: "confirm";
        isOpen: boolean;
        onCancel: () => void;
        onSubmit: () => void;
        title: string;
        children: JSX.Element | JSX.Element[];
     }
   | {
        className?: string;
        type?: "info";
        isOpen: boolean;
        onCancel?: never;
        onSubmit: () => void;
        title: string;
        children: JSX.Element | JSX.Element[];
     };

const Modal: React.FC<Props> = ({
   className,
   children,
   type = "info",
   isOpen,
   onCancel,
   onSubmit,
   title,
}) => {
   const { t } = useTranslation();

   if (!isOpen) {
      return null;
   }

   return (
      <ModalRoot>
         <div className={styles.modal_backdrop}>
            <Card className={`${styles.modal_wrapper} ${className && className}`}>
               {title && (
                  <div className={styles.modal_header}>
                     <p className="category-title">{title}</p>
                  </div>
               )}
               <div>{children}</div>
               <div className={styles.modal_footer}>
                  {type === "confirm" && (
                     <Button
                        className={styles.cancel}
                        onClick={() => onCancel && onCancel()}
                     >
                        {t("label.cancel")}
                     </Button>
                  )}
                  <Button onClick={() => onSubmit()}>{t("label.submit")}</Button>
               </div>
            </Card>
         </div>
      </ModalRoot>
   );
};

export default Modal;
