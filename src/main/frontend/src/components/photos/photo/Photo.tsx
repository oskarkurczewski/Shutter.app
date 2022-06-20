import { Button } from "components/shared";
import React, { useEffect, useState } from "react";
import { useTranslation } from "react-i18next";
import {
   useDeletePhotoRequestMutation,
   useLikePhotoRequestMutation,
} from "redux/service/photoService";
import styles from "./Photo.module.scss";
import { DateTime } from "luxon";
import { useAppDispatch } from "redux/hooks";
import { push, ToastTypes } from "redux/slices/toastSlice";
import { ErrorResponse, Toast } from "types";
import { parseError } from "util/errorUtil";

interface Props {
   photo_id: number;
   img: string;
   title: string;
   onClick?: () => void;
   description?: string;
   date: string;
   liked: boolean;
   likeCount: number;
   showDeleteButton: boolean;
   refetch?: () => void;
}

export const Photo: React.FC<Props> = ({
   photo_id,
   img,
   title,
   onClick,
   description,
   date,
   liked,
   likeCount,
   showDeleteButton,
   refetch,
}) => {
   const { t } = useTranslation();
   const dispatch = useAppDispatch();

   const [likes, setLikes] = useState<number>(likeCount);
   const [isLiked, setIsLiked] = useState<boolean>(liked);
   const [likePhotoMutation, { isLoading, isError, isSuccess, error }] =
      useLikePhotoRequestMutation();

   const [deletePhotoMutation, deletePhotoMutationState] =
      useDeletePhotoRequestMutation();

   //Handle delete photo response
   useEffect(() => {
      if (deletePhotoMutationState.isSuccess) {
         const successToast: Toast = {
            type: ToastTypes.SUCCESS,
            text: t("toast.success_remove_photo"),
         };

         dispatch(push(successToast));
         refetch();
      }
      if (deletePhotoMutationState.isError) {
         const err = deletePhotoMutationState.error as ErrorResponse;
         const errorToast: Toast = {
            type: ToastTypes.ERROR,
            text: t(parseError(err)),
         };
         dispatch(push(errorToast));
      }
   }, [deletePhotoMutationState]);

   useEffect(() => {
      isSuccess && setLikes(likes + 1);
   }, [isSuccess]);

   return (
      <div className={styles.photo_wrapper}>
         <div tabIndex={-1} role="button" onKeyDown={null} onClick={onClick}>
            <picture className={styles.photo_picture}>
               <source media="(min-width: 0px)" srcSet={img} />
               <img src={img} alt="photographers_photo_from_gallery" />
            </picture>
         </div>
         <div className={styles.photo_label}>
            <div className={styles.photo_label_content}>
               <p className={styles.photo_label_title}>{title}</p>
               <p className={styles.photo_label_date}>
                  {DateTime.fromISO(date).toFormat("yyyy.MM.dd")}
               </p>
            </div>
            <div className={styles.photo_label_likes}>
               <Button
                  className={styles.photo_label_likes_button}
                  onClick={() => {
                     likePhotoMutation(photo_id);
                  }}
                  icon="favorite"
               >
                  {`${likes}`}
               </Button>
            </div>
            {showDeleteButton && (
               <div className={styles.photo_label_delete}>
                  <Button
                     className={styles.photo_label_delete_button}
                     onClick={() => {
                        const confirmToast: Toast = {
                           type: ToastTypes.WARNING,
                           text: t("photographer_page.confirm_remove_photo"),
                           confirm: {
                              onClick: () => deletePhotoMutation(photo_id),
                              text: t("global.label.confirm"),
                           },
                        };
                        dispatch(push(confirmToast));
                     }}
                     icon="delete"
                  >
                     {" "}
                  </Button>
               </div>
            )}
         </div>
      </div>
   );
};
