import { PhotographerAddImageModal } from "components/photographer-profile/photographer-add-image-modal";
import { Button, Card } from "components/shared";
import React, { useState } from "react";
import { useTranslation } from "react-i18next";
import styles from "./photographerGalleryPage.module.scss";

export const PhotographerGalleryPage = () => {
   const { t } = useTranslation();
   const [modalOpen, setModalOpen] = useState<boolean>(false);

   return (
      <div className={styles.gallery_page_wrapper}>
         <Card className={styles.table_card}>
            <Button onClick={() => setModalOpen(true)}>
               {t("photographer_gallery_page.add_photo")}
            </Button>
            <PhotographerAddImageModal
               isOpen={modalOpen}
               onCancel={() => setModalOpen(false)}
               onSubmit={() => setModalOpen(false)}
            />
         </Card>
      </div>
   );
};
