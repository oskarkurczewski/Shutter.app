import React, { useState } from "react";
import "./style.scss";
import Card from "components/shared/Card";
import TextInput from "components/shared/TextInput";
import Button from "components/shared/Button";

interface LoginProps {
   setToken: (token: string) => void;
}

const LoginPage = ({ setToken }: LoginProps) => {
   const [login, setLogin] = useState<string>("");
   const [password, setPassword] = useState<string>("");

   const onSubmit = (
      e: React.MouseEvent<HTMLDivElement, MouseEvent> &
         React.KeyboardEvent<HTMLDivElement>
   ) => {
      console.log(e);
      // logowanie
   };

   return (
      <section className="login-wrapper">
         <Card>
            <h3>Zaloguj się</h3>
            <TextInput
               icon="person"
               placeholder="Login"
               value={login}
               onChange={(e) => setLogin(e.target.value)}
            />
            <TextInput
               icon="lock"
               type="password"
               placeholder="Password"
               value={password}
               onChange={(e) => setPassword(e.target.value)}
            />
            <Button icon="send" onClick={onSubmit}>
               Zaloguj się
            </Button>
         </Card>
      </section>
   );
};

export default LoginPage;
