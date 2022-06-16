import { Button, Card, Modal, TextArea, TextInput } from "components/shared";
import { FileInput } from "components/shared/file-input";
import React, { useEffect, useState } from "react";
import { useTranslation } from "react-i18next";
import { usePostPhotoRequestMutation } from "redux/service/photoService";
import styles from './photographerGalleryPage.module.scss';

export const PhotographerGalleryPage = () => {

  const { t } = useTranslation();

  const [postPhotoMutation, postPhotoMutationState] = usePostPhotoRequestMutation();

  const [uploadFormVisible, setUploadFormVisibility] = useState<boolean>(false);
  const [formData, setFormData] = useState<{title: string, desc: string, file: File}>({
    title: "",
    desc: "",
    file: null,
  });

  const closeModal = () => {
    postPhotoMutationState.reset();
    setUploadFormVisibility(false);
    setFormData({
      title: "",
      desc: "",
      file: null,
    });
  }
  useEffect(() => {
    if (postPhotoMutationState.isSuccess) closeModal();
  }, [postPhotoMutationState])


  const uploadFile = () => {
    const pngB64Header = "iVBORw0KGgoA";
    const fileReader = new FileReader();

    fileReader.onloadend = () => {
      const encoded = btoa(fileReader.result.toString());
      if (encoded.slice(0, 12) !== pngB64Header) return;
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
          onCancel={closeModal}
          title={t("photographer_gallery_page.add_photo")}
        >
          <div className={styles.upload_form}>
            <FileInput
              onFileChange={(e) => setFormData({ ...formData, file: e.target.files[0] })}
              file={formData.file}
            />
            {((formData.file === null || formData.file.name.slice(-4) !== ".png") &&
              <p>{t("photographer_gallery_page.photo_data_empty")}</p>)}

            <TextInput
              value={formData.title}
              label={t("photographer_gallery_page.photo_title")}
              onChange={(e) => setFormData({ ...formData, title: e.target.value })}
            />
            {(formData.title.length === 0 && 
              <p>{t("photographer_gallery_page.photo_title_empty")}</p>)}

            <TextArea
              className={styles.desc_field}
              value={formData.desc}
              label={t("photographer_gallery_page.photo_description")}
              onChange={(e) => setFormData({ ...formData, desc: e.target.value })}
            />
            {(postPhotoMutationState.isLoading && 
              <p>{t("photographer_gallery_page.uploading_photo")}</p>)}
            {(postPhotoMutationState.isError && 
              <p>{t("exception.photo_upload_error")}</p>)}

          </div>
        </Modal>
      </Card>
    </div>
  )
}