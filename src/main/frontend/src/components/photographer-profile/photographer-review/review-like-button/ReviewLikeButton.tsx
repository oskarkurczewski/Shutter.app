import { Button } from "components/shared";
import styles from "./ReviewLikeButton.module.scss";
import React, { useEffect, useState } from "react";
import {
   useLikeReviewMutation,
   useUnlikeReviewMutation,
} from "redux/service/reviewService";
import { Toast } from "types";
import { push, ToastTypes } from "redux/slices/toastSlice";
import { useTranslation } from "react-i18next";
import { useAppDispatch } from "redux/hooks";
import Animation from "assets/animations/like_boom.json";
import { Player } from "@lottiefiles/react-lottie-player";

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

   const [isLiked, setIsLiked] = useState(false);
   const [likeCounter, setLikeCounter] = useState(0);
   const [showAnimation, setShowAnimation] = useState(false);

   useEffect(() => {
      setIsLiked(liked);
      setLikeCounter(likeCount);
   }, []);

   // Remove animation after 1s
   useEffect(() => {
      if (showAnimation) {
         setTimeout(() => {
            setShowAnimation(false);
         }, 1000);
      }
   }, [showAnimation]);

   useEffect(() => {
      if (likeMutationState.isSuccess) {
         const successToast: Toast = {
            type: ToastTypes.SUCCESS,
            text: t("toast.success_like"),
         };

         setIsLiked(true);
         setLikeCounter(likeCounter + 1);
         setShowAnimation(true);
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

         setIsLiked(false);
         setLikeCounter(likeCounter - 1);
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
      <div className={styles.wrapper}>
         {showAnimation && <Player autoplay src={Animation} background="transparent" />}
         <Button
            className={`${styles.button_wrapper} ${isLiked ? styles.liked : ""}`}
            onClick={() => {
               isLiked ? unlikeMutation(id) : likeMutation(id);
            }}
            icon="favorite"
         >
            {likeCounter?.toString()}
         </Button>
      </div>
   );
};
