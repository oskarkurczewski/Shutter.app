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
   const dispatch = useAppDispatch();

   const [likeMutation, likeMutationState] = useLikeReviewMutation();
   const [unlikeMutation, unlikeMutationState] = useUnlikeReviewMutation();

   useEffect(() => {
      if (likeMutationState.isSuccess) {
         const successToast: Toast = {
            type: ToastTypes.SUCCESS,
            text: t("toast.success_like"),
         };

         dispatch(push(successToast));
      }
      if (likeMutationState.isError) {
         const errorToast: Toast = {
            type: ToastTypes.ERROR,
            text: t("toast.error_like"),
         };

         dispatch(push(errorToast));
      }
   }, [likeMutationState]);

   useEffect(() => {
      if (unlikeMutationState.isSuccess) {
         const successToast: Toast = {
            type: ToastTypes.SUCCESS,
            text: t("toast.success_unlike"),
         };

         dispatch(push(successToast));
      }
      if (unlikeMutationState.isError) {
         const errorToast: Toast = {
            type: ToastTypes.ERROR,
            text: t("toast.error_unlike"),
         };

         dispatch(push(errorToast));
      }
   }, [unlikeMutationState]);

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
            <Button
               className={styles.button_wrapper}
               onClick={() => {
                  liked ? unlikeMutation(id) : likeMutation(id);
               }}
               icon="thumb_up"
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
