import jwtDecode from "jwt-decode";

export const getToken = async (login: string, password: string): Promise<string> => {
   // const url = "http://studapp.it.p.lodz.pl:8002/ssbd02-0.0.1/api/auth/login";
   const url = "http://localhost:8080/ssbd02-0.0.4/api/auth/login";
   const loginData = {
      login,
      password,
   };
   const headers = {
      "Content-Type": "application/json; charset=utf-8",
   };

   const request = {
      method: "POST",
      body: JSON.stringify(loginData),
      headers,
   };
   let token = "";
   const response = await fetch(url, request);

   if (response.ok) {
      token = await response.text();
      localStorage.setItem("token", token);
   } else {
      throw new Error("Wrong login or password!");
   }

   return token;
};
