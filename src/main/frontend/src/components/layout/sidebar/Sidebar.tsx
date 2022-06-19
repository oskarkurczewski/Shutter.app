import React from "react";
import styles from "./Sidebar.module.scss";
import { AuthCard, LanguageDropdown, RoleDropdown } from "components/layout";
import { AnimatePresence } from "framer-motion";
import { motion } from "framer-motion";

export const Sidebar = () => {
   return (
      <div className={`layout-bar ${styles.sidebar_wrapper}`}>
         <AuthCard />
         <AnimatePresence>
            <motion.div
               className={styles.dropdowns_wrapper}
               initial={{ opacity: 0 }}
               animate={{ opacity: 1 }}
               exit={{ opacity: 0 }}
            >
               <LanguageDropdown />
               <RoleDropdown />
            </motion.div>
         </AnimatePresence>
      </div>
   );
};
