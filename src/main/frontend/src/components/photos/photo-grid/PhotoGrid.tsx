import React from "react";
import { useTranslation } from "react-i18next";
import { useAppSelector } from "redux/hooks";
import {
   useDeletePhotoRequestMutation,
   useGetPhotosRequestQuery,
} from "redux/service/photoService";
import { getPhotosResponse } from "redux/types/api";
import { Photo } from "../photo/Photo";
import styles from "./PhotoGrid.module.scss";

interface Props {
   data: getPhotosResponse;
   refetch: () => void;
}

export const PhotoGrid: React.FC<Props> = ({ data, refetch }) => {
   const { t } = useTranslation();

   if (data?.list.length == 0) {
      return (
         <div className={styles.no_photos}>
            <h4>{t("photographer_gallery_page.no_photos")}</h4>
         </div>
      );
   }

   return (
      <div className={styles.photo_grid}>
         {data?.list?.map((photo, i) => {
            return (
               <div key={i} className={styles.photo_grid_item}>
                  <Photo
                     photo_id={photo.id}
                     img={photo.s3Url}
                     title={photo.title}
                     date={photo.createdAt}
                     likeCount={photo.likeCount}
                     liked={photo.liked}
                     showDeleteButton={true}
                     refetch={refetch}
                     photo={photo}
                  />
               </div>
            );
         })}
      </div>
   );
};
