import React from "react";
import styles from "./InfoElement.module.scss";

interface Props {
   icon: JSX.Element;
   children: string | number | JSX.Element;
}
export const InfoElement: React.FC<Props> = ({ icon, children }) => {
   return (
      <div className={styles.info_element_wrapper}>
         <div className={styles.icon}>{icon}</div>
         <div className={styles.text}>{children}</div>
      </div>
   );
};
