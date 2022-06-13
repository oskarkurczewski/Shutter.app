import { Modal } from "components/shared";
import React from "react";
import { useTranslation } from "react-i18next";
import { useAppSelector } from "redux/hooks";
import { useGetAdvancedUserInfoQuery } from "redux/service/usersManagementService";
import { AccessLevel } from "types";
import { ChangeAccessLevels } from "./change-access-levels";
import { ChangeBaseInfo } from "./change-base-info";
import { ChangePassword } from "./change-password";

import styles from "./EditAccountModal.module.scss";

interface Props {
   login: string;
   isOpen: boolean;
   onSubmit: () => void;
}

const EditAccountModal: React.FC<Props> = ({ login, isOpen, onSubmit }) => {
   const { t } = useTranslation();

   const { data: userInfoData } = useGetAdvancedUserInfoQuery(login);
   const { accessLevel } = useAppSelector((state) => state.auth);

   return (
      <Modal
         type="info"
         isOpen={isOpen}
         onSubmit={onSubmit}
         title={t("edit_account_page.title")}
         submitText={t("edit_account_page.close")}
      >
         <div
            className={`${styles.edit_account_modal_wrapper} ${
               accessLevel === AccessLevel.MODERATOR && styles.moderator
            }`}
         >
            <ChangeBaseInfo userInfoData={userInfoData} />
            {accessLevel === AccessLevel.ADMINISTRATOR && (
               <>
                  <div className={`${styles.border} ${styles.border_a}`} />
                  <ChangeAccessLevels userInfoData={userInfoData} />
                  <div className={`${styles.border} ${styles.border_b}`} />
                  <ChangePassword
                     login={login}
                     isRegistered={userInfoData?.data.registered}
                  />
               </>
            )}
            <div className={`${styles.border} ${styles.border_c}`} />
         </div>
      </Modal>
   );
};

export default EditAccountModal;
