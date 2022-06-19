import React, { useEffect, useState } from "react";
import styles from "./PhotographerReview.module.scss";
import { Stars } from "../../shared/stars";
import { PhotographerReviewReportModal } from "../photographer-report-modal";
import { MenuDropdown } from "components/shared/dropdown/menu-dropdown";
import { Button } from "components/shared/button";
import { MenuDropdownItem } from "components/shared/dropdown/menu-dropdown/menu-dropdown-item";
import { useTranslation } from "react-i18next";
import { useLikeReviewMutation } from "redux/service/reviewService";
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
}

export const PhotographerReview: React.FC<Props> = ({
   id,
   name,
   surname,
   stars,
   description,
   likeCount,
}) => {
   const [editReportModalIsOpen, setEditReportModalIsOpen] = useState<boolean>(false);
   const { t } = useTranslation();

   const [mutation, { isLoading, isError, isSuccess, error }] = useLikeReviewMutation();

   const likeReview = () => {
      return mutation(id);
   };

   const dispatch = useAppDispatch();

   useEffect(() => {
      if (isSuccess) {
         dispatch(push(successToast));
      }
   }, [isSuccess]);

   useEffect(() => {
      if (isError) {
         dispatch(push(errorToast));
      }
   }, [isError]);

   const reportReview = () => {
      setEditReportModalIsOpen(true);
   };

   const deleteReview = () => {
      //TODO: delete review
   };

   const successToast: Toast = {
      type: ToastTypes.SUCCESS,
      text: t("toast.success_like"),
   };

   const errorToast: Toast = {
      type: ToastTypes.ERROR,
      text: t("toast.error_like"),
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
               onClick={likeReview}
               icon="thumb_up"
            >
               {likeCount?.toString()}
            </Button>
         </div>
         <PhotographerReviewReportModal
            reviewId={id}
            isOpen={editReportModalIsOpen}
            onSubmit={() => setEditReportModalIsOpen(false)}
            onCancel={() => setEditReportModalIsOpen(false)}
         />
      </div>
   );
};
