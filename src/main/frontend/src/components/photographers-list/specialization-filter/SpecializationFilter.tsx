import { Card, Checkbox } from "components/shared";
import React, { FC, useState, useEffect } from "react";
import { useTranslation } from "react-i18next";
import { useGetAvailableSpecializationsQuery } from "redux/service/photographerManagementService";

interface Props {
   className: string;

   selectedSpecialization: string;
   setSelectedSpecialization: React.Dispatch<React.SetStateAction<string>>;
}

export const SpecializationFilter: FC<Props> = ({
   className,

   selectedSpecialization,
   setSelectedSpecialization,
}) => {
   const { t } = useTranslation();
   const availableSpecializations = useGetAvailableSpecializationsQuery();

   const selectSpecialization = (specialization: string) => {
      if (selectedSpecialization === specialization) {
         setSelectedSpecialization(undefined);
      } else {
         setSelectedSpecialization(specialization);
      }
   };

   return (
      <Card className={className}>
         <p className="section-title">{t("photographer_list_page.specialization")}</p>
         <>
            {availableSpecializations.isSuccess &&
               availableSpecializations?.data?.map((specialization, key) => {
                  return (
                     <Checkbox
                        key={key}
                        id={specialization}
                        value={selectedSpecialization === specialization ? true : false}
                        onChange={(e) => {
                           selectSpecialization(specialization);
                        }}
                        disabled={
                           selectedSpecialization === specialization ||
                           selectedSpecialization === undefined
                              ? false
                              : true
                        }
                     >
                        {t(`photographer_specialization.${specialization.toLowerCase()}`)}
                     </Checkbox>
                  );
               })}
         </>
      </Card>
   );
};
