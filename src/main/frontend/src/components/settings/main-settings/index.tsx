import React, { useState } from "react";
import Card from "components/shared/Card";
import TextInput from "components/shared/TextInput";
import Button from "components/shared/Button";
import { useChangeOwnUserDataMutation } from "redux/service/api";
import { useTranslation } from "react-i18next";

const MainSettings = () => {
   const { t } = useTranslation();

   const [name, setName] = useState("");
   const [surname, setSurname] = useState("");

   const [mutation, { isLoading, isError, isSuccess }] = useChangeOwnUserDataMutation();

   return (
      <Card id="main-settings">
         <p className="category-title">{t("label.settings")}</p>
         <p>{t("message.info.main-settings")}</p>
         <div className="row">
            <TextInput
               label={t("labe.first-name")}
               placeholder={t("label.first-name")}
               required
               value={name}
               onChange={(e) => setName(e.target.value)}
            />
            <TextInput
               label={t("label.second-name")}
               placeholder={t("label.second-name")}
               required
               value={surname}
               onChange={(e) => setSurname(e.target.value)}
            />
         </div>

         {isLoading && <p>{t("message.loading.main-settings")}</p>}
         {isError && <p>{t("message.error.main-settings")}</p>}
         {isSuccess && <p>{t("message.success.main-settings")}</p>}

         <Button
            onClick={() => {
               mutation({ name, surname });
            }}
         >
            {t("label.change")}
         </Button>
      </Card>
   );
};

export default MainSettings;
