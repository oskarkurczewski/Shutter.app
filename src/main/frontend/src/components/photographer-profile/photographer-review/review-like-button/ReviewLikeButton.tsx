import { Button } from "components/shared";
import styles from "./ReviewLikeButton.module.scss";
import React, { useEffect } from "react";
import {
   useLikeReviewMutation,
   useUnlikeReviewMutation,
} from "redux/service/reviewService";
import { Toast } from "types";
import { push, ToastTypes } from "redux/slices/toastSlice";
import { useTranslation } from "react-i18next";
import { useAppDispatch } from "redux/hooks";

interface Props {
   id: number;
   likeCount: number;
   liked: boolean;
}

export const ReviewLikeButton: React.FC<Props> = ({ id, likeCount, liked }) => {
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

   return (
      <Button
         className={`${styles.button_wrapper} ${liked ? styles.liked : ""}`}
         onClick={() => {
            liked ? unlikeMutation(id) : likeMutation(id);
         }}
         icon="favorite"
      >
         {likeCount?.toString()}
      </Button>
   );
};
