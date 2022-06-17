import { Modal, Notification, TextArea, TextInput } from "components/shared";
import { FileInput } from "components/shared/file-input";
import React, { useEffect, useState } from "react";
import { useTranslation } from "react-i18next";
import { FaFileImage } from "react-icons/fa";
import { usePostPhotoRequestMutation } from "redux/service/photoService";
import { ErrorResponse } from "types";
import { parseError } from "util/errorUtil";
import styles from "./PhotographerAddImageModal.module.scss";

interface Props {
   file: File;
   isOpen: boolean;
   onSubmit: () => void;
   onCancel: () => void;
}

export const PhotographerAddImageModal: React.FC<Props> = ({
   file,
   isOpen,
   onSubmit,
   onCancel,
}) => {
   const { t } = useTranslation();
   const [postPhotoMutation, postPhotoMutationState] = usePostPhotoRequestMutation();

   const [notification, setNotification] = useState<Notification>(null);
   const [formData, setFormData] = useState<{ title: string; desc: string; file: File }>({
      title: "",
      desc: "",
      file: null,
   });

   useEffect(() => {
      setFormData({ ...formData, file });
   }, [file]);

   const uploadFile = () => {
      const pngB64Header = "iVBORw0KGgoA";
      const fileReader = new FileReader();

      fileReader.onloadend = () => {
         const encoded = btoa(fileReader.result.toString());

         if (encoded.slice(0, 12) !== pngB64Header) {
            setNotification({
               type: "error",
               content: t(`photographer_gallery_page.error.format`),
            });
            return;
         }
         postPhotoMutation({
            title: formData.title,
            description: formData.desc,
            data: encoded,
         });
      };

      if (!formData.file) {
         setNotification({
            type: "error",
            content: t(`photographer_gallery_page.error.file_required`),
         });

         return;
      }

      fileReader.readAsBinaryString(formData.file);
   };

   useEffect(() => {
      if (postPhotoMutationState.isSuccess) {
         onSubmit();
      }
      if (postPhotoMutationState.isError) {
         const msg = postPhotoMutationState.error as ErrorResponse;

         setNotification({
            type: "error",
            content: t(`photographer_gallery_page.${parseError(msg.data.message)}`),
         });
      }
   }, [postPhotoMutationState]);

   return (
      <Modal
         type="confirm"
         isOpen={isOpen}
         onSubmit={uploadFile}
         onCancel={onCancel}
         title={t("photographer_gallery_page.add_photo")}
         notification={notification}
      >
         <div className={styles.upload_form}>
            <div className={styles.content}>
               <FileInput
                  label={t("photographer_gallery_page.photo_file")}
                  icon={<FaFileImage />}
                  required={true}
                  file={formData.file}
                  className={styles.file_input}
                  onFileChange={(e) =>
                     setFormData({ ...formData, file: e.target.files[0] })
                  }
               />

               <TextInput
                  value={formData.title}
                  required={true}
                  label={t("photographer_gallery_page.photo_title")}
                  onChange={(e) => setFormData({ ...formData, title: e.target.value })}
               />

               <TextArea
                  className={styles.desc_field}
                  value={formData.desc}
                  label={t("photographer_gallery_page.photo_description")}
                  onChange={(e) => setFormData({ ...formData, desc: e.target.value })}
               />
            </div>
         </div>
      </Modal>
   );
};
