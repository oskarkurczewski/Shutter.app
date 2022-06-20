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
   useRemoveOwnPhotographerReviewMutation,
   useRemoveSomeonesPhotographerReviewMutation,
} from "redux/service/reviewService";
import { Toast } from "types";
import { useAppDispatch, useAppSelector } from "redux/hooks";
import { push, ToastTypes } from "redux/slices/toastSlice";
import { AccessLevel } from "types";

interface Props {
   id?: number;
   authorLogin: string;
   name: string;
   surname: string;
   stars: number;
   description: string;
   likeCount: number;
   liked: boolean;
}

export const PhotographerReview: React.FC<Props> = ({
   id,
   authorLogin,
   name,
   surname,
   stars,
   description,
   likeCount,
   liked,
}) => {
   const [editReportModalIsOpen, setEditReportModalIsOpen] = useState<boolean>(false);
   const { t } = useTranslation();
   const { username, accessLevel } = useAppSelector((state) => state.auth);
   const [likeReviewMutation, likeReviewMutationState] = useLikeReviewMutation();
   const [removeOwnReviewMutation, removeOwnReviewMutationState] =
      useRemoveOwnPhotographerReviewMutation();
   const [removeSomeonesReviewMutation, removeSomeonesReviewMutationState] =
      useRemoveSomeonesPhotographerReviewMutation();
   const dispatch = useAppDispatch();

   const reportReview = () => {
      setEditReportModalIsOpen(true);
   };

   // polubienie recenzji

   const likeReview = () => {
      return likeReviewMutation(id);
   };

   useEffect(() => {
      if (likeReviewMutationState.isSuccess) {
         dispatch(push(likeSuccessToast));
      }
   }, [likeReviewMutationState.isSuccess]);

   useEffect(() => {
      if (likeReviewMutationState.isError) {
         dispatch(push(likeErrorToast));
      }
   }, [likeReviewMutationState.isError]);

   const likeSuccessToast: Toast = {
      type: ToastTypes.SUCCESS,
      text: t("toast.success_like"),
   };

   const likeErrorToast: Toast = {
      type: ToastTypes.ERROR,
      text: t("toast.error_like"),
   };

   // usuwanie recenzji
   const deleteReview = () => {
      if (username === authorLogin) {
         removeOwnReviewMutation(id);
      }
      if (accessLevel === AccessLevel.ADMINISTRATOR) {
         removeSomeonesReviewMutation(id);
      }
   };

   useEffect(() => {
      if (
         removeOwnReviewMutationState.isSuccess ||
         removeSomeonesReviewMutationState.isSuccess
      ) {
         dispatch(push(removeReviewSuccessToast));
      }
   }, [removeOwnReviewMutationState.isSuccess]);

   useEffect(() => {
      if (
         removeOwnReviewMutationState.isError ||
         removeSomeonesReviewMutationState.isError
      ) {
         dispatch(push(removeReviewErrorToast));
      }
   }, [removeOwnReviewMutationState.isError]);

   const removeReviewSuccessToast: Toast = {
      type: ToastTypes.SUCCESS,
      text: t("toast.success_remove_review"),
   };

   const removeReviewErrorToast: Toast = {
      type: ToastTypes.ERROR,
      text: t("toast.error_remove_review"),
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
            <Stars
               className={styles.review_info_stars}
               stars={stars}
               backgroundVariant="score"
            />
         </div>
         <p className={styles.review_description}>{description}</p>
         <div className={styles.review_buttons}>
            <MenuDropdown className={styles.review_more_button}>
               <MenuDropdownItem
                  value={t("photographer_page.report_button")}
                  onClick={reportReview}
               />
               {((username === authorLogin && accessLevel === AccessLevel.CLIENT) ||
                  accessLevel === AccessLevel.ADMINISTRATOR) && (
                  <MenuDropdownItem
                     value={t("photographer_page.delete_button")}
                     onClick={deleteReview}
                  />
               )}
            </MenuDropdown>
            <Button
               className={`${styles.review_like_button} ${
                  liked ? styles.review_like_button_liked : ""
               }`}
               onClick={likeReview}
               icon="favorite"
            >
               {likeCount?.toString()}
            </Button>
         </div>
         <PhotographerReviewReportModal
            reviewId={id}
            isOpen={editReportModalIsOpen}
            onCancel={() => setEditReportModalIsOpen(false)}
         />
      </div>
   );
};
