import React, { useEffect, useState } from "react";
import "./style.scss";
import Button from "components/shared/Button";
import Card from "components/shared/Card";
import Checkbox from "components/shared/Checkbox";
import TextInput from "components/shared/TextInput";
import ValidationBox from "components/shared/ValidationBox";
import { Link } from "react-router-dom";
import { validateFields } from "./validation";
import { useRegisterMutation } from "redux/service/api";
import Form from "components/shared/Form";

const RegisterPage = () => {
   const [formState, setFormState] = useState({
      login: "",
      email: "",
      password: "",
      confirmPassword: "",
      name: "",
      surname: "",
   });
   const [checkboxState, setCheckboxState] = useState({
      userDataChecked: null,
      termsOfUseChecked: null,
   });

   const [validation, setValidation] = useState(
      validateFields({ ...formState, ...checkboxState })
   );

   const [registerMutation, { isLoading, isSuccess, isError }] = useRegisterMutation();

   useEffect(() => {
      setValidation(validateFields({ ...formState, ...checkboxState }));
   }, [formState, checkboxState]);

   const handleChange = ({
      target: { name, value },
   }: React.ChangeEvent<HTMLInputElement>) =>
      setFormState((prev) => ({ ...prev, [name]: value }));

   const onSubmit = (e) => {
      e.preventDefault();
      registerMutation(formState);
   };

   return (
      <section className="register-page-wrapper">
         <div className="form-wrapper">
            <ValidationBox data={validation} />
            <Card className="register-form">
               <Form onSubmit={onSubmit} isLoading={isLoading}>
                  <p className="section-title">Rejestracja</p>
                  <div className="inputs-wrapper">
                     <div className="column">
                        <TextInput
                           label="Login"
                           placeholder="Login"
                           required
                           name="login"
                           value={formState.login}
                           onChange={handleChange}
                        />
                        <TextInput
                           label="Email"
                           placeholder="Email"
                           required
                           name="email"
                           value={formState.email}
                           onChange={handleChange}
                        />
                        <TextInput
                           label="Hasło"
                           placeholder="Hasło"
                           required
                           name="password"
                           type="password"
                           value={formState.password}
                           onChange={handleChange}
                        />
                        <TextInput
                           label="Powtórz hasło"
                           placeholder="Hasło"
                           required
                           name="confirmPassword"
                           type="password"
                           value={formState.confirmPassword}
                           onChange={handleChange}
                        />
                     </div>
                     <div className="column">
                        <TextInput
                           label="Imię"
                           placeholder="Imię"
                           required
                           name="name"
                           value={formState.name}
                           onChange={handleChange}
                        />
                        <TextInput
                           label="Nazwisko"
                           placeholder="Nazwisko"
                           required
                           name="surname"
                           value={formState.surname}
                           onChange={handleChange}
                        />
                     </div>
                  </div>
                  <div className="checkboxes-wrapper">
                     <Checkbox
                        required
                        value={checkboxState.userDataChecked}
                        onChange={(e) => {
                           setCheckboxState({
                              ...checkboxState,
                              userDataChecked: e.target.checked,
                           });
                        }}
                     >
                        Wyrażam zgodę na przetwarzanie moich danych osobowych
                     </Checkbox>
                     <Checkbox
                        required
                        value={checkboxState.termsOfUseChecked}
                        onChange={(e) => {
                           setCheckboxState({
                              ...checkboxState,
                              termsOfUseChecked: e.target.checked,
                           });
                        }}
                     >
                        Potwierdzam, że zapoznałem się z regulaminem
                     </Checkbox>
                  </div>

                  {isSuccess && <p>Udało się pomyślnie zarejestrować!</p>}
                  {isError && <p>Nie udało się zarejestrować</p>}

                  <div className="footer">
                     <Link to="/login">Masz już konto? Zaloguj się</Link>
                     <Button onClick={onSubmit}>Zarejestruj się</Button>
                  </div>
               </Form>
            </Card>
         </div>
      </section>
   );
};

export default RegisterPage;
