import React, { useState, useEffect } from "react";
``;
import styles from "./PhotographerReviewReportModal.module.scss";
import { Dropdown, IconDropdown, Modal, Notification } from "components/shared";
import { useTranslation } from "react-i18next";
import { ReviewReportCause } from "types/ReviewReportCause";
import {
   useReportPhotographerReviewMutation,
   useGetPhotographerReviewReportCausesQuery,
} from "redux/service/photographerManagementService";
import { useDispatch } from "react-redux";
import { push, ToastTypes } from "redux/slices/toastSlice";
import { ErrorResponse, Toast } from "types";
import { parseError } from "util/errorUtil";

interface Props {
   reviewId: number;
   isOpen: boolean;
   onCancel: () => void;
}

export const PhotographerReviewReportModal: React.FC<Props> = ({
   reviewId,
   isOpen,
   onCancel,
}) => {
   const { t } = useTranslation();
   const dispatch = useDispatch();

   const [cause, setCause] = useState<string>(ReviewReportCause.SPAM);

   const reportCauses = useGetPhotographerReviewReportCausesQuery();
   const [reportMutation, reportMutationState] = useReportPhotographerReviewMutation();

   const [options, setOptions] = useState({});
   const [selected, setSelected] = useState<string>();
   const [notification, setNotification] = useState<Notification>();

   useEffect(() => {
      if (reportCauses.isSuccess) {
         setOptions(
            Object.fromEntries(
               reportCauses?.data?.map((cause) => [
                  cause,
                  t(`reports_page.review.causes.${cause}`),
               ])
            )
         );
      }
   }, [reportCauses]);

   useEffect(() => {
      if (reportMutationState.isSuccess) {
         onCancel();
         const successToast: Toast = {
            type: ToastTypes.SUCCESS,
            text: t("toast.success_report"),
         };
         dispatch(push(successToast));
      }
      if (reportMutationState.isError) {
         const error = reportMutationState.error as ErrorResponse;
         setNotification({
            type: "error",
            content: t(parseError(error)),
         });
      }
   }, [reportMutationState]);

   return (
      <Modal
         title={t("photographer_page.report_review_modal_title")}
         isOpen={isOpen}
         type="confirm"
         onSubmit={async () => {
            await reportMutation({ reviewId: reviewId, cause: cause });
         }}
         onCancel={onCancel}
         className={styles.photographer_review_report_modal_wrapper}
         notification={notification}
      >
         <p className="label">Przyczyna zgłoszenia</p>
         <IconDropdown
            className={styles.icon_dropdown}
            options={options}
            value={selected}
            onChange={(key) => {
               setSelected(key);
            }}
         />
      </Modal>
   );
};
