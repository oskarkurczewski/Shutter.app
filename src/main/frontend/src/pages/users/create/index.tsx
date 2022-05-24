import Button from "components/shared/Button";
import Card from "components/shared/Card";
import Checkbox from "components/shared/Checkbox";
import TextInput from "components/shared/TextInput";
import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { useRegisterAsAdminMutation } from "redux/service/api";
import "./style.scss";

const CreateAccountPage = () => {
   const [formData, setFormData] = useState({
      login: "",
      password: "",
      password2: "",
      name: "",
      surname: "",
      email: "",
      email2: "",
      registered: false,
      active: false,
   });

   const [emailCheck, setEmailCheck] = useState(false);
   const [passwordCheck, setPasswordCheck] = useState(false);
   const [mutation, { isLoading, isError, isSuccess }] = useRegisterAsAdminMutation();

   useEffect(() => {
      setEmailCheck(formData.email == formData.email2);
      setPasswordCheck(formData.password == formData.password2);
   }, [formData]);

   useEffect(() => {
      isSuccess && navigate("/users");
   }, [isSuccess]);

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
                  required
                  className="text"
               />
            </div>
            <div />
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
               <TextInput
                  value={formData.password}
                  onChange={(e) => {
                     setFormData({ ...formData, password: e.target.value });
                  }}
                  label="Hasło"
                  required
                  className="text"
                  type="password"
               />
            </div>
            <div>
               <TextInput
                  value={formData.password2}
                  onChange={(e) => {
                     setFormData({ ...formData, password2: e.target.value });
                  }}
                  label="Powtórz hasło"
                  required
                  className="text"
                  type="password"
               />
               {!passwordCheck && (
                  <p className="error-message">Hasła różnią się od siebie</p>
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
                     if (emailCheck && passwordCheck) {
                        mutation({
                           login: formData.login,
                           password: formData.password,
                           email: formData.email,
                           name: formData.name,
                           surname: formData.surname,
                           registered: formData.registered,
                           active: formData.active,
                        });
                     }
                  }}
               >
                  Stwórz konto
               </Button>
               {isError && <p className="error-message">Nie można utworzyć konta</p>}
            </div>
         </Card>
      </div>
   );
};

export default CreateAccountPage;
