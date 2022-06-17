import { Photo } from "components/photo";
import React from "react";
import { useTranslation } from "react-i18next";
import styles from "./photoMasonry.module.scss";

interface Props {
   login: string;
}

export const PhotoMasonry: React.FC<Props> = ({ login }) => {
   // const { data, isError } = jakiestamquery()
   const photoList: string[] = [
      "https://images.unsplash.com/photo-1655354782962-f99b5b32d617?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1169&q=80",
      "https://images.unsplash.com/photo-1655411439249-6a72181b4bb2?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=735&q=80",
      "https://images.unsplash.com/photo-1655395340958-1c2a24c9742a?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=732&q=80",
      "https://images.unsplash.com/photo-1655401167348-69bc3a0bce34?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=687&q=80",
      "https://images.unsplash.com/photo-1655339998027-fed1e01b783a?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1170&q=80",
      "https://images.unsplash.com/photo-1655365225178-b1b4c59cbdb2?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1170&q=80",
   ];

   return (
      <div className={styles.photo_masonry}>
         {photoList.map((photo, i) => {
            return (
               <div key={i} className={styles.photo_masonry_item}>
                  <Photo
                     img={photo}
                     title="Lorem ipsum"
                     description="Very nice picture"
                     date="01.01.2022"
                     likeCount={321}
                  ></Photo>
               </div>
            );
         })}
      </div>
   );
};
