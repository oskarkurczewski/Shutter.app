import React, { useState } from "react";
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

const LoginPage = () => {
   const [username, setUsername] = useState<string>("");
   const [password, setPassword] = useState<string>("");
   const [showMesage, setShowMessage] = useState<boolean>(false);
   const [check, setCheck] = useState<boolean>(false);

   const navigate = useNavigate();
   const dispatch = useAppDispatch();

   const onSubmit = async (e?: React.FormEvent<HTMLFormElement>) => {
      e.preventDefault();
      try {
         await getToken(username, password);
         setShowMessage(false);
         navigate("/dashboard");
         dispatch(login(getLoginPayload()));
      } catch (err) {
         setShowMessage(true);
      }
   };

   return (
      <section className="login-wrapper">
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
                  value={username}
                  onChange={(e) => setUsername(e.target.value)}
               />
               <TextInput
                  label="Hasło"
                  type="password"
                  placeholder="Hasło"
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
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
