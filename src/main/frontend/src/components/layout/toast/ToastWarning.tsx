import { Button } from "components/shared";
import React, { FC } from "react";
import { useTranslation } from "react-i18next";
import { RiErrorWarningFill } from "react-icons/ri";
import { useAppDispatch } from "redux/hooks";
import { remove } from "redux/slices/toastSlice";
import { Toast } from ".";
import styles from "./ToastWarning.module.scss";

interface ButtonObject {
   onClick: () => void;
   text: string;
}

interface Props {
   text: string;
   id: number;
   confirm?: ButtonObject;
   cancel?: ButtonObject;
}

export const ToastWarning: FC<Props> = ({ text, id, confirm, cancel }) => {
   const dispatch = useAppDispatch();

   return (
      <Toast
         id={id}
         icon={<RiErrorWarningFill />}
         text={text}
         className={styles.toast}
         buttons={
            <>
               {cancel && (
                  <Button
                     className={styles.cancel}
                     onClick={() => {
                        cancel.onClick();
                        dispatch(remove(id));
                     }}
                  >
                     {cancel.text}
                  </Button>
               )}
               {confirm && (
                  <Button
                     className={styles.confirm}
                     onClick={() => {
                        confirm.onClick();
                        dispatch(remove(id));
                     }}
                  >
                     {confirm.text}
                  </Button>
               )}
            </>
         }
      />
   );
};
