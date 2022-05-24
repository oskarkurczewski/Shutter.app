import React, { ChangeEvent, useState } from "react";
import "./style.scss";
import Card from "components/shared/Card";
import TextInput from "components/shared/TextInput";
import Checkbox from "components/shared/Checkbox";
import Button from "components/shared/Button";
import { Link, useNavigate } from "react-router-dom";
import { useAppDispatch } from "redux/hooks";
import { getLoginPayload } from "util/loginUtil";
import { login } from "redux/slices/authSlice";
import { useLoginMutation, useSendTwoFACodeMutation } from "redux/service/api";
import { LoginRequest } from "redux/types/api/authTypes";

const LoginPage: React.FC = () => {
   const navigate = useNavigate();
   const dispatch = useAppDispatch();

   const [formState, setFormState] = useState<LoginRequest>({
      login: "",
      password: "",
      twoFACode: "000000",
   });

   const [loginMutation, loginMutationState] = useLoginMutation();
   const [sendTwoFACodeMutation, sendTwoFACodeMutationState] = useSendTwoFACodeMutation();

   const handleChange = ({ target: { name, value } }: ChangeEvent<HTMLInputElement>) =>
      setFormState((prev) => ({ ...prev, [name]: value }));

   const onSendTwoFA = (e: React.MouseEvent<HTMLButtonElement, MouseEvent>) => {
      e.preventDefault();
      sendTwoFACodeMutation(formState.login);
   };

   const onSubmit = async (e) => {
      e.preventDefault();
      const token = await loginMutation(formState).unwrap();
      localStorage.setItem("token", token.token);
      dispatch(login(getLoginPayload()));
      navigate("/");
   };

   return (
      <section className="login-page-wrapper">
         <Card>
            <div className="aside">
               <img src="images/logo_new_black.svg" alt="logo" />
               <div>
                  <p className="section-title">
                     Nie masz <br /> jeszcze konta?
                  </p>
                  <p>
                     Załóż je teraz, <br />
                     to zupełnie darmowe!
                  </p>
               </div>
               <Button
                  onClick={() => {
                     navigate("/register");
                  }}
               >
                  Zarejestruj się
               </Button>
            </div>
            <form onSubmit={onSubmit}>
               <p className="section-title">Logowanie</p>
               <TextInput
                  label="Login"
                  placeholder="Login"
                  type="text"
                  name="login"
                  value={formState.login}
                  onChange={handleChange}
               />
               <TextInput
                  label="Hasło"
                  type="password"
                  placeholder="Hasło"
                  value={formState.password}
                  name="password"
                  onChange={handleChange}
               />
               <div className="two-factory-auth">
                  <TextInput
                     label="Kod uwierzytelnienia"
                     placeholder="Kod"
                     value={formState.twoFACode}
                     name="twoFACode"
                     onChange={handleChange}
                  />
                  <Button onClick={onSendTwoFA}>Send Code</Button>
               </div>

               {/* <Checkbox value={check} onChange={(e) => setCheck(e.target.checked)}>
                  Zapamiętaj mnie
               </Checkbox> */}

               <p>
                  {(loginMutationState.isLoading ||
                     sendTwoFACodeMutationState.isLoading) &&
                     "loading..."}
               </p>

               {loginMutationState.isError && (
                  <p className="message">Zły login lub hasło</p>
               )}
               {sendTwoFACodeMutationState.isError && (
                  <p className="message">Nie udało się wysłać kodu</p>
               )}
               {sendTwoFACodeMutationState.isSuccess && <p>Pomyślnie wysłano kod</p>}

               <div className="footer">
                  <Link to={"/request-reset-password"}>Zapomniałeś hasła?</Link>
                  <Button onClick={(e) => onSubmit(e)}>Zaloguj się</Button>
               </div>
            </form>
         </Card>
      </section>
   );
};

export default LoginPage;
