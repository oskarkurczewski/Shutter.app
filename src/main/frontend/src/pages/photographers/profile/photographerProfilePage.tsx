import React from "react";
import styles from "./PhotographerProfilePage.module.scss";
import {
   PhotographerDescription,
   PhotographerInfo,
   PhotographerReviewsCardWrapper,
} from "components/photographer-profile";
import { useGetPhotographerDetailedInfoQuery } from "redux/service/photographerService";
import { useParams } from "react-router-dom";
import { useTranslation } from "react-i18next";
import { Card } from "components/shared";
import { PhotoMasonry } from "components/photos";
import { PhotographerCalendar } from "components/photographer-profile/photographer-calendar";

export const PhotographerProfilePage = () => {
   const { t } = useTranslation();
   const { login } = useParams();
   const { data, isError } = useGetPhotographerDetailedInfoQuery(login);

   const photographer = {
      login: data?.login,
      name: data?.name,
      surname: data?.surname,
      email: data?.email,
   };

   return (
      <section className={styles.photographer_info_page_wrapper}>
         <p className="category-title">{t("photographer_page.title")}</p>
         {isError && <p>EXCEPTION</p>}
         {data && (
            <section>
               <div className={styles.row}>
                  <PhotographerInfo
                     name={data?.name}
                     surname={data?.surname}
                     location="Lodz"
                     stars={data?.score}
                     sessionCount={30}
                     photosCount={546}
                     reviewCount={data?.reviewCount}
                  />

                  <div className={styles.column}>
                     <PhotographerDescription
                        specializationList={data?.specializationList}
                        description={data?.description}
                     />

                     <PhotographerReviewsCardWrapper
                        reviewCount={data.reviewCount}
                        photographerLogin={login}
                     />

                  </div>

               </div>
               <div className={styles.photographer_callendar_container}>
                  <PhotographerCalendar photographer={photographer} />
               </div>
               <div className={styles.photographer_gallery_container}>
                  <Card className={styles.photographer_gallery_card}>
                     <p className={styles.gallery_title}>
                        {t("photographer_page.gallery_title")}
                     </p>
                     <PhotoMasonry login={login} />
                  </Card>
               </div>
            </section>
         )}
      </section>
   );
};
