import React, { SetStateAction, useState } from "react";
import styles from "./PhotographerReviewReportModal.module.scss";
import {
   Button,
   Card,
   Dropdown,
   Modal,
   SquareButton,
   TextInput,
} from "components/shared";
import { useTranslation } from "react-i18next";
import { ReviewReportCause } from "types/ReviewReportCause";
import { useReportPhotographerReviewMutation } from "redux/service/photographerManagementService";

interface Props {
   reviewId: number;
   isOpen: boolean;
   onSubmit: () => void;
   onCancel: () => void;
}

export const PhotographerReviewReportModal: React.FC<Props> = ({
   reviewId,
   isOpen,
   onSubmit,
   onCancel,
}) => {
   const [cause, setCause] = useState<ReviewReportCause>(
      ReviewReportCause.OBSCENE_CONTENT
   );
   const [reportReview, { data: data }] = useReportPhotographerReviewMutation();
   const { t } = useTranslation();

   return (
      <Modal
         title={t("photographer_page.report_review")}
         isOpen={isOpen}
         type="confirm"
         onSubmit={async () => {
            console.log(reviewId);
            console.log({ reviewId: reviewId, cause: cause });
            await reportReview({ reviewId: reviewId, cause: cause });
            onSubmit();
         }}
         onCancel={onCancel}
      >
         <Dropdown
            values={[
               ReviewReportCause.OBSCENE_CONTENT,
               ReviewReportCause.SPAM,
               ReviewReportCause.FAKE_REVIEW,
            ]}
            selectedValue={cause}
            name="selectCause"
            id="selectCause"
            onChange={(e) => setCause(ReviewReportCause[e.target.value])}
         >
            {t("photographer_page.report_review_dropdown")}
         </Dropdown>
      </Modal>
   );
};
