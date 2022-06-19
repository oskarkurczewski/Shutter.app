import React, { useState } from "react";
import { useTranslation } from "react-i18next";
import { useGetPhotosRequestQuery } from "redux/service/photoService";
import { Photo } from "../photo/Photo";
import styles from "./PhotoMasonry.module.scss";
import PhotoModal from "components/photos/photo-modal/PhotoModal";
interface Props {
   login: string;
}

export const PhotoMasonry: React.FC<Props> = ({ login }) => {
   const { t } = useTranslation();
   const { data } = useGetPhotosRequestQuery({
      photographerLogin: login,
   });
   const [photoModalIsOpen, setPhotoModalIsOpen] = useState<boolean>(false);
   const [photoData, setPhotoData] = useState(null);
   const closePhotoModal = () => {
      setPhotoData(null);
      setPhotoModalIsOpen(false);
   };
   const openPhotoModal = (photo) => {
      setPhotoData(photo);
      setPhotoModalIsOpen(true);
   };

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
                        onClick={() => openPhotoModal(photo)}
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
            <h4 className={styles.no_photos}>
               {t("photographer_page.gallery_no_photos")}
            </h4>
         )}
         {photoData && (
            <PhotoModal
               photo={photoData}
               isOpen={photoModalIsOpen}
               onSubmit={closePhotoModal}
            />
         )}
      </div>
   );
};
