import React, { useState } from "react";
import "./style.scss";
import Card from "components/shared/Card";
import TextInput from "components/shared/TextInput";
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
   const navigate = useNavigate();
   const dispatch = useAppDispatch();

   const onSubmit = async () => {
      try {
         await getToken(username, password);
         setShowMessage(false);
         dispatch(login(getLoginPayload()));
         navigate("/", { replace: true });
      } catch (err) {
         setShowMessage(true);
      }
   };

   return (
      <section className="login-wrapper">
         <Card>
            <h3>Zaloguj się</h3>
            <TextInput
               icon="person"
               placeholder="Login"
               value={username}
               onChange={(e) => setUsername(e.target.value)}
            />
            <TextInput
               icon="lock"
               type="password"
               placeholder="Password"
               value={password}
               onChange={(e) => setPassword(e.target.value)}
            />
            <p className={`message ${showMesage ? "" : "hidden"}`}>Zły login lub hasło</p>
            <Button icon="send" onClick={onSubmit}>
               Zaloguj się
            </Button>
         </Card>
      </section>
   );
};

export default LoginPage;
