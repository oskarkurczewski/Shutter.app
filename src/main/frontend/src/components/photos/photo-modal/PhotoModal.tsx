import React, { useState, useEffect } from "react";
import { Button, Modal } from "components/shared";
import styles from "./PhotoModal.module.scss";
import { t } from "i18next";
import { DateTime } from "luxon";
import { useLikePhotoRequestMutation } from "redux/service/photoService";

interface Props {
   isOpen: boolean;
   onSubmit: () => void;
   photo;
}

const PhotoModal: React.FC<Props> = ({ isOpen, onSubmit, photo }) => {
   const [isLiked, setIsLiked] = useState<boolean>(photo.liked);
   const [likes, setLikes] = useState<number>(photo.likeCount);
   const [likePhotoMutation, { isLoading, isError, isSuccess, error }] =
      useLikePhotoRequestMutation();

   const likePhoto = () => {
      likePhotoMutation(photo.id);
   };

   useEffect(() => {
      isSuccess && setLikes(likes + 1);
   }, [isSuccess]);

   return (
      <Modal
         type="info"
         isOpen={isOpen}
         onSubmit={onSubmit}
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
                        icon="favorite"
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
