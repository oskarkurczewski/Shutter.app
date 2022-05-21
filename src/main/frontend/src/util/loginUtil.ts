import jwtDecode from "jwt-decode";

export const getLoginPayload = () => {
   const decoded = jwtDecode(localStorage.getItem("token") || "");
   const decodedJson = JSON.parse(JSON.stringify(decoded));
   const roles = decodedJson.roles.split(",");
   const name = decodedJson.sub;

   return { username: name, accessLevel: roles };
};

export const getTokenExp = () => {
   const decoded = jwtDecode(localStorage.getItem("token") || "");

   const decodedJson = JSON.parse(JSON.stringify(decoded));
   const exp = decodedJson.exp * 1000;

   return exp;
};
