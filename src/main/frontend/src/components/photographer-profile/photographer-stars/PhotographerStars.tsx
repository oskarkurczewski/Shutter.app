import React from "react";
import styles from "./PhotographerStars.module.scss";
import { FaRegStar, FaStarHalfAlt, FaStar } from "react-icons/fa";

interface Props {
   className?: string;
   stars: number;
}

const countingStars = (stars: number) => {
   const fullStars = Math.floor(stars / 2);
   const halfStars = stars % 2;
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

   result.push(<p>{fullStars + halfStars * 0.5}/5</p>);

   return result;
};

export const PhotographerStars: React.FC<Props> = ({ className, stars }) => {
   return (
      <div className={`${styles.text_wrapper} ${className ? className : ""}`}>
         <ul>
            {countingStars(stars).map((oneRepublic, index) => (
               <li key={index}>{oneRepublic}</li>
            ))}
         </ul>
      </div>
   );
};
