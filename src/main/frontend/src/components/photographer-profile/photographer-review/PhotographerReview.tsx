import React, { useState } from "react";
import styles from "./PhotographerReview.module.scss";
import { Button, SquareButton } from "components/shared";
import { FaEllipsisH } from "react-icons/fa";
import { PhotographerStars } from "../photographer-stars";
import { PhotographerReviewReportModal } from "../photographer-report-modal";

interface Props {
   id?: number;
   name?: string;
   surname?: string;
   stars?: number;
   description?: string;
}

export const PhotographerReview: React.FC<Props> = ({
   id,
   name,
   surname,
   stars,
   description,
}) => {
   const [editModalIsOpen, setEditModalIsOpen] = useState<boolean>(false);

   const reportReview = () => {
      setEditModalIsOpen(true);
   };

   return (
      <div className={styles.review_wrapper}>
         <img
            src="/images/avatar.png"
            alt="reviewer_photo"
            className={styles.review_photo}
         />
         <div className={styles.review_info}>
            <p>
               {name} {surname}
            </p>
            <PhotographerStars stars={stars} />
         </div>
         <p className={styles.review_description}>{description}</p>
         <div className={styles.review_buttons}>
            <SquareButton onClick={reportReview}>
               <FaEllipsisH />
            </SquareButton>
            {/* TODO: like button */}
         </div>
         <PhotographerReviewReportModal
            reviewId={id}
            isOpen={editModalIsOpen}
            onSubmit={() => setEditModalIsOpen(false)}
            onCancel={() => setEditModalIsOpen(false)}
         />
      </div>
   );
};
