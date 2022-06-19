import React from "react";
import styles from "./Modal.module.scss";
import { Button, Card } from "components/shared";
import { ModalRoot } from "./ModalRoot";
import { useTranslation } from "react-i18next";
import { ModalNotification } from "./modal-notification";
import { motion, AnimatePresence } from "framer-motion";

export type Notification = {
   type?: "info" | "error" | "warning" | "success";
   content: string;
   onSubmit?: () => void;
};

type Props =
   | {
        className?: string;
        type?: "confirm";
        isOpen: boolean;
        title: string;
        children: JSX.Element | JSX.Element[];
        notification?: Notification;
        cancelText?: string;
        submitText?: string;
        onCancel: () => void;
        onSubmit: () => void;
     }
   | {
        className?: string;
        type?: "info";
        isOpen: boolean;
        title: string;
        children: JSX.Element | JSX.Element[];
        notification?: Notification;
        cancelText?: never;
        submitText?: string;
        onCancel?: never;
        onSubmit: () => void;
     };

export const Modal: React.FC<Props> = ({
   className,
   children,
   type = "info",
   isOpen,
   title,
   notification,
   submitText,
   cancelText,
   onCancel,
   onSubmit,
}) => {
   const { t } = useTranslation();

   if (!isOpen) {
      return null;
   }

   return (
      <ModalRoot>
         <div className={styles.modal_wrapper}>
            <div
               className={styles.backdrop}
               role="button"
               tabIndex={-1}
               onKeyDown={() => onCancel()}
               onClick={() => onCancel()}
            />
            <AnimatePresence>
               <motion.div
                  className={styles.content}
                  initial={{ top: -300, opacity: 0.5 }}
                  animate={{ top: 80, opacity: 1 }}
                  exit={{ top: 2000, opacity: 0 }}
               >
                  <Card className={`${styles.card_wrapper} ${className && className}`}>
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
                  {notification && (
                     <ModalNotification
                        className={styles.notification}
                        onSubmit={notification.onSubmit}
                        content={notification.content}
                        type={notification.type}
                     />
                  )}
               </motion.div>
            </AnimatePresence>
         </div>
      </ModalRoot>
   );
};
