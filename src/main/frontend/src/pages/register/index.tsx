import React, { useEffect, useState } from "react";
import "./style.scss";
import Button from "components/shared/Button";
import Card from "components/shared/Card";
import Checkbox from "components/shared/Checkbox";
import TextInput from "components/shared/TextInput";
import ValidationBox from "components/shared/ValidationBox";
import { Link } from "react-router-dom";
import { validateFields } from "./validation";

const RegisterPage = () => {
   const [formData, setFormData] = useState({
      login: "",
      email: "",
      password: "",
      confirmPassword: "",
      name: "",
      surname: "",
      userDataChecked: null,
      termsOfUseChecked: null,
   });

   const [validation, setValidation] = useState(validateFields(formData));
   useEffect(() => {
      setValidation(validateFields(formData));
   }, [formData]);

   const onSubmit = (e) => {
      e.preventDefault();
      console.log(formData);
   };

   return (
      <section className="register-page-wrapper">
         <div className="form-wrapper">
            <ValidationBox data={validation} />
            <Card className="register-form">
               <form onSubmit={onSubmit}>
                  <p className="section-title">Rejestracja</p>
                  <div className="inputs-wrapper">
                     <div className="column">
                        <TextInput
                           label="Login"
                           placeholder="Login"
                           required
                           value={formData.login}
                           onChange={(e) => {
                              setFormData({
                                 ...formData,
                                 login: e.target.value,
                              });
                           }}
                        />
                        <TextInput
                           label="Email"
                           placeholder="Email"
                           required
                           value={formData.email}
                           onChange={(e) => {
                              setFormData({
                                 ...formData,
                                 email: e.target.value,
                              });
                           }}
                        />
                        <TextInput
                           label="Hasło"
                           placeholder="Hasło"
                           required
                           type="password"
                           value={formData.password}
                           onChange={(e) => {
                              setFormData({
                                 ...formData,
                                 password: e.target.value,
                              });
                           }}
                        />
                        <TextInput
                           label="Powtórz hasło"
                           placeholder="Hasło"
                           required
                           type="password"
                           value={formData.confirmPassword}
                           onChange={(e) => {
                              setFormData({
                                 ...formData,
                                 confirmPassword: e.target.value,
                              });
                           }}
                        />
                     </div>
                     <div className="column">
                        <TextInput
                           label="Imię"
                           placeholder="Imię"
                           required
                           value={formData.name}
                           onChange={(e) => {
                              setFormData({
                                 ...formData,
                                 name: e.target.value,
                              });
                           }}
                        />
                        <TextInput
                           label="Nazwisko"
                           placeholder="Nazwisko"
                           required
                           value={formData.surname}
                           onChange={(e) => {
                              setFormData({
                                 ...formData,
                                 surname: e.target.value,
                              });
                           }}
                        />
                     </div>
                  </div>
                  <div className="checkboxes-wrapper">
                     <Checkbox
                        required
                        value={formData.userDataChecked}
                        onChange={(e) => {
                           setFormData({
                              ...formData,
                              userDataChecked: e.target.checked,
                           });
                        }}
                     >
                        Wyrażam zgodę na przetwarzanie moich danych osobowych
                     </Checkbox>
                     <Checkbox
                        required
                        value={formData.termsOfUseChecked}
                        onChange={(e) => {
                           setFormData({
                              ...formData,
                              termsOfUseChecked: e.target.checked,
                           });
                        }}
                     >
                        Potwierdzam, że zapoznałem się z regulaminem
                     </Checkbox>
                  </div>
                  <div className="footer">
                     <Link to="/login">Masz już konto? Zaloguj się</Link>
                     <Button onClick={onSubmit}>Zarejestruj się</Button>
                  </div>
               </form>
            </Card>
         </div>
      </section>
   );
};

export default RegisterPage;
