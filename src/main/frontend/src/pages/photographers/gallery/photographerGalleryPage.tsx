import { Button, Card, Modal, TextArea, TextInput } from "components/shared";
import { FileInput } from "components/shared/file-input";
import React, { useEffect, useState } from "react";
import { useTranslation } from "react-i18next";
import { usePostPhotoRequestMutation } from "redux/service/photoService";
import styles from './photographerGalleryPage.module.scss';

export const PhotographerGalleryPage = () => {

  const { t } = useTranslation();

  const [uploadFormVisible, setUploadFormVisibility] = useState<boolean>(false);
  const [formData, setFormData] = useState<{title: string, desc: string, file: File}>({
    title: "",
    desc: "",
    file: null,
  });

  const [postPhotoMutation, postPhotoMutationState] = usePostPhotoRequestMutation();
  useEffect(() => {
    if (postPhotoMutationState.isSuccess) {
      setUploadFormVisibility(false);
    }
  }, [postPhotoMutationState])

  const uploadFile = () => {
    const fileReader = new FileReader();
    fileReader.onloadend = () => {
      const encoded = btoa(fileReader.result.toString());
      postPhotoMutation({
        title: formData.title,
        description: formData.desc,
        data: encoded
      })
    }
    fileReader.readAsBinaryString(formData.file);
  }

  return (
    <div className={styles.gallery_page_wrapper}>
      <Card className={styles.table_card}>
        <Button onClick={ () => setUploadFormVisibility(true) }>
          {t("photographer_gallery_page.add_photo")}
        </Button>
        <Modal
          type={"confirm"}
          isOpen={uploadFormVisible}
          onSubmit={uploadFile}
          onCancel={() => setUploadFormVisibility(false)}
          title={t("photographer_gallery_page.add_photo")}
        >
          <div className={styles.upload_form}>
            <FileInput
              onFileChange={(e) => setFormData({ ...formData, file: e.target.files[0] })}
              file={formData.file}
            />

            <TextInput
              value={formData.title}
              label={t("photographer_gallery_page.photo_title")}
              onChange={(e) => setFormData({ ...formData, title: e.target.value })}
            />

            <TextArea
              className={styles.desc_field}
              value={formData.desc}
              label={t("photographer_gallery_page.photo_description")}
              onChange={(e) => setFormData({ ...formData, desc: e.target.value })}
            />
            
            {postPhotoMutationState.isError && (
              <p>{t("exception.photo_upload_error")}</p>
            )}
          </div>
        </Modal>
      </Card>
    </div>
  )
}