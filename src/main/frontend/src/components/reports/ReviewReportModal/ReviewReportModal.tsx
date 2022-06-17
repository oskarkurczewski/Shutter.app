import { Modal, Stars } from "components/shared";
import React, { useEffect } from "react";
import styles from "./ReviewReportModal.module.scss";
import { useGetReviewByIdQuery } from "redux/service/reviewService";
import { DateTime } from "luxon";
import { MdThumbUp } from "react-icons/md";
import { HiCamera, HiClock, HiUser } from "react-icons/hi";
import { InfoElement } from "./InfoElement";
import { FaCheck, FaEye } from "react-icons/fa";

interface Props {
   isOpen: boolean;
   onSubmit: () => void;
   reviewId: number;
}

export const ReviewReportModal: React.FC<Props> = ({ isOpen, onSubmit, reviewId }) => {
   const reviewData = useGetReviewByIdQuery(reviewId);

   return (
      <Modal
         type="info"
         isOpen={isOpen}
         onSubmit={onSubmit}
         title={`Recenzja ${reviewId}`}
         className={styles.review_report_modal_wrapper}
      >
         <section>
            <div className={styles.info}>
               <InfoElement icon={<FaEye />}>
                  {reviewData?.data?.active ? <FaCheck /> : ""}
               </InfoElement>
               <InfoElement icon={<HiUser />}>
                  {reviewData?.data?.reviewerLogin}
               </InfoElement>
               <InfoElement icon={<HiCamera />}>
                  {reviewData?.data?.photographerLogin}
               </InfoElement>
               <InfoElement icon={<MdThumbUp />}>
                  {reviewData?.data?.likeCount}
               </InfoElement>
               <InfoElement icon={<HiClock />}>
                  {DateTime.fromJSDate(new Date(reviewData?.data?.createdAt)).toFormat(
                     "dd.MM.yyyy, HH.mm.ss"
                  )}
               </InfoElement>
            </div>
            <div className={styles.separator} />
            <div className={styles.content}>
               <Stars
                  stars={reviewData?.data?.score}
                  className={styles.stars}
                  backgroundVariant="all"
               />
               <p>{reviewData?.data?.content}</p>
            </div>
         </section>
      </Modal>
      // )
   );
};
