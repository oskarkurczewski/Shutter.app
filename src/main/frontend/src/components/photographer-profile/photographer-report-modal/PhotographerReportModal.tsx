import React, { useEffect, useState } from "react";
import styles from "./PhotographerReportModal.module.scss";
import { IconDropdown, Modal, Notification } from "components/shared";
import { useTranslation } from "react-i18next";
import { useParams } from "react-router-dom";
import { push, ToastTypes } from "redux/slices/toastSlice";
import { useAppDispatch } from "redux/hooks";
import { ErrorResponse, Toast } from "types";
import { parseError } from "util/errorUtil";
import {
   useGetPhotographerReportCausesQuery,
   useReportPhotographerMutation,
} from "redux/service/photographerManagementService";

interface Props {
   isOpen: boolean;
   onCancel: () => void;
   photographerLogin?: string;
}

export const PhotographerReportModal: React.FC<Props> = ({
   isOpen,
   onCancel,
   photographerLogin,
}) => {
   const { t } = useTranslation();
   const dispatch = useAppDispatch();

   const { login } = useParams();

   const reportCauses = useGetPhotographerReportCausesQuery();
   const [reportMutation, reportMutationState] = useReportPhotographerMutation();

   const [options, setOptions] = useState({});
   const [selected, setSelected] = useState<string>();
   const [notification, setNotification] = useState<Notification>();

   useEffect(() => {
      if (reportCauses.isSuccess) {
         setOptions(
            Object.fromEntries(
               reportCauses?.data?.map((cause) => [
                  cause,
                  t(`reports_page.photographer.causes.${cause}`),
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
         title={t("photographer_page.report.photographer_title")}
         isOpen={isOpen}
         type="confirm"
         onSubmit={async () => {
            await reportMutation({
               photographerLogin: photographerLogin ? photographerLogin : login,
               cause: selected,
            });
         }}
         onCancel={onCancel}
         className={styles.photographer_review_report_modal_wrapper}
         notification={notification}
      >
         <p className="label">{t("photographer_report_modal.report_reason")}</p>
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
