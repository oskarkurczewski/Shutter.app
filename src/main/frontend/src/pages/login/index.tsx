import React, { ChangeEvent, useState } from "react";
import "./style.scss";
import Card from "components/shared/Card";
import TextInput from "components/shared/TextInput";
import Checkbox from "components/shared/Checkbox";
import Button from "components/shared/Button";
import { useNavigate } from "react-router-dom";
import { useAppDispatch } from "redux/hooks";
import { getLoginPayload } from "util/loginUtil";
import { login } from "redux/slices/authSlice";
import { useLoginMutation } from "redux/service/api";
import { LoginRequest } from "redux/types/api/authTypes";

const LoginPage = () => {
   const navigate = useNavigate();
   const dispatch = useAppDispatch();

   const [formState, setFormState] = useState<LoginRequest>({
      login: "",
      password: "",
      twoFACode: "000000",
   });

   const [loginMutation, test] = useLoginMutation();
   console.log(test);

   const handleChange = ({ target: { name, value } }: ChangeEvent<HTMLInputElement>) =>
      setFormState((prev) => ({ ...prev, [name]: value }));

   const [showMesage, setShowMessage] = useState<boolean>(false);
   const [check, setCheck] = useState<boolean>(false);

   const onSubmit = async (e?: React.FormEvent<HTMLFormElement>) => {
   const navigate = useNavigate();
   const dispatch = useAppDispatch();

   const onSubmit = async (e) => {
      e.preventDefault();
      try {
         const token = await loginMutation(formState).unwrap();
         localStorage.setItem("token", token.token);
         dispatch(login(getLoginPayload()));
         navigate(-1);
      } catch (err) {
         setShowMessage(true);
      }
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
                  <p>{test.isLoading ? "loading..." : "done"}</p>
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
               <Checkbox value={check} onChange={(e) => setCheck(e.target.checked)}>
                  Zapamiętaj mnie
               </Checkbox>
               {showMesage && <p className="message">Zły login lub hasło</p>}
               <div className="footer">
                  <a href="#a">Zapomniałeś hasła?</a>
                  <Button onClick={(e) => onSubmit(e)}>Zaloguj się</Button>
               </div>
            </form>
         </Card>
      </section>
   );
};

export default LoginPage;
