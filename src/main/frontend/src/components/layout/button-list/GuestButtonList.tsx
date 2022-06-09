import React, { FC } from "react";
import { BarButton } from "components/layout";
import { MdSpaceDashboard } from "react-icons/md";
import { useTranslation } from "react-i18next";

interface Props {
   path: string;
   expanded?: boolean;
}

export const GuestButtonList: FC<Props> = ({ path, expanded }) => {
   const { t } = useTranslation();

   const arr = [
      { to: "/", icon: <MdSpaceDashboard />, text: t("navbar.buttons.homepage") },
   ];

   return (
      <>
         {arr.map((btn) => (
            <BarButton
               active={path === btn.to}
               expanded={expanded}
               to={btn.to}
               icon={btn.icon}
               key={btn.to}
               text={btn.text}
            />
         ))}
      </>
   );
};
