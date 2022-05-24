import React from "react";
import "./style.scss";
import Button from "../Button";
import Card from "../Card";
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
         <div className="modal-backdrop">
            <Card className={`modal-wrapper ${className ? className : ""}`}>
               {title && (
                  <div className="modal-header">
                     <p className="category-title">{title}</p>
                  </div>
               )}
               <div className="modal-content">{children}</div>
               <div className="modal-footer">
                  {type === "confirm" && (
                     <Button className="cancel" onClick={() => onCancel && onCancel()}>
                        {t("label.cancel")}
                     </Button>
                  )}
                  <Button className="submit" onClick={() => onSubmit()}>
                     {t("label.submit")}
                  </Button>
               </div>
            </Card>
         </div>
      </ModalRoot>
   );
};

export default Modal;
