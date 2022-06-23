/* eslint-disable jsx-a11y/no-noninteractive-element-to-interactive-role */
import React, { useRef } from "react";
import styles from "./Stars.module.scss";
import { FaRegStar, FaStarHalfAlt, FaStar } from "react-icons/fa";

interface Props {
   className?: string;
   stars: number;
   backgroundVariant?: "all" | "score" | "none" | "hidden";
   setStars?: (stars: number) => void;
}

export const Stars: React.FC<Props> = ({
   className,
   stars,
   backgroundVariant = "none",
   setStars,
}) => {
   const fullStars = Math.floor(stars / 2);
   const halfStars = Math.floor(stars % 2);
   const countingStars = (stars: number) => {
      const result = [];

      for (let i = 0; i < fullStars; i++) {
         result.push(<FaStar />);
      }

      if (halfStars === 1) {
         result.push(<FaStarHalfAlt />);
      }

      for (let i = 0; i < 5 - fullStars - halfStars; i++) {
         result.push(<FaRegStar />);
      }

      return result;
   };

   const starsElement = useRef();

   return (
      <div
         className={`${styles.text_wrapper} ${
            backgroundVariant === "all" ? styles.all : ""
         } ${className ? className : ""}`}
      >
         <ul ref={starsElement}>
            {countingStars(stars).map((oneRepublic, index) => (
               <li key={index}>
                  {setStars && (
                     <div
                        role="button"
                        onClick={(e) => {
                           let half;
                           if (e.nativeEvent.offsetX < e.currentTarget.clientWidth / 3) {
                              half = 0;
                           } else if (
                              e.nativeEvent.offsetX <
                              (e.currentTarget.clientWidth * 2) / 3
                           ) {
                              half = 1;
                           } else {
                              half = 2;
                           }
                           setStars(index * 2 + half);
                        }}
                        tabIndex={-1}
                        onKeyDown={null}
                     />
                  )}
                  {oneRepublic}
               </li>
            ))}
         </ul>
         {backgroundVariant !== "hidden" && (
            <span className={backgroundVariant === "score" ? styles.score : ""}>
               {fullStars + halfStars * 0.5}/5
            </span>
         )}
      </div>
   );
};
