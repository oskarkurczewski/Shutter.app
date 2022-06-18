import React from "react";
import styles from "./Loader.module.scss";
import { Player } from "@lottiefiles/react-lottie-player";
import Animation from "assets/animations/logo.json";
import { AnimatePresence, motion } from "framer-motion";

interface Props {
   className?: string;
}

export const Loader: React.FC<Props> = ({ className }) => {
   return (
      <AnimatePresence>
         <motion.div
            className={`${styles.loader_wrapper} ${className ? className : ""}`}
            initial={{ opacity: 0 }}
            animate={{ opacity: 1 }}
            exit={{ opacity: 0 }}
         >
            <Player autoplay loop src={Animation} background="transparent" />
         </motion.div>
      </AnimatePresence>
   );
};
