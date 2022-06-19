import React, { useEffect, useState } from "react";
import styles from "./PhotographerReview.module.scss";
import { Stars } from "../../shared/stars";
import { PhotographerReviewReportModal } from "../photographer-review-report-modal";
import { MenuDropdown } from "components/shared/dropdown/menu-dropdown";
import { Button } from "components/shared/button";
import { MenuDropdownItem } from "components/shared/dropdown/menu-dropdown/menu-dropdown-item";
import { useTranslation } from "react-i18next";
import {
   useLikeReviewMutation,
   useUnlikeReviewMutation,
} from "redux/service/reviewService";
import { Toast } from "types";
import { useAppDispatch } from "redux/hooks";
import { push, ToastTypes } from "redux/slices/toastSlice";
import { ReviewLikeButton } from "./review-like-button";

interface Props {
   id?: number;
   name?: string;
   surname?: string;
   stars?: number;
   description?: string;
   likeCount: number;
   liked: boolean;
}

export const PhotographerReview: React.FC<Props> = ({
   id,
   name,
   surname,
   stars,
   description,
   likeCount,
   liked,
}) => {
   const [editReportModalIsOpen, setEditReportModalIsOpen] = useState<boolean>(false);
   const { t } = useTranslation();

   const reportReview = () => {
      setEditReportModalIsOpen(true);
   };

   const deleteReview = () => {
      //TODO: delete review
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
            <Stars stars={stars} backgroundVariant="score" />
         </div>
         <p className={styles.review_description}>{description}</p>
         <div className={styles.review_buttons}>
            <MenuDropdown>
               <MenuDropdownItem
                  value={t("photographer_page.report_button")}
                  onClick={reportReview}
               />
               <MenuDropdownItem
                  value={t("photographer_page.delete_button")}
                  onClick={deleteReview}
               />
            </MenuDropdown>
            <ReviewLikeButton id={id} likeCount={likeCount} liked={liked} />
         </div>
         <PhotographerReviewReportModal
            reviewId={id}
            isOpen={editReportModalIsOpen}
            onCancel={() => setEditReportModalIsOpen(false)}
         />
      </div>
   );
};
