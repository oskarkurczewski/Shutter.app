import React, { useState } from "react";
import styles from "./PhotographerReview.module.scss";
import { PhotographerStars } from "../photographer-stars";
import { PhotographerReviewReportModal } from "../photographer-report-modal";
import { MenuDropdown } from "components/shared/dropdown/menu-dropdown";
import { MenuDropdownItem } from "components/shared/dropdown/menu-dropdown/menu-dropdown-item";
import { useTranslation } from "react-i18next";

interface Props {
   id?: number;
   name?: string;
   surname?: string;
   stars?: number;
   description?: string;
}

export const PhotographerReview: React.FC<Props> = ({
   id,
   name,
   surname,
   stars,
   description,
}) => {
   const [editReportModalIsOpen, setEditReportModalIsOpen] = useState<boolean>(false);
   const { t } = useTranslation();

   const reportReview = () => {
      setEditReportModalIsOpen(true);
   };

   const deleteReview = () => {
      //TODO: delete review
   };

   return (
      <div className={styles.review_wrapper}>
         <img
            src="/images/avatar.png"
            alt="reviewer_photo"
            className={styles.review_photo}
         />
         <div className={styles.review_info}>
            <p>
               {name} {surname}
            </p>
            <PhotographerStars stars={stars} />
         </div>
         <p className={styles.review_description}>{description}</p>
         <div className={styles.review_buttons}>
            <MenuDropdown>
               <MenuDropdownItem
                  value={t("photographer_page.report_button")}
                  onClick={reportReview}
               />
               <MenuDropdownItem
                  value={t("photographer_page.delete_button")}
                  onClick={deleteReview}
               />
            </MenuDropdown>
            {/* TODO: like button */}
         </div>
         <PhotographerReviewReportModal
            reviewId={id}
            isOpen={editReportModalIsOpen}
            onSubmit={() => setEditReportModalIsOpen(false)}
            onCancel={() => setEditReportModalIsOpen(false)}
         />
      </div>
   );
};
