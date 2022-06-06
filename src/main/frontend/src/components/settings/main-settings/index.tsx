import React, { useEffect, useState } from "react";
import Card from "components/shared/card";
import TextInput from "components/shared/text-input";
import Button from "components/shared/button";
import { useChangeOwnUserDataMutation, useCurrentUserInfoQuery } from "redux/service/api";
import { useTranslation } from "react-i18next";
import { useAppSelector } from "redux/hooks";

const MainSettings = () => {
   const { t } = useTranslation();

   const [name, setName] = useState("");
   const [surname, setSurname] = useState("");

   const { username } = useAppSelector((state) => state.auth);
   const [mutation, { isLoading, isError, isSuccess }] = useChangeOwnUserDataMutation();
   const { data } = useCurrentUserInfoQuery({});

   return (
      <Card id="main-settings">
         <p className="category-title">{t("label.settings")}</p>
         <p>{t("message.info.main-settings")}</p>
         <div className="row">
            <TextInput
               label={t("label.first-name")}
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
               mutation({
                  body: { name, surname, login: username },
                  etag: { etag: data.etag, version: data.data.version },
               });
            }}
         >
            {t("label.change")}
         </Button>
      </Card>
   );
};

export default MainSettings;
