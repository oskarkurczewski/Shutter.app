import { Button } from "components/shared";
import React, { useState, useEffect } from "react";
import { useTranslation } from "react-i18next";
import {
   useDeletePhotoRequestMutation,
   useLikePhotoRequestMutation,
   useUnlikePhotoMutation,
} from "redux/service/photoService";
import styles from "./Photo.module.scss";
import { DateTime } from "luxon";
import { useAppDispatch } from "redux/hooks";
import { push, ToastTypes } from "redux/slices/toastSlice";
import { ErrorResponse, Toast } from "types";
import { parseError } from "util/errorUtil";
import PhotoModal from "../photo-modal/PhotoModal";

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
   photo: any;
}

export const Photo: React.FC<Props> = ({
   photo_id,
   img,
   title,
   onClick,
   date,
   liked,
   likeCount,
   showDeleteButton,
   photo = {},
}) => {
   const { t } = useTranslation();
   const dispatch = useAppDispatch();

   const [likePhotoMutation] = useLikePhotoRequestMutation();
   const [unlikePhoto] = useUnlikePhotoMutation();
   const [modalOpen, setModalOpen] = useState(false);

   const photoData = { ...photo, liked, likeCount };

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

   const likePhoto = () => {
      if (liked) {
         unlikePhoto(photo_id)
            .unwrap()
            .then(
               () => {
                  dispatch(
                     push({
                        text: t("global.component.photo.unlike_successful_message"),
                        type: ToastTypes.SUCCESS,
                     })
                  );
               },
               (err) => {
                  dispatch(
                     push({
                        text: t([err.data?.message || ".", "exception.unexpected"]),
                        type: ToastTypes.ERROR,
                     })
                  );
               }
            );
      } else {
         likePhotoMutation(photo_id)
            .unwrap()
            .then(
               () => {
                  dispatch(
                     push({
                        text: t("global.component.photo.like_successful_message"),
                        type: ToastTypes.SUCCESS,
                     })
                  );
               },
               (err) => {
                  dispatch(
                     push({
                        text: t([err.data?.message || ".", "exception.unexpected"]),
                        type: ToastTypes.ERROR,
                     })
                  );
               }
            );
      }
   };

   return (
      <div className={styles.photo_wrapper}>
         <div
            tabIndex={-1}
            role="button"
            onKeyDown={null}
            onClick={() => {
               onClick && onClick();
               setModalOpen(true);
            }}
         >
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
                  className={`${styles.photo_label_likes_button} ${
                     liked ? styles.active : ""
                  }`}
                  onClick={likePhoto}
                  icon={liked ? "favorite" : "favorite_outline"}
               >
                  {`${likeCount}`}
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
         <PhotoModal
            photo={photoData}
            isOpen={modalOpen}
            onSubmit={() => {
               setModalOpen(false);
            }}
         />
      </div>
   );
};
