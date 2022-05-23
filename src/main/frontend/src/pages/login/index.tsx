import React, { ChangeEvent, useState } from "react";
import "./style.scss";
import Card from "components/shared/Card";
import TextInput from "components/shared/TextInput";
import Checkbox from "components/shared/Checkbox";
import Button from "components/shared/Button";
import { getToken } from "actions/loginActions";
import { useNavigate } from "react-router-dom";
import { useAppDispatch } from "redux/hooks";
import { getLoginPayload } from "util/loginUtil";
import { login } from "redux/slices/authSlice";
import { LoginRequest, useLoginMutation } from "redux/service/api";

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

   // const [username, setUsername] = useState<string>("");
   // const [password, setPassword] = useState<string>("");
   const [showMesage, setShowMessage] = useState<boolean>(false);
   const [check, setCheck] = useState<boolean>(false);

   const onSubmit = async (e?: React.FormEvent<HTMLFormElement>) => {
      e.preventDefault();
      try {
         const user = await loginMutation(formState);
         console.log(user);
      } catch (err) {
         setShowMessage(true);
      }
      // try {
      //    await getToken(username, password);
      //    setShowMessage(false);
      //    dispatch(login(getLoginPayload()));
      //    navigate("/", { replace: true });
      // } catch (err) {
      //    setShowMessage(true);
      // }
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
                     console.log(); // Navigate to register page
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
                  // value={username}
                  // onChange={(e) => setUsername(e.target.value)}
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
                  // value={password}
                  // onChange={(e) => setPassword(e.target.value)}
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
