import { PhotographerAddImageModal } from "components/photographer-profile/photographer-add-image-modal";
import { PhotoGrid } from "components/photos";
import { Card, DragAndDrop } from "components/shared";
import React, { useState } from "react";
import { useTranslation } from "react-i18next";
import { FaPlusCircle } from "react-icons/fa";
import styles from "./photographerGalleryPage.module.scss";

export const PhotographerGalleryPage = () => {
   const { t } = useTranslation();
   const [modalOpen, setModalOpen] = useState<boolean>(false);
   const [file, setFile] = useState<File>(null);

   return (
      <div className={styles.gallery_page_wrapper}>
         <Card className={styles.table_card}>
            <DragAndDrop
               icon={<FaPlusCircle />}
               label={t("photographer_gallery_page.add_photo")}
               onCapture={(uploadedFile) => {
                  setFile(uploadedFile);
                  setModalOpen(true);
               }}
            />
            <PhotographerAddImageModal
               file={file}
               isOpen={modalOpen}
               onCancel={() => setModalOpen(false)}
               onSubmit={() => setModalOpen(false)}
            />
            <PhotoGrid />
         </Card>
      </div>
   );
};
