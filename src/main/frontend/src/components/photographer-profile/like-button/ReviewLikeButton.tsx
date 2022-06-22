import React, { useEffect, useState } from "react";
import {
   useLikeReviewMutation,
   useUnlikeReviewMutation,
} from "redux/service/reviewService";
import { Toast } from "types";
import { push, ToastTypes } from "redux/slices/toastSlice";
import { useTranslation } from "react-i18next";
import { useAppDispatch } from "redux/hooks";
import { LikeButton } from "components/photographer-profile/like-button";

interface Props {
   id: number;
   liked: boolean;
   likeCount: number;
}

export const ReviewLikeButton: React.FC<Props> = ({ id, likeCount, liked }) => {
   const { t } = useTranslation();
   const dispatch = useAppDispatch();

   const [likeMutation, likeMutationState] = useLikeReviewMutation();
   const [unlikeMutation, unlikeMutationState] = useUnlikeReviewMutation();
   const [showAnimation, setShowAnimation] = useState(false);

   useEffect(() => {
      if (likeMutationState.isSuccess) {
         const successToast: Toast = {
            type: ToastTypes.SUCCESS,
            text: t("toast.success_like"),
         };

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
      <LikeButton
         liked={liked}
         likeCount={likeCount}
         onLike={() => likeMutation(id)}
         onUnlike={() => unlikeMutation(id)}
         showAnimation={showAnimation}
         setShowAnimation={setShowAnimation}
      />
   );
};
