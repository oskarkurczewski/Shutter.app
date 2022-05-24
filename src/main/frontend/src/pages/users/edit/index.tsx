import Button from "components/shared/Button";
import Card from "components/shared/Card";
import Checkbox from "components/shared/Checkbox";
import Dropdown from "components/shared/Dropdown";
import SquareButton from "components/shared/SquareButton";
import TextInput from "components/shared/TextInput";
import React, { useEffect, useState } from "react";
import { FaMinus, FaPlus } from "react-icons/fa";
import { useNavigate, useParams } from "react-router-dom";
import {
   useAdvancedUserInfoQuery,
   useChangeAccessLevelMutation,
   useEditAccountInfoAsAdminMutation,
} from "redux/service/api";
import "./style.scss";

const EditAccountPage = () => {
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

   const userInfo = useAdvancedUserInfoQuery(login);

   const userRoles = useAdvancedUserInfoQuery(login);

   const [refresh, setRefresh] = useState(true);

   useEffect(() => {
      console.log(userInfo.data);
      setFormData({
         ...formData,
         login: userInfo.data?.login,
         name: userInfo.data?.name,
         surname: userInfo.data?.surname,
         email: userInfo.data?.email,
         email2: userInfo.data?.email,
         registered: userInfo.data?.registered,
         active: userInfo.data?.active,
      });
   }, [userInfo]);

   const mutationInfo = useEditAccountInfoAsAdminMutation();

   const mutationLevel = useChangeAccessLevelMutation();

   useEffect(() => {
      userRoles.data?.accessLevelList && setAllRoles([...userInfo.data.accessLevelList]);
   }, [userRoles.data, mutationLevel[1].isLoading]);

   const [emailCheck, setEmailCheck] = useState(false);

   const [selectedRole, setSelectedRole] = useState("CLIENT");

   useEffect(() => {
      setEmailCheck(formData.email == formData.email2);
   }, [formData]);

   useEffect(() => {
      mutationInfo[1].isSuccess && navigate("/users");
   }, [mutationInfo[1].isSuccess]);

   const navigate = useNavigate();

   return (
      <div className="create-account-page-wrapper">
         <Card className="card">
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
            <div className="access-levels-wrapper">
               <div className="access-levels">
                  {allRoles &&
                     allRoles.map((accessLevel) => {
                        return <p key={accessLevel.name}>{accessLevel.name}</p>;
                     })}
               </div>
               <div className="buttons">
                  <SquareButton
                     onClick={() => {
                        mutationLevel[0]({
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
                        mutationLevel[0]({
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
                  <p className="error-message">Adresy email różnią się od siebie</p>
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
                        mutationInfo[0]({
                           params: { login: formData.login },
                           body: {
                              email: formData.email,
                              name: formData.name,
                              surname: formData.surname,
                           },
                        });
                  }}
               >
                  Zapisz
               </Button>
               {mutationInfo[1].isError && (
                  <p className="error-message">Nie można zapisać edycji</p>
               )}
            </div>
         </Card>
         <Card className="card">
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

export default EditAccountPage;
