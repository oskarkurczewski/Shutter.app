import React, { FC, useState } from "react";
import { IconDropdown, Modal } from "components/shared";
import { useTranslation } from "react-i18next";
import { useAppDispatch } from "redux/hooks";
import { useReportUserMutation } from "redux/service/usersManagementService";
import { push, ToastTypes } from "redux/slices/toastSlice";
import { UserReportCause } from "redux/types/api/reportTypes";
import styles from "./ReportModal.module.scss";

interface Props {
   isOpen: boolean;
   onCancel: () => void;
   onSubmit: () => void;
   login: string;
}

export const ReportModal: FC<Props> = ({ isOpen, onCancel, onSubmit, login }) => {
   const { t } = useTranslation();
   const dispatch = useAppDispatch();
   const [report] = useReportUserMutation();

   const [selectedCause, setSelectedCause] = useState(UserReportCause.PAY_ON_TIME);

   const causes = {
      [UserReportCause.PAY_ON_TIME]: t("photographer_jobs_page.report_modal.pay_on_time"),
      [UserReportCause.SHOW_UP]: t("photographer_jobs_page.report_modal.show_up"),
   };

   return (
      <Modal
         className={styles.modal}
         title={t("photographer_jobs_page.report_modal.title")}
         type="confirm"
         isOpen={isOpen}
         onCancel={onCancel}
         onSubmit={() => {
            report({
               reportedLogin: login,
               cause: selectedCause,
            })
               .unwrap()
               .then(
                  () => {
                     dispatch(
                        push({
                           type: ToastTypes.SUCCESS,
                           text: t("photographer_jobs_page.report_modal.success_toast"),
                        })
                     );
                  },
                  (err) => {
                     dispatch(
                        push({
                           type: ToastTypes.ERROR,
                           text: t([err.data?.message || ".", "exception.unexpected"]),
                        })
                     );
                  }
               );

            onSubmit();
         }}
         submitText={t("photographer_jobs_page.report_modal.report")}
      >
         <div className={styles.wrapper}>
            <p className={`label ${styles.text}`}>
               {t("photographer_jobs_page.report_modal.report_message")}:{" "}
            </p>
            <IconDropdown
               options={causes}
               value={selectedCause}
               onChange={(key) => setSelectedCause(key as UserReportCause)}
               className={styles.dropdown}
            />
         </div>
      </Modal>
   );
};
