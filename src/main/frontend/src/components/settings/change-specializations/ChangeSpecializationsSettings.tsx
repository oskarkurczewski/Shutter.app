import { Button, Card, IconText, MultiSelectDropdown } from "components/shared";
import React, { useEffect, useState } from "react";
import { useTranslation } from "react-i18next";
import { FaPlusCircle } from "react-icons/fa";
import { useAppDispatch } from "redux/hooks";
import {
   useEditOwnSpecializationsMutation,
   useGetAvailableSpecializationsQuery,
   useGetOwnSpecializationsQuery,
} from "redux/service/photographerManagementService";
import { push, ToastTypes } from "redux/slices/toastSlice";
import { Toast } from "types";
import { getSpecializationProps } from "util/photographerUtil";
import styles from "./ChangeSpecializationsSettings.module.scss";

export const ChangeSpecializationsSettings: React.FC = () => {
   const { t } = useTranslation();
   const dispatch = useAppDispatch();

   const availableSpecializations = useGetAvailableSpecializationsQuery();

   const ownSpecializations = useGetOwnSpecializationsQuery();

   const [editSpecializationsMutation, editSpecializationsMutationState] =
      useEditOwnSpecializationsMutation();

   const [selectedSpecializations, setSelectedSpecializations] = useState<string[]>([]);

   const [options, setOptions] = useState({});

   useEffect(() => {
      if (editSpecializationsMutationState.isSuccess) {
         const successToast: Toast = {
            type: ToastTypes.SUCCESS,
            text: t("toast.success_update"),
         };
         dispatch(push(successToast));
      }
      if (editSpecializationsMutationState.isError) {
         const errorToast: Toast = {
            type: ToastTypes.ERROR,
            text: t("exception.error_update"),
         };
         dispatch(push(errorToast));
      }
   }, [editSpecializationsMutationState]);

   useEffect(() => {
      if (ownSpecializations.isSuccess) {
         setSelectedSpecializations(ownSpecializations?.data);
      }
   }, [ownSpecializations]);

   useEffect(() => {
      if (availableSpecializations.isSuccess) {
         setOptions(
            Object.fromEntries(
               availableSpecializations?.data?.map((s) => [
                  s,
                  t(`photographer_specialization.${s.toLowerCase()}`),
               ])
            )
         );
      }
   }, [availableSpecializations]);

   const generateSpecializationElements = () => {
      if (selectedSpecializations.length === 0) {
         return <FaPlusCircle />;
      }
      return selectedSpecializations.map((specialization) => {
         const specProps = getSpecializationProps(specialization);
         return (
            <div
               role="button"
               key={specialization}
               onClick={() => {
                  setSelectedSpecializations(
                     selectedSpecializations.filter((s) => s !== specialization)
                  );
               }}
               tabIndex={-1}
               onKeyDown={null}
               className={styles.specialization_wrapper}
            >
               <IconText
                  key={specialization}
                  className={styles.specialization}
                  text={`${options[specialization]} ✕`}
                  color={specProps.color}
               />
            </div>
         );
      });
   };

   return (
      <Card id="change-specializations" className={styles.change_specializations_wrapper}>
         <p className={`category-title ${styles.title}`}>Zmień specializacje</p>
         <p>{t("settings_page.change_specializations.description")}</p>
         <div className={styles.specializations}>{generateSpecializationElements()}</div>
         <div className={styles.footer}>
            <Button
               onClick={() => {
                  editSpecializationsMutation(selectedSpecializations);
               }}
               loading={editSpecializationsMutationState.isLoading}
            >
               {t("settings_page.change_specializations.confirm")}
            </Button>
            <MultiSelectDropdown
               options={options}
               onChange={setSelectedSpecializations}
               selected={selectedSpecializations}
               label={t("settings_page.change_specializations.select")}
            />
         </div>
      </Card>
   );
};
