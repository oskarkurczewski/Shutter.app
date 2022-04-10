export const getToken = async (login: string, password: string): Promise<string> => {
   //TODO: change url to studapp when it will start working
   const url = "http://localhost:8088/ssbd02-0.0.1/api/auth/login";
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
   } else {
      throw new Error("Wrong login or password!");
   }

   return token;
};
