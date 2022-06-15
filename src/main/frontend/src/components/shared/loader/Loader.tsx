import React from "react";
import styles from "./Loader.module.scss";
import { Player } from "@lottiefiles/react-lottie-player";
import Animation from "assets/animations/logo.json";

interface Props {
   className?: string;
}

export const Loader: React.FC<Props> = ({ className }) => {
   return (
      <div className={`${styles.loader_wrapper} ${className ? className : ""}`}>
         <Player autoplay loop src={Animation} background="transparent" />
      </div>
   );
};
