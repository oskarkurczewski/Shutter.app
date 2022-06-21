import React, { useEffect } from "react";
import styles from "./PhotographerProfilePage.module.scss";
import {
   PhotographerDescription,
   PhotographerInfo,
   PhotographerReviewsCardWrapper,
} from "components/photographer-profile";
import { useGetPhotographerDetailedInfoQuery } from "redux/service/photographerService";
import { useLocation, useNavigate, useParams } from "react-router-dom";
import { useTranslation } from "react-i18next";
import { Button, Card } from "components/shared";
import { PhotoMasonry } from "components/photos";
import { ErrorResponse, Toast } from "types";
import { parseError } from "util/errorUtil";
import { push, ToastTypes } from "redux/slices/toastSlice";
import { useAppDispatch, useAppSelector } from "redux/hooks";
import { PhotographerCalendar } from "components/photographer-profile/photographer-calendar";

export const PhotographerProfilePage = () => {
   const { t } = useTranslation();
   const { hash } = useLocation();
   const dispatch = useAppDispatch();
   const { username } = useAppSelector((state) => state.auth);
   const navigate = useNavigate();
   const { login } = useParams();

   const { data, isError, error } = useGetPhotographerDetailedInfoQuery(login);

   if (login === undefined) {
      navigate(username);
   }

   if (isError) {
      const err = parseError(error as ErrorResponse);

      if (err.includes("exception.photographer_not_found")) {
         return <h3>{t("photographer_page.not_found")}</h3>;
      } else {
         const successToast: Toast = {
            type: ToastTypes.ERROR,
            text: t(err),
         };
         dispatch(push(successToast));
      }

      return;
   }

   useEffect(() => {
      if (hash) {
         const element = document.getElementById(hash.replace("#", ""));

         element &&
            setTimeout(() => {
               element.scrollIntoView();
            }, 600);
      }
   }, [hash]);

   return (
      <section className={styles.photographer_info_page_wrapper}>
         <div className={styles.header}>
            <p className="category-title">{t("photographer_page.title")}</p>
            <div>
               {login === username && (
                  <>
                     <Button onClick={() => navigate("/settings")}>
                        {t("photographer_page.button.settings")}
                     </Button>
                     <Button onClick={() => navigate("/profile/change-availability")}>
                        {t("photographer_page.button.availability")}
                     </Button>
                     <Button onClick={() => navigate("/profile/gallery")}>
                        {t("photographer_page.button.gallery")}
                     </Button>
                  </>
               )}
            </div>
         </div>
         <div className={styles.photographer_info_container}>
            {data && (
               <div className={styles.content}>
                  <div className={styles.row}>
                     <PhotographerInfo
                        name={data?.name}
                        surname={data?.surname}
                        email={data?.email}
                        stars={data?.score}
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
                        <div
                           className={styles.photographer_callendar_container}
                           id="calendar"
                        >
                           <PhotographerCalendar
                              photographer={{
                                 login: data?.login,
                                 name: data?.name,
                                 surname: data?.surname,
                                 email: data?.email,
                              }}
                           />
                        </div>
                     </div>
                  </div>
                  <div className={styles.photographer_gallery_container}>
                     <Card className={styles.photographer_gallery_card}>
                        <p className={styles.gallery_title}>
                           {t("photographer_page.gallery_title")}
                        </p>
                        <PhotoMasonry login={login} />
                     </Card>
                  </div>
               </div>
            )}
         </div>
      </section>
   );
};
