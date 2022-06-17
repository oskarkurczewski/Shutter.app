import React, { useState, useEffect } from "react";
import { Modal, TextArea, Notification } from "components/shared";
import { useTranslation } from "react-i18next";
import { Stars } from "components/shared/stars";
import styles from "./AddReviewModal.module.scss";
import { useAddReviewMutation } from "redux/service/reviewService";
import { ErrorResponse } from "types";
import { parseError } from "util/errorUtil";
import { Toast } from "types";
import { useAppDispatch } from "redux/hooks";
import { push, ToastTypes } from "redux/slices/toastSlice";
interface Props {
   isOpen: boolean;
   photographerLogin: string;
   onSubmit: () => void;
   onCancel: () => void;
}

const AddReviewModal: React.FC<Props> = ({
   isOpen,
   photographerLogin,
   onSubmit,
   onCancel,
}) => {
   const { t } = useTranslation();
   const [stars, setStars] = useState<number>(8);
   const [desc, setDesc] = useState<string>("");
   const [notification, setNotification] = useState<Notification>(null);
   const [addReviewMutation, addReviewMutationState] = useAddReviewMutation();
   const dispatch = useAppDispatch();

   const successToast: Toast = {
      type: ToastTypes.SUCCESS,
      text: t("toast.success_review"),
   };

   const errorToast: Toast = {
      type: ToastTypes.ERROR,
      text: t("toast.error_review"),
   };

   useEffect(() => {
      if (addReviewMutationState.isSuccess) {
         dispatch(push(successToast));
         onSubmit();
      }
      if (addReviewMutationState.isError) {
         dispatch(push(errorToast));
         setNotification({
            type: "error",
            content: t(
               parseError(
                  addReviewMutationState.error as ErrorResponse,
                  "photographer_page.add_review"
               )
            ),
         });
      }
   }, [addReviewMutationState]);

   return (
      <Modal
         className={styles.add_review_modal}
         type="confirm"
         isOpen={isOpen}
         onSubmit={() => {
            addReviewMutation({
               photographerLogin: photographerLogin,
               score: stars,
               content: desc,
            });
         }}
         onCancel={onCancel}
         title={t("photographer_page.add_review")}
         submitText={t("photographer_page.add_review")}
      >
         <div className={styles.review_form}>
            <p>{t("photographer_page.your_opinion")}</p>
            <Stars stars={stars} className={styles.review_stars} setStars={setStars} />
            <p>{t("photographer_page.opinion_content")}</p>
            <TextArea
               className={styles.desc_field}
               value={desc}
               onChange={(e) => setDesc(e.target.value)}
            />
         </div>
      </Modal>
   );
};

export default AddReviewModal;
