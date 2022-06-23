import React from "react";
import { useTranslation } from "react-i18next";
import { useGetPhotosRequestQuery } from "redux/service/photoService";
import { Photo } from "../photo/Photo";
import styles from "./PhotoMasonry.module.scss";

interface Props {
   login: string;
}

export const PhotoMasonry: React.FC<Props> = ({ login }) => {
   const { t } = useTranslation();
   const { data } = useGetPhotosRequestQuery(
      {
         photographerLogin: login,
         recordsPerPage: 100,
      },
      {
         refetchOnMountOrArgChange: true,
         // pollingInterval: 3000,
      }
   );

   return (
      <div className={styles.photo_masonry}>
         {data !== undefined && data.list.length > 0 ? (
            data.list.map((photo, i) => {
               return (
                  <div
                     aria-hidden="true"
                     key={i}
                     id={`${i}`}
                     className={styles.photo_masonry_item}
                  >
                     <Photo
                        photo={photo}
                        photo_id={photo.id}
                        img={photo.s3Url}
                        title={photo.title}
                        description={photo.description}
                        date={photo.createdAt}
                        likeCount={photo.likeCount}
                        liked={photo.liked}
                        showDeleteButton={false}
                     />
                  </div>
               );
            })
         ) : (
            <h4 className={styles.no_photos}>
               {t("photographer_page.gallery_no_photos")}
            </h4>
         )}
      </div>
   );
};
