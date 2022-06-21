import { Button, Modal, Stars } from "components/shared";
import React from "react";
import styles from "./ReviewReportModal.module.scss";
import {
   useGetReviewByIdQuery,
   useRemoveSomeonesPhotographerReviewMutation,
} from "redux/service/reviewService";
import { DateTime } from "luxon";
import { MdThumbUp } from "react-icons/md";
import { HiCamera, HiClock, HiUser } from "react-icons/hi";
import { InfoElement } from "./InfoElement";
import { FaEye } from "react-icons/fa";
import { Toast } from "types";
import { push, ToastTypes } from "redux/slices/toastSlice";
import { useAppDispatch } from "redux/hooks";
import { useTranslation } from "react-i18next";

interface Props {
   isOpen: boolean;
   onSubmit: () => void;
   reviewId: number;
}

export const ReviewReportModal: React.FC<Props> = ({ isOpen, onSubmit, reviewId }) => {
   const { t } = useTranslation();
   const dispatch = useAppDispatch();

   const reviewData = useGetReviewByIdQuery(reviewId);

   const [removeMutation, removeMutationState] =
      useRemoveSomeonesPhotographerReviewMutation();

   return (
      <Modal
         type={"info"}
         isOpen={isOpen}
         onSubmit={onSubmit}
         title={`${t("reports_page.review.modal.title")} ${reviewId}`}
         className={styles.review_report_modal_wrapper}
         submitText={t("reports_page.review.modal.submit")}
         notification={
            removeMutationState.isSuccess
               ? {
                    type: "success",
                    content: t("reports_page.review.modal.success_message"),
                 }
               : removeMutationState.isError
               ? { type: "error", content: t("reports_page.review.modal.error_message") }
               : undefined
         }
      >
         <section>
            <div className={styles.main}>
               <div className={styles.info}>
                  <InfoElement icon={<FaEye />}>
                     {reviewData?.data?.active
                        ? t("reports_page.review.modal.visible")
                        : t("reports_page.review.modal.invisible")}
                  </InfoElement>
                  <InfoElement icon={<HiUser />}>
                     {reviewData?.data?.reviewerLogin}
                  </InfoElement>
                  <InfoElement icon={<HiCamera />}>
                     {reviewData?.data?.photographerLogin}
                  </InfoElement>
                  <InfoElement icon={<HiClock />}>
                     {DateTime.fromJSDate(new Date(reviewData?.data?.createdAt)).toFormat(
                        "dd.MM.yyyy, HH:mm"
                     )}
                  </InfoElement>
                  <InfoElement icon={<MdThumbUp />}>
                     {reviewData?.data?.likeCount}
                  </InfoElement>
                  <div className={styles.buttons}></div>
               </div>
               <div className={styles.content}>
                  <Stars
                     stars={reviewData?.data?.score}
                     className={styles.stars}
                     backgroundVariant="none"
                  />
                  <p>{reviewData?.data?.content}</p>
               </div>
            </div>
            <Button
               className={styles.button}
               disabled={!reviewData?.data?.active}
               onClick={() => {
                  removeMutation(reviewData?.data?.id).then(() => {
                     reviewData.refetch();
                  });
               }}
               loading={removeMutationState.isLoading}
            >
               {t("reports_page.review.modal.delete")}
            </Button>
         </section>
      </Modal>
   );
};
