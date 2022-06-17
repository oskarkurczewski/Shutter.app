import React, { useState } from "react";
import { Modal, TextArea } from "components/shared";
import { useTranslation } from "react-i18next";
import { Stars } from "components/shared/stars";
import styles from "./AddReviewModal.module.scss";

interface Props {
   isOpen: boolean;
   onSubmit: () => void;
   onCancel: () => void;
}

const AddReviewModal: React.FC<Props> = ({ isOpen, onSubmit, onCancel }) => {
   const { t } = useTranslation();
   const [stars, setStars] = useState<number>(0);
   const [desc, setDesc] = useState<string>("");

   return (
      <Modal
         type="confirm"
         isOpen={isOpen}
         onSubmit={() => {
            
            onSubmit();
         }}
         onCancel={onCancel}
         title={t("photographer_page.add_review")}
         submitText={t("photographer_page.add_review")}
      >
         <div className={styles.review_form}>
            <p>{t("photographer_page.your_opinion")}</p>
            <Stars stars={stars} className={styles.review_stars} setStars={setStars} />
            <p>{t("photographer_page.opinion_content")}</p>
            <TextArea
               className={styles.desc_field}
               value={desc}
               onChange={(e) => setDesc(e.target.value)}
            />
         </div>
      </Modal>
   );
};

export default AddReviewModal;
