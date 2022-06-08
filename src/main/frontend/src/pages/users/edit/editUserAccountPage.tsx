import React, { useEffect, useState } from "react";
import styles from "./editUserAccountPage.module.scss";
import {
   Button,
   Card,
   Checkbox,
   Dropdown,
   SquareButton,
   TextInput,
} from "components/shared";
import { FaMinus, FaPlus } from "react-icons/fa";
import { useNavigate, useParams } from "react-router-dom";
import {
   useAdvancedUserInfoQuery,
   useChangeAccessLevelMutation,
   useEditAccountInfoAsAdminMutation,
} from "redux/service/api";

export const EditUserAccountPage = () => {
   const { login } = useParams();

   const [formData, setFormData] = useState({
      login: "",
      name: "",
      surname: "",
      email: "",
      email2: "",
      registered: false,
      active: false,
   });

   const [allRoles, setAllRoles] = useState([]);
   const [refresh, setRefresh] = useState(true);

   const [emailCheck, setEmailCheck] = useState(false);
   const [selectedRole, setSelectedRole] = useState("CLIENT");

   const { data: userInfoData } = useAdvancedUserInfoQuery(login);
   const { data: userRolesData } = useAdvancedUserInfoQuery(login);

   const [infoMutation, infoMutationState] = useEditAccountInfoAsAdminMutation();
   const [accessLevelMutation, accessLevelMutationState] = useChangeAccessLevelMutation();

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
      userRolesData?.data.accessLevelList &&
         setAllRoles([...userInfoData.data.accessLevelList]);
   }, [userRolesData, accessLevelMutationState.isLoading]);

   useEffect(() => {
      setEmailCheck(formData.email == formData.email2);
   }, [formData]);

   useEffect(() => {
      infoMutationState.isSuccess && navigate("/users");
   }, [infoMutationState.isSuccess]);

   const navigate = useNavigate();

   return (
      <div className={styles.create_account_page_wrapper}>
         <Card className={styles.card_wrapper}>
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
            <div className={styles.access_levels_wrapper}>
               <div className={styles.access_levels}>
                  {allRoles &&
                     allRoles.map((accessLevel) => {
                        return <p key={accessLevel.name}>{accessLevel.name}</p>;
                     })}
               </div>
               <div className={styles.buttons}>
                  <SquareButton
                     onClick={() => {
                        accessLevelMutation({
                           params: { login: formData.login },
                           body: { accessLevel: selectedRole, active: true },
                        });
                        setRefresh(!refresh);
                     }}
                  >
                     <FaPlus />
                  </SquareButton>
                  <SquareButton
                     onClick={() => {
                        accessLevelMutation({
                           params: { login: formData.login },
                           body: { accessLevel: selectedRole, active: false },
                        });
                        setRefresh(!refresh);
                     }}
                  >
                     <FaMinus />
                  </SquareButton>
               </div>
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
                  <p className={styles.error_message}>
                     Adresy email różnią się od siebie
                  </p>
               )}
            </div>

            <div>
               <Checkbox
                  value={formData.active}
                  onChange={(e) => {
                     setFormData({ ...formData, active: e.target.checked });
                  }}
               >
                  Aktywne
               </Checkbox>
               <Checkbox
                  value={formData.registered}
                  onChange={(e) => {
                     setFormData({ ...formData, registered: e.target.checked });
                  }}
               >
                  Zarejestrowane
               </Checkbox>
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
                           etag: {
                              version: userInfoData.data.version,
                              etag: userInfoData.etag,
                           },
                        });
                  }}
               >
                  Zapisz
               </Button>
               {infoMutationState.isError && (
                  <p className={styles.error_message}>Nie można zapisać edycji</p>
               )}
            </div>
         </Card>
         <Card className={styles.card_wrapper}>
            <Dropdown
               values={["MODERATOR", "PHOTOGRAPHER", "CLIENT"]}
               selectedValue={selectedRole}
               name="selectRole"
               id="selectRole"
               onChange={(e) => {
                  setSelectedRole(e.target.value);
               }}
            >
               Role
            </Dropdown>
         </Card>
      </div>
   );
};
