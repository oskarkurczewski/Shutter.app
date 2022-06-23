import React from "react";
import { Modal } from "components/shared";
import { useAppDispatch } from "redux/hooks";
import { logout } from "redux/slices/authSlice";
import { useTranslation } from "react-i18next";
import { push, ToastTypes } from "redux/slices/toastSlice";
import { Toast } from "types";
import { useNavigate } from "react-router-dom";

interface Props {
   isOpen: boolean;
   setIsOpen: (value: boolean) => void;
}

export const LogoutModal: React.FC<Props> = ({ isOpen, setIsOpen }) => {
   const { t } = useTranslation();
   const dispatch = useAppDispatch();
   const navigate = useNavigate();

   return (
      <Modal
         title={t("logout_modal.title")}
         type="confirm"
         isOpen={isOpen}
         onCancel={() => setIsOpen(false)}
         onSubmit={() => {
            dispatch(logout());

            const successToast: Toast = {
               type: ToastTypes.SUCCESS,
               text: t("logout_modal.successfully_logged_out"),
            };
            dispatch(push(successToast));

            setIsOpen(false);
            navigate("/");
         }}
         submitText={t("logout_modal.title")}
      >
         <p>{t("logout_modal.description")}</p>
      </Modal>
   );
};
