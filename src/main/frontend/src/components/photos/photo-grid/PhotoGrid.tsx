import React from "react";
import { useTranslation } from "react-i18next";
import { useAppSelector } from "redux/hooks";
import { useGetPhotosRequestQuery } from "redux/service/photoService";
import { Photo } from "../photo/Photo";
import styles from "./PhotoGrid.module.scss";

export const PhotoGrid = () => {
   const { t } = useTranslation();
   const auth = useAppSelector((state) => state.auth);

   const { data } = useGetPhotosRequestQuery({
      photographerLogin: auth.username,
   });

   return (
      <div className={styles.photo_grid}>
         {data !== undefined && data.list.length > 0 ? (
            data.list.map((photo, i) => {
               return (
                  <div key={i} className={styles.photo_grid_item}>
                     <Photo
                        photo_id={photo.id}
                        img={photo.s3Url}
                        title={photo.title}
                        description={photo.description}
                        date={photo.createdAt}
                        likeCount={photo.likeCount}
                        liked={photo.liked}
                     ></Photo>
                  </div>
               );
            })
         ) : (
            <h4 className={styles.no_photos}>{t("photographer_gallery_page")}</h4>
         )}
      </div>
   );
};
