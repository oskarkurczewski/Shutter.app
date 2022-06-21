import { Button } from "components/shared";
import React, { useState } from "react";
import { useTranslation } from "react-i18next";
import {
   useLikePhotoRequestMutation,
   useUnlikePhotoMutation,
} from "redux/service/photoService";
import styles from "./Photo.module.scss";
import { DateTime } from "luxon";
import { useAppDispatch } from "redux/hooks";
import { push, ToastTypes } from "redux/slices/toastSlice";
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
   onDelete?: (number) => void;
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
   onDelete,
   photo = {},
}) => {
   const { t } = useTranslation();
   const dispatch = useAppDispatch();

   const [likes, setLikes] = useState<number>(likeCount);
   const [isLiked, setIsLiked] = useState<boolean>(liked);
   const [likePhotoMutation] = useLikePhotoRequestMutation();
   const [unlikePhoto] = useUnlikePhotoMutation();
   const [modalOpen, setModalOpen] = useState(false);

   const photoData = { ...photo, liked: isLiked, likeCount: likes };

   const likePhoto = () => {
      if (isLiked) {
         unlikePhoto(photo_id)
            .unwrap()
            .then(
               () => {
                  setIsLiked(false);
                  setLikes(likes - 1);
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
                  setIsLiked(true);
                  setLikes(likes + 1);
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
                     isLiked ? styles.active : ""
                  }`}
                  onClick={likePhoto}
                  icon={isLiked ? "favorite" : "favorite_outline"}
               >
                  {`${likes}`}
               </Button>
            </div>
            {showDeleteButton && (
               <div className={styles.photo_label_delete}>
                  <Button
                     className={styles.photo_label_delete_button}
                     onClick={() => onDelete(photo_id)}
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
            onSubmit={(likes, isLiked) => {
               setLikes(likes);
               setIsLiked(isLiked);
               setModalOpen(false);
            }}
         />
      </div>
   );
};
