import { Button, Card, TextInput } from "components/shared";
import React, { useState } from "react";
import styles from "./Password.module.scss";

export const Password = () => {
   const [data, setData] = useState({ password: "", password2: "" });

   const save = () => {
      true;
   };

   return (
      <Card className={styles["changePassword-wrapper"]}>
         <div>
            <TextInput value={data.password} label="Hasło" />
            <TextInput value={data.password2} label="Powtórz hasło" />
         </div>
         <Button onClick={save} className={styles.save}>
            zApIsZ
         </Button>
      </Card>
   );
};
