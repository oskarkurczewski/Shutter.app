import React, { FC } from "react";
import { RiCheckboxCircleFill } from "react-icons/ri";
import { Toast } from ".";
import styles from "./ToastSuccess.module.scss";

interface Props {
   text: string;
   id: number;
}

export const ToastSuccess: FC<Props> = ({ text, id }) => {
   return (
      <Toast
         id={id}
         icon={<RiCheckboxCircleFill />}
         text={text}
         className={styles.toast}
      />
   );
};
