import React, { FC } from "react";
import { RiCloseCircleFill } from "react-icons/ri";
import { Toast } from ".";
import styles from "./ToastError.module.scss";

interface Props {
   text: string;
   id: number;
}

export const ToastError: FC<Props> = ({ text, id }) => {
   return (
      <Toast
         id={id}
         icon={<RiCloseCircleFill />}
         text={text}
         className={styles.toast}
         permanent
      />
   );
};
