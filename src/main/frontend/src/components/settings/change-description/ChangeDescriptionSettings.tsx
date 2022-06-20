import React, { useEffect, useState } from "react";
import styles from "./ChangeDescriptionSettings.module.scss";
import { Button, Card, TextArea } from "components/shared";
import { useTranslation } from "react-i18next";
import { useSendChangeDescriptionLinkMutation } from "redux/service/userSettingsService";
import { useDispatch } from "react-redux";
import { push, ToastTypes } from "redux/slices/toastSlice";
import { parseError } from "util/errorUtil";
import { ErrorResponse, Toast } from "types";

export const ChangeDescriptionSettings = () => {
   const { t } = useTranslation();
   const dispatch = useDispatch();

   const [mutation, mutationState] = useSendChangeDescriptionLinkMutation();
   const [newDescription, setNewDescription] = useState("");

   useEffect(() => {
      if (mutationState.isSuccess) {
         const successToast: Toast = {
            type: ToastTypes.SUCCESS,
            text: t("toast.success_change_description_message"),
         };
         dispatch(push(successToast));
      }
      if (mutationState.isError) {
         const err = mutationState.error as ErrorResponse;
         const errorToast: Toast = {
            type: ToastTypes.ERROR,
            text: t(parseError(err)),
         };
         dispatch(push(errorToast));
      }
   }, [mutationState]);

   return (
      <Card id="change-description" className={styles.card_wrapper}>
         <p className={`category-title ${styles.category_title}`}>
            {t("settings_page.change_description.title")}
         </p>
         <p>{t("settings_page.change_description.details_message")}</p>

         <TextArea
            className={styles.textarea}
            label={t("settings_page.change_description.label_message")}
            placeholder={t("settings_page.change_description.placeholder_message")}
            required
            value={newDescription}
            onChange={(e) => setNewDescription(e.target.value)}
            rows={8}
            maxLength={4096}
         />

         <Button
            loading={mutationState.isLoading}
            onClick={() => {
               mutation({ content: newDescription });
            }}
         >
            {t("settings_page.change_description.confirm")}
         </Button>
      </Card>
   );
};
