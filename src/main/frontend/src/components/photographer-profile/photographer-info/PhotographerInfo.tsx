import React from "react";
import styles from "./PhotographerInfo.module.scss";
import { Card, IconText } from "components/shared";
import { IoLocationSharp } from "react-icons/io5";
import { IoMdImage } from "react-icons/io";
import { AiFillLike } from "react-icons/ai";
import { RiFileList2Fill } from "react-icons/ri";
import { MdFactCheck } from "react-icons/md";
import { PhotographerStars } from "../photographer-stars";

interface Props {
   name?: string;
   surname?: string;
   location?: string;
   stars?: number;
   sessionCount?: number;
   photosCount?: number;
   reviewCount?: number;
}

export const PhotographerInfo: React.FC<Props> = ({
   name,
   surname,
   location,
   stars,
   sessionCount,
   photosCount,
   reviewCount,
}) => {
   return (
      <div className={styles.photographer_profile_wrapper}>
         <Card className={styles.data_wrapper}>
            <img
               src="/images/avatar.png"
               alt="user"
               className={styles.photographer_photo}
            />

            <div className={styles.label_wrapper}>
               <p className="section-title">{name + " " + surname}</p>
               <IconText text={location} Icon={IoLocationSharp} className="label-bold" />
               <PhotographerStars stars={stars} className="label-bold" />
               <IconText
                  text={`${sessionCount} ${"photographer_info_page.photographer_session_count"}`}
                  Icon={MdFactCheck}
                  className="label-bold"
               />
               <IconText
                  text={`${photosCount} ${"photographer_info_page.photographer_photos_count"}`}
                  Icon={IoMdImage}
                  className="label-bold"
               />
               <IconText
                  text={`${reviewCount} ${"photographer_info_page.photographer_review_count"}`}
                  Icon={RiFileList2Fill}
                  className="label-bold"
               />
            </div>
         </Card>
      </div>
   );
};
