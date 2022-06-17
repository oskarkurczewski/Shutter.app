import React from "react";
import styles from "./Modal.module.scss";
import { Button, Card } from "components/shared";
import { ModalRoot } from "./ModalRoot";
import { useTranslation } from "react-i18next";

type Props =
   | {
        className?: string;
        type?: "confirm";
        isOpen: boolean;
        onCancel: () => void;
        onSubmit: () => void;
        title: string | JSX.Element;
        children: JSX.Element | JSX.Element[];
        cancelText?: string;
        submitText?: string;
     }
   | {
        className?: string;
        type?: "info";
        isOpen: boolean;
        onCancel?: never;
        onSubmit: () => void;
        title: string | JSX.Element;
        children: JSX.Element | JSX.Element[];
        cancelText?: never;
        submitText?: string;
     };

export const Modal: React.FC<Props> = ({
   className,
   children,
   type = "info",
   isOpen,
   onCancel,
   onSubmit,
   title,
   submitText,
   cancelText,
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
                        {cancelText ? cancelText : t("global.label.cancel")}
                     </Button>
                  )}
                  <Button onClick={() => onSubmit()}>
                     {submitText ? submitText : t("global.label.submit")}
                  </Button>
               </div>
            </Card>
         </div>
      </ModalRoot>
   );
};
