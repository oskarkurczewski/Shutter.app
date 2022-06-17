import React, { useEffect, useState } from "react";
import styles from "./PhotographerProfilePage.module.scss";
import {
   PhotographerDescription,
   PhotographerInfo,
   PhotographerReviewsCardWrapper,
} from "components/photographer-profile";
import { useGetPhotographerDetailedInfoQuery } from "redux/service/photographerService";
import { useParams } from "react-router-dom";
import { useTranslation } from "react-i18next";

export const PhotographerProfilePage = () => {
   const { t } = useTranslation();
   const { login } = useParams();
   const { data, isError } = useGetPhotographerDetailedInfoQuery(login);

   return (
      <section className={styles.photographer_info_page_wrapper}>
         <p className="category-title">{t("photographer_page.title")}</p>
         {isError && <p>EXCEPTION</p>}
         {data && (
            <div className={styles.content}>
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
         )}
      </section>
   );
};
