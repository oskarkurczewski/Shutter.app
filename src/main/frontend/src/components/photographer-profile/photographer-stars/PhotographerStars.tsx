import React from "react";
import styles from "./PhotographerStars.module.scss";
import { FaRegStar, FaStarHalfAlt, FaStar } from "react-icons/fa";

interface Props {
   className?: string;
   stars: number;
}

const countStars = (stars: number) => {
   const fullStars = Math.floor(stars / 2);
   const halfStars = stars % 2;
   const result = [];

   let i = 0;
   for (i = 0; i < fullStars; i++) {
      result.push(
         <li key={i}>
            <FaStar />
         </li>
      );
   }

   if (halfStars === 1) {
      result.push(
         <li key={i}>
            <FaStarHalfAlt />
         </li>
      );
   }

   for (let i = 0; i < 5 - fullStars - halfStars; i++) {
      result.push(
         <li key={i}>
            <FaRegStar />
         </li>
      );
   }

   result.push(<p>{fullStars + halfStars * 0.5}/5</p>);

   return <ul>{result}</ul>;
};

export const PhotographerStars: React.FC<Props> = ({ className, stars }) => {
   return (
      <div className={`${styles.text_wrapper} ${className ? className : ""}`}>
         {countStars(stars)}
      </div>
   );
};
