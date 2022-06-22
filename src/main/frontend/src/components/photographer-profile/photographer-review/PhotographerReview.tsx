import React, { useEffect, useState } from "react";
import styles from "./PhotographerReview.module.scss";
import { Stars } from "../../shared/stars";
import { PhotographerReviewReportModal } from "../photographer-review-report-modal";
import { MenuDropdown } from "components/shared/dropdown/menu-dropdown";
import { MenuDropdownItem } from "components/shared/dropdown/menu-dropdown/menu-dropdown-item";
import { useTranslation } from "react-i18next";
import {
   useRemoveOwnPhotographerReviewMutation,
   useRemoveSomeonesPhotographerReviewMutation,
} from "redux/service/reviewService";
import { Toast } from "types";
import { useAppDispatch, useAppSelector } from "redux/hooks";
import { push, ToastTypes } from "redux/slices/toastSlice";
import { ReviewLikeButton } from "components/photographer-profile/like-button";
import { Avatar } from "components/shared";
import { AccessLevel } from "types";

interface Props {
   id?: number;
   authorLogin: string;
   name: string;
   surname: string;
   email: string;
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
   email,
   stars,
   description,
   likeCount,
   liked,
}) => {
   const [editReportModalIsOpen, setEditReportModalIsOpen] = useState<boolean>(false);
   const { t } = useTranslation();

   const { username, accessLevel } = useAppSelector((state) => state.auth);
   const [removeOwnReviewMutation, removeOwnReviewMutationState] =
      useRemoveOwnPhotographerReviewMutation();
   const [removeSomeonesReviewMutation, removeSomeonesReviewMutationState] =
      useRemoveSomeonesPhotographerReviewMutation();
   const dispatch = useAppDispatch();

   const reportReview = () => {
      setEditReportModalIsOpen(true);
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

   const removeReviewSuccessToast: Toast = {
      type: ToastTypes.SUCCESS,
      text: t("toast.success_remove_review"),
   };

   const removeReviewErrorToast: Toast = {
      type: ToastTypes.ERROR,
      text: t("toast.error_remove_review"),
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

   return (
      <div className={styles.review_wrapper}>
         <Avatar className={styles.review_photo} email={email} />
         <div className={styles.review_info}>
            <p className="section-title">{`${name} ${surname}`}</p>
            <Stars
               className={styles.review_info_stars}
               stars={stars}
               backgroundVariant="score"
            />
         </div>
         <p className={styles.review_description}>{description}</p>
         <div className={styles.review_buttons}>
            <MenuDropdown>
               <MenuDropdownItem
                  value={t("photographer_page.report_button")}
                  onClick={reportReview}
               />
               {((username === authorLogin && accessLevel === AccessLevel.CLIENT) ||
                  accessLevel === AccessLevel.ADMINISTRATOR) && (
                  <MenuDropdownItem
                     value={t("photographer_page.delete_button")}
                     onClick={() => {
                        const confirmToast: Toast = {
                           type: ToastTypes.WARNING,
                           text: t("photographer_page.confirm_remove_review"),
                           confirm: {
                              onClick: () => deleteReview(),
                              text: t("global.label.confirm"),
                           },
                        };
                        dispatch(push(confirmToast));
                     }}
                  />
               )}
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
