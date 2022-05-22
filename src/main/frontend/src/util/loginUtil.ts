import jwtDecode from "jwt-decode";

export const getLoginPayload = () => {
   const decoded = jwtDecode(localStorage.getItem("token") || "");
   const decodedJson = JSON.parse(JSON.stringify(decoded));
   const roles = decodedJson.roles.split(",");
   const username = decodedJson.sub;
   const accessLevel = roles[0];

   return { username, roles, accessLevel };
};

export const getTokenExp = () => {
   const decoded = jwtDecode(localStorage.getItem("token") || "");

   const decodedJson = JSON.parse(JSON.stringify(decoded));
   const exp = decodedJson.exp * 1000;

   return exp;
};
