import { Button } from "components/shared";
import React, { useEffect, useState } from "react";
import { useTranslation } from "react-i18next";
import { useLikePhotoRequestMutation } from "redux/service/photoService";
import styles from "./photo.module.scss";

interface Props {
   photo_id: number;
   img: string;
   title: string;
   description?: string;
   date: string;
   liked: boolean;
   likeCount: number;
}

export const Photo: React.FC<Props> = ({
   photo_id,
   img,
   title,
   description,
   date,
   liked,
   likeCount,
}) => {
   const [likes, setLikes] = useState<number>(likeCount);
   const [isLiked, setIsLiked] = useState<boolean>(liked);
   const [likePhotoMutation, { isLoading, isError, isSuccess, error }] =
      useLikePhotoRequestMutation();

   const likePhoto = () => {
      likePhotoMutation(photo_id);
   };

   useEffect(() => {
      isSuccess && setLikes(likes + 1);
   }, [isSuccess]);

   return (
      <div className={styles.photo_wrapper}>
         <picture className={styles.photo_picture}>
            <source media="(min-width: 0px)" srcSet={img} />
            <img src={img} alt="photographers_photo_from_gallery" />
         </picture>
         <div className={styles.photo_label}>
            <div className={styles.photo_label_content}>
               <p className={styles.photo_label_title}>{title}</p>
               <p className={styles.photo_label_date}>{date}</p>
            </div>
            <div className={styles.photo_label_likes}>
               <Button
                  className={styles.photo_label_likes_button}
                  onClick={likePhoto}
                  icon="favorite"
               >
                  {likes}
               </Button>
            </div>
         </div>
      </div>
   );
};
