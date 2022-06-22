import React, { useEffect, useState } from "react";
import { Toast } from "types";
import { push, ToastTypes } from "redux/slices/toastSlice";
import { useTranslation } from "react-i18next";
import { useAppDispatch } from "redux/hooks";
import { LikeButton } from "components/photographer-profile/like-button";
import {
   useLikePhotoRequestMutation,
   useUnlikePhotoMutation,
} from "redux/service/photoService";

interface Props {
   id: number;
   likeCount: number;
   liked: boolean;
}

export const PhotoLikeButton: React.FC<Props> = ({ id, likeCount, liked }) => {
   const { t } = useTranslation();
   const dispatch = useAppDispatch();

   const [likeMutation, likeMutationState] = useLikePhotoRequestMutation();
   const [unlikeMutation, unlikeMutationState] = useUnlikePhotoMutation();
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
