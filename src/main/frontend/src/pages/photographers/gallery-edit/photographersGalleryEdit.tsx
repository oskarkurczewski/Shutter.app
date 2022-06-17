import { PhotoGrid } from "components/photo-grid/photoGrid";
import React from "react";
import { useTranslation } from "react-i18next";
import { useParams } from "react-router-dom";
import styles from "./photographerGalleryEdit.module.scss";

export const PhotographerGalleryEdit = () => {
   const { t } = useTranslation();
   const { login } = useParams();
   // const { data, isError } = jakiestamquery()

   return (
      <section className={styles.photographer_gallery_edit_page_wrapper}>
         <p className={styles.title}>{t("photographer_gallery_page.title")}</p>
         <PhotoGrid login={login} />
      </section>
   );
};
