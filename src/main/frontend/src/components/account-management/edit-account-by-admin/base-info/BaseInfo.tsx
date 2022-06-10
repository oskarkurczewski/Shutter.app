import { Button, Card, Checkbox, TextInput } from "components/shared";
import React, { useEffect, useState } from "react";
import { useChangeAccountInfoMutation } from "redux/service/usersManagementService";
import { advancedUserInfoResponse } from "redux/types/api";
import { EtagData } from "redux/types/api/dataTypes";
import styles from "./BaseInfo.module.scss";

interface Props {
   userInfoData: EtagData<advancedUserInfoResponse>;
}

export const BaseInfo: React.FC<Props> = ({ userInfoData }) => {
   const [formData, setFormData] = useState({
      login: "",
      name: "",
      surname: "",
      email: "",
      email2: "",
      registered: false,
      active: false,
   });
   const [emailCheck, setEmailCheck] = useState(false);

   useEffect(() => {
      setFormData({
         ...formData,
         login: userInfoData?.data.login,
         name: userInfoData?.data.name,
         surname: userInfoData?.data.surname,
         email: userInfoData?.data.email,
         email2: userInfoData?.data.email,
         registered: userInfoData?.data.registered,
         active: userInfoData?.data.active,
      });
   }, [userInfoData]);

   useEffect(() => {
      setEmailCheck(formData.email == formData.email2);
   }, [formData.email, formData.email2]);

   const [infoMutation, infoMutationState] = useChangeAccountInfoMutation();

   useEffect(() => {
      // TODO: add toast
   }, [infoMutationState.isSuccess]);

   return (
      <Card className={styles.base_info}>
         <div>
            <TextInput
               value={formData.login}
               onChange={(e) => {
                  setFormData({ ...formData, login: e.target.value });
               }}
               label="Login"
               className="text"
               disabled
            />
         </div>
         <div>
            <Checkbox
               id="active"
               value={formData.active}
               onChange={(e) => {
                  setFormData({ ...formData, active: e.target.checked });
               }}
            >
               Aktywne
            </Checkbox>
            <Checkbox
               id="registered"
               value={formData.registered}
               onChange={(e) => {
                  setFormData({ ...formData, registered: e.target.checked });
               }}
            >
               Zarejestrowane
            </Checkbox>
         </div>
         <div>
            <TextInput
               value={formData.name}
               onChange={(e) => {
                  setFormData({ ...formData, name: e.target.value });
               }}
               label="Imię"
               required
               className="text"
            />
         </div>
         <div>
            <TextInput
               value={formData.surname}
               onChange={(e) => {
                  setFormData({ ...formData, surname: e.target.value });
               }}
               label="Nazwisko"
               required
               className="text"
            />
         </div>
         <div>
            <TextInput
               value={formData.email}
               onChange={(e) => {
                  setFormData({ ...formData, email: e.target.value });
               }}
               label="Email"
               required
               className="text"
               type="email"
            />
         </div>
         <div>
            <TextInput
               value={formData.email2}
               onChange={(e) => {
                  setFormData({ ...formData, email2: e.target.value });
               }}
               label="Powtórz email"
               required
               className="text"
               type="email"
            />
            {!emailCheck && (
               <p className={styles.error_message}>Adresy email różnią się od siebie</p>
            )}
         </div>

         <div>
            <Button
               onClick={() => {
                  emailCheck &&
                     infoMutation({
                        body: {
                           login: formData.login,
                           email: formData.email,
                           name: formData.name,
                           surname: formData.surname,
                        },
                        etag: userInfoData.etag,
                     });
               }}
               disabled={!emailCheck}
            >
               Zapisz
            </Button>
            {infoMutationState.isError && (
               <p className={styles.error_message}>Nie można zapisać edycji</p>
            )}
         </div>
      </Card>
   );
};
