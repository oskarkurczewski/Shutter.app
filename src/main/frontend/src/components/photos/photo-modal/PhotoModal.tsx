import React, { useState, useEffect } from "react";
import { Button, Modal } from "components/shared";
import styles from "./PhotoModal.module.scss";
import { t } from "i18next";
import { DateTime } from "luxon";
import {
   useLikePhotoRequestMutation,
   useUnlikePhotoMutation,
} from "redux/service/photoService";
import { push, ToastTypes } from "redux/slices/toastSlice";
import { useAppDispatch } from "redux/hooks";

interface Props {
   isOpen: boolean;
   onSubmit: (likes, isLiked) => void;
   photo;
}

const PhotoModal: React.FC<Props> = ({ isOpen, onSubmit, photo }) => {
   const dispatch = useAppDispatch();

   const [isLiked, setIsLiked] = useState<boolean>(photo.liked);
   const [likes, setLikes] = useState<number>(photo.likeCount);
   const [likePhotoMutation] = useLikePhotoRequestMutation();
   const [unlikePhoto] = useUnlikePhotoMutation();

   useEffect(() => {
      setIsLiked(photo.liked);
      setLikes(photo.likeCount);
   }, [photo]);

   const likePhoto = () => {
      if (isLiked) {
         unlikePhoto(photo.id)
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
         likePhotoMutation(photo.id)
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
      <Modal
         type="info"
         isOpen={isOpen}
         onSubmit={() => {
            onSubmit(likes, isLiked);
         }}
         title={t("photographer_gallery_page.photo")}
         submitText={t("photographer_gallery_page.close")}
      >
         <div className={styles.modal_container}>
            <img className={styles.photo} src={photo.s3Url} alt={photo.title}></img>
            <div className={styles.photo_info}>
               <div className={styles.summary}>
                  <h3>{photo.title}</h3>
                  <div className={styles.photo_label_likes}>
                     <Button
                        className={styles.photo_label_likes_button}
                        onClick={likePhoto}
                        icon={isLiked ? "favorite" : "favorite_outline"}
                     >
                        {`${likes}`}
                     </Button>
                  </div>
               </div>
               <div className={styles.details}>
                  <p className={styles.user}>
                     <span className={styles.important}>{photo.photographerName}</span> (
                     {photo.photographerLogin})
                  </p>
                  <p className={styles.date}>
                     <span className={styles.important}>
                        {DateTime.fromISO(photo.createdAt).toFormat("dd.MM.yyyy, HH:mm")}
                     </span>
                  </p>
                  <p className={styles.desc}>{photo.description}</p>
               </div>
            </div>
         </div>
      </Modal>
   );
};

export default PhotoModal;
